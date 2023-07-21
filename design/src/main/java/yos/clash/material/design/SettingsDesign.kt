package yos.clash.material.design

import android.content.Context
import android.view.View
import yos.clash.material.design.databinding.DesignSettingsBinding
import yos.clash.material.design.util.applyFrom
import yos.clash.material.design.util.bindAppBarElevation
import yos.clash.material.design.util.layoutInflater
import yos.clash.material.design.util.root

class SettingsDesign(context: Context) : Design<SettingsDesign.Request>(context) {
    enum class Request {
        StartApp, StartNetwork, StartOverride,
    }

    private val binding = DesignSettingsBinding
        .inflate(context.layoutInflater, context.root, false)

    override val root: View
        get() = binding.root

    init {
        binding.self = this

        binding.activityBarLayout.applyFrom(context)

        binding.scrollRoot.bindAppBarElevation(binding.activityBarLayout)
    }

    fun request(request: Request) {
        requests.trySend(request)
    }
}