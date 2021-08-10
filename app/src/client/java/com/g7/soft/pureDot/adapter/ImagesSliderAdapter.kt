package com.g7.soft.pureDot.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.g7.soft.pureDot.databinding.ItemSliderOfferBinding


class ImagesSliderAdapter(private val fragment: Fragment) :
    ListAdapter<String, ImagesSliderAdapter.ViewHolder>(ImagesSliderDiffCallback()) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder = ViewHolder.from(viewGroup)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position), fragment)


    class ViewHolder private constructor(private val binding: ItemSliderOfferBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            dataModel: String,
            fragment: Fragment,
        ) {
            binding.imageUrl = dataModel
            binding.executePendingBindings()

            binding.root.setOnClickListener {
                fragment.findNavController() // todo
            }
        }

        companion object {
            internal fun from(viewGroup: ViewGroup) = ViewHolder(
                ItemSliderOfferBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )
        }
    }


}

class ImagesSliderDiffCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(
        oldItem: String,
        newItem: String,
    ): Boolean = oldItem == newItem

    override fun areContentsTheSame(
        oldItem: String,
        newItem: String,
    ): Boolean = oldItem == newItem
}