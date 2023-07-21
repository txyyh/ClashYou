package yos.clash.material.design.dialog

import android.content.Context
import yos.clash.material.design.databinding.DialogFetchStatusBinding
import yos.clash.material.design.util.layoutInflater
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface ModelProgressBarConfigure {
    var isIndeterminate: Boolean
    var text: String?
    var progress: Int
    var max: Int
}

interface ModelProgressBarScope {
    suspend fun configure(block: suspend ModelProgressBarConfigure.() -> Unit)
}

suspend fun Context.withModelProgressBar(block: suspend ModelProgressBarScope.() -> Unit) {
    val view = DialogFetchStatusBinding.inflate(this.layoutInflater)
    val dialog = MaterialAlertDialogBuilder(this)
        .setCancelable(false)
        .setView(view.root)
        .show()

    val configureImpl = object : ModelProgressBarConfigure {
        override var isIndeterminate: Boolean
            get() = view.progressIndicator.isIndeterminate
            set(value) {
                view.progressIndicator.isIndeterminate = value
            }
        override var text: String?
            get() = view.text.text?.toString()
            set(value) {
                view.text.text = value
            }
        override var progress: Int
            get() = view.progressIndicator.progress
            set(value) {
                view.progressIndicator.setProgressCompat(value, true)
            }
        override var max: Int
            get() = view.progressIndicator.max
            set(value) {
                view.progressIndicator.max = value
            }

    }

    val scopeImpl = object : ModelProgressBarScope {
        override suspend fun configure(block: suspend ModelProgressBarConfigure.() -> Unit) {
            withContext(Dispatchers.Main) {
                configureImpl.block()
            }
        }
    }

    try {
        scopeImpl.block()
    } finally {
        dialog.dismiss()
    }
}