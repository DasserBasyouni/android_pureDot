package com.g7.soft.pureDot.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.g7.soft.pureDot.databinding.ItemSliderOfferBinding
import com.g7.soft.pureDot.model.SliderOfferModel


class OffersSliderAdapter(private val fragment: Fragment) :
    ListAdapter<SliderOfferModel, OffersSliderAdapter.ViewHolder>(OffersSliderDiffCallback()) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder = ViewHolder.from(viewGroup)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position), fragment)


    class ViewHolder private constructor(private val binding: ItemSliderOfferBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            dataModel: SliderOfferModel,
            fragment: Fragment,
        ) {
            binding.imageUrl = dataModel.imageUrl
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

class OffersSliderDiffCallback : DiffUtil.ItemCallback<SliderOfferModel>() {
    override fun areItemsTheSame(
        oldItem: SliderOfferModel,
        newItem: SliderOfferModel,
    ): Boolean = oldItem == newItem

    override fun areContentsTheSame(
        oldItem: SliderOfferModel,
        newItem: SliderOfferModel,
    ): Boolean = oldItem == newItem
}