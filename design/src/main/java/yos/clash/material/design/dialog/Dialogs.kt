package yos.clash.material.design.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import yos.clash.material.common.compat.isAllowForceDarkCompat
import yos.clash.material.common.compat.isSystemBarsTranslucentCompat
import yos.clash.material.design.R
import yos.clash.material.design.ui.Insets
import yos.clash.material.design.ui.Surface
import yos.clash.material.design.util.getPixels
import yos.clash.material.design.util.resolveThemedResourceId
import yos.clash.material.design.util.setOnInsertsChangedListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class AppBottomSheetDialog(context: Context) : BottomSheetDialog(context) {
    private var insets: Insets = Insets.EMPTY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setCancelable(true)

        window!!.apply {
            isSystemBarsTranslucentCompat = true
            isAllowForceDarkCompat = false
        }

        findViewById<ViewGroup>(com.google.android.material.R.id.container)?.apply {
            fitsSystemWindows = false
        }

        findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)?.apply {
            setOnInsertsChangedListener {
                if (insets != it) {
                    insets = it

                    (layoutParams as CoordinatorLayout.LayoutParams).also { params ->
                        if (ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_LTR) {
                            params.setMargins(it.start, 0, it.end, 0)
                        } else {
                            params.setMargins(it.end, 0, it.start, 0)
                        }

                        val top = context.getPixels(R.dimen.bottom_sheet_background_padding_top)
                        val height = context.getPixels(R.dimen.bottom_sheet_header_height)

                        setPaddingRelative(
                            0,
                            top * 2 + height,
                            0,
                            it.bottom
                        )
                    }
                }
            }
        }

        setOnShowListener {
            behavior.halfExpandedRatio = 0.99f
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }
}

class FullScreenDialog(
    context: Context
) : Dialog(context, context.resolveThemedResourceId(R.attr.fullScreenDialogTheme)) {
    val surface = Surface()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window!!.apply {
            isSystemBarsTranslucentCompat = true

            setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )

            decorView.setOnInsertsChangedListener {
                if (surface.insets != it)
                    surface.insets = it
            }
        }
    }
}