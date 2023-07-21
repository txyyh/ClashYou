package yos.clash.material.design

import android.content.Context
import android.view.View
import yos.clash.material.design.databinding.DesignSettingsCommonBinding
import yos.clash.material.design.model.Behavior
import yos.clash.material.design.model.DarkMode
import yos.clash.material.design.preference.*
import yos.clash.material.design.store.UiStore
import yos.clash.material.design.util.applyFrom
import yos.clash.material.design.util.bindAppBarElevation
import yos.clash.material.design.util.layoutInflater
import yos.clash.material.design.util.root
import yos.clash.material.service.store.ServiceStore

class AppSettingsDesign(
    context: Context,
    uiStore: UiStore,
    srvStore: ServiceStore,
    behavior: Behavior,
    running: Boolean,
) : Design<AppSettingsDesign.Request>(context) {
    enum class Request {
        ReCreateAllActivities
    }

    private val binding = DesignSettingsCommonBinding
        .inflate(context.layoutInflater, context.root, false)

    override val root: View
        get() = binding.root

    init {
        binding.surface = surface

        binding.activityBarLayout.applyFrom(context)

        binding.scrollRoot.bindAppBarElevation(binding.activityBarLayout)

        val screen = preferenceScreen(context) {
            category(R.string.behavior)

            switch(
                value = behavior::autoRestart,
                icon = R.drawable.ic_baseline_restore,
                title = R.string.auto_restart,
                summary = R.string.allow_clash_auto_restart,
            )

            category(R.string.interface_)

            selectableList(
                value = uiStore::darkMode,
                values = DarkMode.values(),
                valuesText = arrayOf(
                    R.string.follow_system_android_10,
                    R.string.always_light,
                    R.string.always_dark
                ),
                icon = R.drawable.ic_baseline_brightness_4,
                title = R.string.dark_mode
            ) {
                listener = OnChangedListener {
                    requests.trySend(Request.ReCreateAllActivities)
                }
            }

            category(R.string.service)

            switch(
                value = srvStore::dynamicNotification,
                icon = R.drawable.ic_baseline_domain,
                title = R.string.show_traffic,
                summary = R.string.show_traffic_summary
            ) {
                enabled = !running
            }
        }

        binding.content.addView(screen.root)
    }
}