package yos.clash.material.design.util

import android.app.Activity
import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import yos.clash.material.design.R
import yos.clash.material.design.view.ActivityBarLayout

fun ActivityBarLayout.applyFrom(context: Context) {
    if (context is Activity) {
        findViewById<ImageView>(R.id.activity_bar_close_view)?.apply {
            setOnClickListener {
                context.onBackPressed()
            }
        }
        findViewById<TextView>(R.id.activity_bar_title_view)?.apply {
            text = context.title
        }
    }
}