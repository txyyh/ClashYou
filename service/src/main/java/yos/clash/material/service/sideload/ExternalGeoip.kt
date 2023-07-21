package yos.clash.material.service.sideload

import android.content.Context
import android.content.pm.PackageManager
import yos.clash.material.common.constants.Metadata
import yos.clash.material.common.log.Log
import java.io.InputStream

fun Context.readGeoipDatabaseFrom(packageName: String): ByteArray? {
    return try {
        val appInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
        val path = appInfo.metaData.getString(Metadata.GEOIP_FILE_NAME) ?: return null

        createPackageContext(packageName, 0)
            .resources.assets.open(path).use(InputStream::readBytes)
    } catch (e: PackageManager.NameNotFoundException) {
        Log.w("Sideload geoip: $packageName not found", e)

        null
    }
}