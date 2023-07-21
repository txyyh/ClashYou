package yos.clash.material.design

import android.content.Context
import android.view.View
import androidx.core.widget.addTextChangedListener
import yos.clash.material.design.adapter.AppAdapter
import yos.clash.material.design.component.AccessControlMenu
import yos.clash.material.design.databinding.DesignAccessControlBinding
import yos.clash.material.design.databinding.DialogSearchBinding
import yos.clash.material.design.dialog.FullScreenDialog
import yos.clash.material.design.model.AppInfo
import yos.clash.material.design.store.UiStore
import yos.clash.material.design.util.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class AccessControlDesign(
    context: Context,
    uiStore: UiStore,
    private val selected: MutableSet<String>,
) : Design<AccessControlDesign.Request>(context) {
    enum class Request {
        ReloadApps,
        SelectAll,
        SelectNone,
        SelectInvert,
        Import,
        Export,
    }

    private val binding = DesignAccessControlBinding
        .inflate(context.layoutInflater, context.root, false)

    private val adapter = AppAdapter(context, selected)

    private val menu: AccessControlMenu by lazy {
        AccessControlMenu(context, binding.menuView, uiStore, requests)
    }

    val apps: List<AppInfo>
        get() = adapter.apps

    override val root: View
        get() = binding.root

    suspend fun patchApps(apps: List<AppInfo>) {
        adapter.swapDataSet(adapter::apps, apps, false)
    }

    suspend fun rebindAll() {
        withContext(Dispatchers.Main) {
            adapter.rebindAll()
        }
    }

    init {
        binding.self = this

        binding.activityBarLayout.applyFrom(context)

        binding.mainList.recyclerList.also {
            it.bindAppBarElevation(binding.activityBarLayout)
            it.applyLinearAdapter(context, adapter)
        }

        binding.menuView.setOnClickListener {
            menu.show()
        }

        binding.searchView.setOnClickListener {
            launch {
                try {
                    requestSearch()
                } finally {
                    withContext(NonCancellable) {
                        rebindAll()
                    }
                }
            }
        }
    }

    private suspend fun requestSearch() {
        coroutineScope {
            val binding = DialogSearchBinding
                .inflate(context.layoutInflater, context.root, false)
            val adapter = AppAdapter(context, selected)
            val dialog = FullScreenDialog(context)
            val filter = Channel<Unit>(Channel.CONFLATED)

            dialog.setContentView(binding.root)

            binding.surface = dialog.surface
            binding.mainList.applyLinearAdapter(context, adapter)
            binding.keywordView.addTextChangedListener {
                filter.trySend(Unit)
            }
            binding.closeView.setOnClickListener {
                dialog.dismiss()
            }

            dialog.setOnDismissListener {
                cancel()
            }

            dialog.setOnShowListener {
                binding.keywordView.requestTextInput()
            }

            dialog.show()

            while (isActive) {
                filter.receive()

                val keyword = binding.keywordView.text?.toString() ?: ""

                val apps: List<AppInfo> = if (keyword.isEmpty()) {
                    emptyList()
                } else {
                    withContext(Dispatchers.Default) {
                        apps.filter {
                            it.label.contains(keyword, ignoreCase = true) ||
                                    it.packageName.contains(keyword, ignoreCase = true)
                        }
                    }
                }

                adapter.patchDataSet(adapter::apps, apps, false, AppInfo::packageName)

                delay(200)
            }
        }
    }
}