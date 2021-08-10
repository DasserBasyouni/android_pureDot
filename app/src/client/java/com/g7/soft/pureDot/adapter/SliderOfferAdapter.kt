package com.g7.soft.pureDot.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.g7.soft.pureDot.databinding.ItemSliderOfferBinding
import com.g7.soft.pureDot.model.SliderOfferModel
import com.smarteist.autoimageslider.SliderViewAdapter
import java.util.*

internal class SliderOfferAdapter : SliderViewAdapter<SliderOfferAdapter.ViewHolder>() {

    private var offers: List<SliderOfferModel> = ArrayList()


    override fun onCreateViewHolder(viewGroup: ViewGroup): ViewHolder = ViewHolder.from(viewGroup)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(offers[position])

    override fun getCount(): Int = offers.size


    fun updateItems(SliderOfferModels: List<SliderOfferModel>) {
        offers = SliderOfferModels
        notifyDataSetChanged()
    }


    class ViewHolder private constructor(private val binding: ItemSliderOfferBinding) : SliderViewAdapter.ViewHolder(binding.root) {
        fun bind(item: SliderOfferModel) {
            binding.imageUrl = item.imageUrl
            binding.executePendingBindings()

            binding.root.setOnClickListener {
                //findNavController(itemView).navigate(R.id.action_)
            }
        }

        companion object {
            internal fun from(viewGroup: ViewGroup) = ViewHolder(
                ItemSliderOfferBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
            )
        }
    }
}