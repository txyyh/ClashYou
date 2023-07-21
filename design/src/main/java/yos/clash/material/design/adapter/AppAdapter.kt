package yos.clash.material.design.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import yos.clash.material.design.databinding.AdapterAppBinding
import yos.clash.material.design.model.AppInfo
import yos.clash.material.design.util.layoutInflater
import yos.clash.material.design.util.root

class AppAdapter(
    private val context: Context,
    private val selected: MutableSet<String>,
) : RecyclerView.Adapter<AppAdapter.Holder>() {
    class Holder(val binding: AdapterAppBinding) : RecyclerView.ViewHolder(binding.root)

    var apps: List<AppInfo> = emptyList()

    fun rebindAll() {
        notifyItemRangeChanged(0, itemCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            AdapterAppBinding
                .inflate(context.layoutInflater, context.root, false)
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val current = apps[position]

        holder.binding.app = current
        holder.binding.selected = current.packageName in selected
        holder.binding.root.setOnClickListener {
            if (holder.binding.selected) {
                selected.remove(current.packageName)
                holder.binding.selected = false
            } else {
                selected.add(current.packageName)
                holder.binding.selected = true
            }
        }
    }

    override fun getItemCount(): Int {
        return apps.size
    }
}