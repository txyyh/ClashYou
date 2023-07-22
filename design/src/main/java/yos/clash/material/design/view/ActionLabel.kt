package yos.clash.material.design.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import yos.clash.material.design.R
import yos.clash.material.design.databinding.ComponentActionLabelBinding
import yos.clash.material.design.util.layoutInflater

class ActionLabel @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0
) : FrameLayout(context, attributeSet, defStyleAttr, defStyleRes) {
    private val binding = ComponentActionLabelBinding
        .inflate(context.layoutInflater, this, true)

    var icon: Drawable?
        get() = binding.iconView.background
        set(value) {
            binding.iconView.background = value
            binding.iconView.visibility = if (value == null) View.GONE else View.VISIBLE
        }

    var text: CharSequence?
        get() = binding.textView.text
        set(value) {
            binding.textView.text = value
        }

    var subtext: CharSequence?
        get() = binding.subtextView.text
        set(value) {
            binding.subtextView.text = value
            binding.subtextView.visibility = if (value == null) View.GONE else View.VISIBLE
        }

    override fun setOnClickListener(l: OnClickListener?) {
        binding.clickAble.setOnClickListener(l)
    }

    init {
        context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.ActionLabel,
            defStyleAttr,
            defStyleRes
        ).apply {
            try {
                icon = getDrawable(R.styleable.ActionLabel_icon)
                text = getString(R.styleable.ActionLabel_text)
                subtext = getString(R.styleable.ActionLabel_subtext)
            } finally {
                recycle()
            }
        }
    }
}