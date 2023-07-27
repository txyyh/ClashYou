package yos.clash.material.service

import android.content.Context
import android.net.Uri
import yos.clash.material.common.log.Log
import com.github.kr328.clash.core.Clash
import kotlinx.coroutines.Dispatchers
import yos.clash.material.service.data.Imported
import yos.clash.material.service.data.ImportedDao
import yos.clash.material.service.data.Pending
import yos.clash.material.service.data.PendingDao
import yos.clash.material.service.model.Profile
import yos.clash.material.service.remote.IFetchObserver
import yos.clash.material.service.store.ServiceStore
import yos.clash.material.service.util.importedDir
import yos.clash.material.service.util.pendingDir
import yos.clash.material.service.util.processingDir
import yos.clash.material.service.util.sendProfileChanged
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import yos.clash.material.service.data.ProviderMoreInfo
import yos.clash.material.service.data.ProviderMoreInfoDao
import yos.clash.material.service.model.MoreInfo
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import java.util.concurrent.TimeUnit

object ProfileProcessor {
    private val profileLock = Mutex()
    private val processLock = Mutex()

    suspend fun apply(context: Context, uuid: UUID, callback: IFetchObserver? = null) {
        withContext(NonCancellable) {
            processLock.withLock {
                val snapshot = profileLock.withLock {
                    val pending = PendingDao().queryByUUID(uuid)
                        ?: throw IllegalArgumentException("profile $uuid not found")

                    pending.enforceFieldValid()

                    context.processingDir.deleteRecursively()
                    context.processingDir.mkdirs()

                    context.pendingDir.resolve(pending.uuid.toString())
                        .copyRecursively(context.processingDir, overwrite = true)

                    pending
                }

                val force = snapshot.type != Profile.Type.File
                var cb = callback

                Clash.fetchAndValid(context.processingDir, snapshot.source, force) {
                    try {
                        cb?.updateStatus(it)
                    } catch (e: Exception) {
                        cb = null

                        Log.w("Report fetch status: $e", e)
                    }
                }.await()

                val subInfo = if (snapshot.type == Profile.Type.Url) {
                    fetchSubscriptionUserInfo(snapshot.source)
                } else {
                    null
                }

                profileLock.withLock {
                    if (PendingDao().queryByUUID(snapshot.uuid) == snapshot) {
                        context.importedDir.resolve(snapshot.uuid.toString())
                            .deleteRecursively()
                        context.processingDir
                            .copyRecursively(context.importedDir.resolve(snapshot.uuid.toString()))

                        val old = ImportedDao().queryByUUID(snapshot.uuid)

                        val new = Imported(
                            snapshot.uuid,
                            snapshot.name,
                            snapshot.type,
                            snapshot.source,
                            snapshot.interval,
                            old?.createdAt ?: System.currentTimeMillis()
                        )

                        if (old != null) {
                            ImportedDao().update(new)
                        } else {
                            ImportedDao().insert(new)
                        }

                        PendingDao().remove(snapshot.uuid)

                        subInfo?.let {
                            ProviderMoreInfoDao().setInfo(
                                ProviderMoreInfo(
                                    uuid = snapshot.uuid,
                                    upload = it.uploadBase,
                                    download = it.downloadBase,
                                    total = it.totalBase,
                                    expire = it.expireTime
                                )
                            )
                        }

                        context.pendingDir.resolve(snapshot.uuid.toString())
                            .deleteRecursively()

                        context.sendProfileChanged(snapshot.uuid)
                    }
                }
            }
        }
    }

