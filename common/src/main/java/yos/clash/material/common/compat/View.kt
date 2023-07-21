@file:Suppress("DEPRECATION")

package yos.clash.material.common.compat

import android.os.Build
import android.widget.TextView
import androidx.annotation.StyleRes

var TextView.textAppearance: Int
    get() = throw UnsupportedOperationException("set value only")
    set(@StyleRes value) {
        if (Build.VERSION.SDK_INT >= 23) {
            setTextAppearance(value)
        } else {
            setTextAppearance(context, value)
        }
    }