package yos.clash.material.design.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import yos.clash.material.design.databinding.AdapterProfileProviderBinding
import yos.clash.material.design.model.ProfileProvider
import yos.clash.material.design.util.layoutInflater

class ProfileProviderAdapter(
    private val context: Context,
    private val select: (ProfileProvider) -> Unit,
    private val detail: (ProfileProvider) -> Boolean,
) : RecyclerView.Adapter<ProfileProviderAdapter.Holder>() {
    class Holder(val binding: AdapterProfileProviderBinding) : RecyclerView.ViewHolder(binding.root)

    var providers: List<ProfileProvider> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            AdapterProfileProviderBinding.inflate(
                context.layoutInflater,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val current = providers[position]
        val binding = holder.binding

        binding.provider = current

        binding.root.apply {
            setOnClickListener {
                select(current)
            }
            setOnLongClickListener {
                detail(current)
            }
        }
    }

    override fun getItemCount(): Int {
        return providers.size
    }
}