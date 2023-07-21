package yos.clash.material.design.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import yos.clash.material.design.databinding.AdapterFileBinding
import yos.clash.material.design.model.File
import yos.clash.material.design.ui.ObservableCurrentTime
import yos.clash.material.design.util.layoutInflater

class FileAdapter(
    private val context: Context,
    private val open: (File) -> Unit,
    private val more: (File) -> Unit,
) : RecyclerView.Adapter<FileAdapter.Holder>() {
    class Holder(val binding: AdapterFileBinding) : RecyclerView.ViewHolder(binding.root)

    private val currentTime = ObservableCurrentTime()

    var files: List<File> = emptyList()

    fun updateElapsed() {
        currentTime.update()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            AdapterFileBinding
                .inflate(context.layoutInflater, parent, false)
                .also { it.currentTime = currentTime }
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val current = files[position]

        holder.binding.apply {
            file = current

            setOpen {
                open(current)
            }

            setMore {
                more(current)
            }
        }
    }

    override fun getItemCount(): Int {
        return files.size
    }
}