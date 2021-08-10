package com.g7.soft.pureDot.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.databinding.ItemStoreVerticalBinding
import com.g7.soft.pureDot.model.StoreModel


class PagedStoresAdapter(private val fragment: Fragment, private val isSelectable: Boolean = false) :
    PagedListAdapter<StoreModel, PagedStoresAdapter.ViewHolder>(PagedStoresDiffCallback()) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.from(viewGroup)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position), fragment, isSelectable)


    class ViewHolder private constructor(private val binding: ItemStoreVerticalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            dataModel: StoreModel?,
            fragment: Fragment,
            isSelectable: Boolean,
        ) {
            binding.dataModel = dataModel
            binding.executePendingBindings()

            binding.root.setOnClickListener {
                // todo add or get those selected
                if (isSelectable)
                    binding.root.isSelected = !binding.root.isSelected
                else {
                    val bundle = bundleOf("store" to dataModel)
                    fragment.findNavController().navigate(R.id.storeFragment, bundle)
                }
            }
        }

        companion object {
            internal fun from(viewGroup: ViewGroup) = ViewHolder(
                ItemStoreVerticalBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )
        }
    }


}

class PagedStoresDiffCallback : DiffUtil.ItemCallback<StoreModel>() {
    override fun areItemsTheSame(
        oldItem: StoreModel,
        newItem: StoreModel,
    ): Boolean = oldItem == newItem

    override fun areContentsTheSame(
        oldItem: StoreModel,
        newItem: StoreModel,
    ): Boolean = oldItem == newItem
}