package yos.clash.material.design.model

import android.graphics.drawable.Drawable

data class AppInfo(
    val packageName: String,
    val label: String,
    val icon: Drawable,
    val installTime: Long,
    val updateDate: Long,
)