    suspend fun update(context: Context, uuid: UUID, callback: IFetchObserver?) {
        withContext(NonCancellable) {
            processLock.withLock {
                val snapshot = profileLock.withLock {
                    val imported = ImportedDao().queryByUUID(uuid)
                        ?: throw IllegalArgumentException("profile $uuid not found")

                    context.processingDir.deleteRecursively()
                    context.processingDir.mkdirs()

                    context.importedDir.resolve(imported.uuid.toString())
                        .copyRecursively(context.processingDir, overwrite = true)

                    imported
                }

                var cb = callback

                Clash.fetchAndValid(context.processingDir, snapshot.source, true) {
                    try {
                        cb?.updateStatus(it)
                    } catch (e: Exception) {
                        cb = null

                        Log.w("Report fetch status: $e", e)
                    }
                }.await()

                val subInfo = if (snapshot.type == Profile.Type.Url) {
                    fetchSubscriptionUserInfo(snapshot.source)
                } else {
                    null
                }

                profileLock.withLock {
                    if (ImportedDao().exists(snapshot.uuid)) {
                        context.importedDir.resolve(snapshot.uuid.toString()).deleteRecursively()
                        context.processingDir
                            .copyRecursively(context.importedDir.resolve(snapshot.uuid.toString()))

                        subInfo?.let {
                            ProviderMoreInfoDao().setInfo(
                                ProviderMoreInfo(
                                    uuid = snapshot.uuid,
                                    upload = it.uploadBase,
                                    download = it.downloadBase,
                                    total = it.totalBase,
                                    expire = it.expireTime
                                )
                            )
                        }

                        context.sendProfileChanged(snapshot.uuid)
                    }
                }
            }
        }
    }

    suspend fun delete(context: Context, uuid: UUID) {
        withContext(NonCancellable) {
            profileLock.withLock {
                ImportedDao().remove(uuid)
                PendingDao().remove(uuid)

                val pending = context.pendingDir.resolve(uuid.toString())
                val imported = context.importedDir.resolve(uuid.toString())

                pending.deleteRecursively()
                imported.deleteRecursively()

                context.sendProfileChanged(uuid)
            }
        }
    }

    suspend fun release(context: Context, uuid: UUID): Boolean {
        return withContext(NonCancellable) {
            profileLock.withLock {
                PendingDao().remove(uuid)

                context.pendingDir.resolve(uuid.toString()).deleteRecursively()
            }
        }
    }

    suspend fun active(context: Context, uuid: UUID) {
        withContext(NonCancellable) {
            profileLock.withLock {
                if (ImportedDao().exists(uuid)) {
                    val store = ServiceStore(context)

                    store.activeProfile = uuid

                    context.sendProfileChanged(uuid)
                }
            }
        }
    }

    private fun Pending.enforceFieldValid() {
        val scheme = Uri.parse(source)?.scheme?.lowercase(Locale.getDefault())

        when {
            name.isBlank() ->
                throw IllegalArgumentException("Empty name")

            source.isEmpty() && type != Profile.Type.File ->
                throw IllegalArgumentException("Invalid url")

            source.isNotEmpty() && scheme != "https" && scheme != "http" && scheme != "content" ->
                throw IllegalArgumentException("Unsupported url $source")

            interval != 0L && TimeUnit.MILLISECONDS.toMinutes(interval) < 15 ->
                throw IllegalArgumentException("Invalid interval")
        }
    }

    private fun String.find(regex: String, group: Int): String? {
        return regex.toRegex().find(this)?.groupValues?.get(group)
    }

    private suspend fun fetchSubscriptionUserInfo(url: String): MoreInfo? {
        return try {
            val userInfoStr = withContext(Dispatchers.IO) {
                arrayOf("Clash", "Quantumultx").forEach { ua ->
                    val conn = URL(url).openConnection() as HttpURLConnection
                    try {
                        conn.requestMethod = "GET"
                        conn.setRequestProperty("User-Agent", ua)

                        if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                            return@withContext conn.headerFields.entries.firstOrNull {
                                "subscription-userinfo".equals(it.key, true)
                            }?.value?.firstOrNull()
                        }
                    } finally {
                        conn.disconnect()
                    }
                }
                null
            }

            userInfoStr?.let {
                MoreInfo(
                    uploadBase = it.find("upload=(\\d+)", 1)!!.toLong(),
                    downloadBase = it.find("download=(\\d+)", 1)!!.toLong(),
                    totalBase = it.find("total=(\\d+)", 1)!!.toLong(),
                    expireTime = it.find("expire=(\\d+)", 1)?.toLong()?.times(1000) ?: -1,
                )
            }
        } catch (e: Exception) {
            null
        }
    }
}