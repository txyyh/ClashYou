package yos.clash.material.design.model

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import yos.clash.material.common.compat.getDrawableCompat
import yos.clash.material.design.R

sealed class ProfileProvider {
    class File(private val context: Context) : ProfileProvider() {
        override val name: String
            get() = context.getString(R.string.file)
        override val summary: String
            get() = context.getString(R.string.import_from_file)
        override val icon: Drawable?
            get() = context.getDrawableCompat(R.drawable.ic_baseline_attach_file)
    }

    class Url(private val context: Context) : ProfileProvider() {
        override val name: String
            get() = context.getString(R.string.url)
        override val summary: String
            get() = context.getString(R.string.import_from_url)
        override val icon: Drawable?
            get() = context.getDrawableCompat(R.drawable.ic_baseline_cloud_download)
    }

    class External(
        override val name: String,
        override val summary: String,
        override val icon: Drawable?,
        val intent: Intent,
    ) : ProfileProvider()

    abstract val name: String
    abstract val summary: String
    abstract val icon: Drawable?
}
