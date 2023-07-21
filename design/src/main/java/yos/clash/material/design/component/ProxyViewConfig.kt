package yos.clash.material.design.component

import android.content.Context
import android.graphics.Color
import yos.clash.material.design.R
import yos.clash.material.design.util.getPixels
import yos.clash.material.design.util.resolveThemedColor
import yos.clash.material.design.util.resolveThemedResourceId

class ProxyViewConfig(val context: Context, var singleLine: Boolean) {
    private val colorSurface = context.resolveThemedColor(R.attr.colorSurface)

    val clickableBackground =
        context.resolveThemedResourceId(android.R.attr.selectableItemBackground)

    val selectedControl = context.resolveThemedColor(R.attr.colorOnPrimary)
    val selectedBackground = context.resolveThemedColor(R.attr.colorPrimary)

    val unselectedControl = context.resolveThemedColor(R.attr.colorOnSurface)
    val unselectedBackground: Int
        get() = if (singleLine) Color.TRANSPARENT else colorSurface

    val layoutPadding = context.getPixels(R.dimen.proxy_layout_padding).toFloat()
    val contentPadding = context.getPixels(R.dimen.proxy_content_padding).toFloat()
    val textMargin = context.getPixels(R.dimen.proxy_text_margin)
    val textSize = context.getPixels(R.dimen.proxy_text_size).toFloat()

    val shadow = Color.argb(
        0x15,
        Color.red(Color.DKGRAY),
        Color.green(Color.DKGRAY),
        Color.blue(Color.DKGRAY),
    )

    val cardRadius = context.getPixels(R.dimen.proxy_card_radius).toFloat()
    var cardOffset = context.getPixels(R.dimen.proxy_card_offset).toFloat()
}