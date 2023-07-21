package yos.clash.material.design.preference

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import yos.clash.material.design.databinding.DialogPreferenceListBinding
import yos.clash.material.design.dialog.FullScreenDialog
import yos.clash.material.design.util.applyLinearAdapter
import yos.clash.material.design.util.layoutInflater
import yos.clash.material.design.util.root
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

internal enum class EditableListOverlayResult {
    Cancel, Apply, Reset
}

internal suspend fun requestEditableListOverlay(
    context: Context,
    adapter: RecyclerView.Adapter<*>,
    title: CharSequence,
    addNewItem: suspend () -> Unit
): EditableListOverlayResult {
    return coroutineScope {
        suspendCancellableCoroutine { ctx ->
            val dialog = FullScreenDialog(context)
            val binding = DialogPreferenceListBinding
                .inflate(context.layoutInflater, context.root, false)

            binding.surface = dialog.surface
            binding.mainList.applyLinearAdapter(context, adapter)
            binding.titleView.text = title

            binding.newView.setOnClickListener {
                launch {
                    addNewItem()
                }
            }

            binding.resetView.setOnClickListener {
                ctx.resume(EditableListOverlayResult.Reset)

                dialog.dismiss()
            }

            binding.cancelView.setOnClickListener {
                dialog.dismiss()
            }

            binding.okView.setOnClickListener {
                ctx.resume(EditableListOverlayResult.Apply)

                dialog.dismiss()
            }

            dialog.setContentView(binding.root)

            dialog.setOnDismissListener {
                if (!ctx.isCompleted) {
                    ctx.resume(EditableListOverlayResult.Cancel)
                }
            }

            ctx.invokeOnCancellation {
                dialog.dismiss()
            }

            dialog.show()
        }
    }
}