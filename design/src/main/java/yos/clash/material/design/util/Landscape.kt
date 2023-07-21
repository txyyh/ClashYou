package yos.clash.material.design.util

import android.content.Context
import yos.clash.material.design.R
import yos.clash.material.design.ui.Insets

fun Insets.landscape(context: Context): Insets {
    val displayMetrics = context.resources.displayMetrics
    val minWidth = context.getPixels(R.dimen.surface_landscape_min_width)

    val width = displayMetrics.widthPixels
    val height = displayMetrics.heightPixels

    return if (width > height && width > minWidth) {
        val expectedWidth = width.coerceAtMost(height.coerceAtLeast(minWidth))

        val padding = (width - expectedWidth).coerceAtLeast(start + end) / 2

        copy(start = padding.coerceAtLeast(start), end = padding.coerceAtLeast(end))
    } else {
        this
    }
}