package yos.clash.material.design.util

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import yos.clash.material.design.ui.Insets

fun View.setOnInsertsChangedListener(adaptLandscape: Boolean = true, listener: (Insets) -> Unit) {
    setOnApplyWindowInsetsListener { v, ins ->
        val compat = WindowInsetsCompat.toWindowInsetsCompat(ins)
        val insets = compat.getInsets(WindowInsetsCompat.Type.systemBars())

        val rInsets = if (ViewCompat.getLayoutDirection(v) == ViewCompat.LAYOUT_DIRECTION_LTR) {
            Insets(
                insets.left,
                insets.top,
                insets.right,
                insets.bottom,
            )
        } else {
            Insets(
                insets.right,
                insets.top,
                insets.left,
                insets.bottom,
            )
        }

        listener(if (adaptLandscape) rInsets.landscape(v.context) else rInsets)

        compat.toWindowInsets()!!
    }

    requestApplyInsets()
}
