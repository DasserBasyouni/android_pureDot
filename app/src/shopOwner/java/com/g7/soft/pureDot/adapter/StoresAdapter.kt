package com.g7.soft.pureDot.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.databinding.ItemStoreHorizontalBinding
import com.g7.soft.pureDot.model.StoreModel


class StoresAdapter(private val fragment: Fragment) :
    ListAdapter<StoreModel, StoresAdapter.ViewHolder>(StoresDiffCallback()) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder = ViewHolder.from(viewGroup)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position), fragment)


    class ViewHolder private constructor(private val binding: ItemStoreHorizontalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            dataModel: StoreModel,
            fragment: Fragment,
        ) {
            binding.dataModel = dataModel
            binding.executePendingBindings()

            binding.root.setOnClickListener {
                val bundle = bundleOf("store" to dataModel)
                fragment.findNavController().navigate(R.id.storeFragment, bundle)
            }
        }

        companion object {
            internal fun from(viewGroup: ViewGroup) = ViewHolder(
                ItemStoreHorizontalBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )
        }
    }


}

class StoresDiffCallback : DiffUtil.ItemCallback<StoreModel>() {
    override fun areItemsTheSame(
        oldItem: StoreModel,
        newItem: StoreModel,
    ): Boolean = oldItem == newItem

    override fun areContentsTheSame(
        oldItem: StoreModel,
        newItem: StoreModel,
    ): Boolean = oldItem == newItem
}