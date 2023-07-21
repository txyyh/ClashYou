package yos.clash.material.design

import android.content.Context
import android.view.View
import yos.clash.material.design.databinding.DesignAppCrashedBinding
import yos.clash.material.design.util.applyFrom
import yos.clash.material.design.util.bindAppBarElevation
import yos.clash.material.design.util.layoutInflater
import yos.clash.material.design.util.root

class AppCrashedDesign(context: Context) : Design<Unit>(context) {
    private val binding = DesignAppCrashedBinding
        .inflate(context.layoutInflater, context.root, false)

    override val root: View
        get() = binding.root

    fun setAppLogs(logs: String) {
        binding.logsView.text = logs
    }

    init {
        binding.self = this

        binding.activityBarLayout.applyFrom(context)

        binding.scrollRoot.bindAppBarElevation(binding.activityBarLayout)
    }
}