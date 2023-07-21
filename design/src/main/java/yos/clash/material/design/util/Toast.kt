package yos.clash.material.design.util

import yos.clash.material.design.Design
import yos.clash.material.design.R
import yos.clash.material.design.ui.ToastDuration
import com.google.android.material.dialog.MaterialAlertDialogBuilder

suspend fun Design<*>.showExceptionToast(message: CharSequence) {
    showToast(message, ToastDuration.Long) {
        setAction(R.string.detail) {
            MaterialAlertDialogBuilder(it.context)
                .setTitle(R.string.error)
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton(R.string.ok) { _, _ -> }
                .show()
        }
    }
}

suspend fun Design<*>.showExceptionToast(exception: Exception) {
    showExceptionToast(exception.message ?: "Unknown")
}