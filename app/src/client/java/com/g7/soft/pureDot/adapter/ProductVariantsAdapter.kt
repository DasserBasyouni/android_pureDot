package com.g7.soft.pureDot.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.g7.soft.pureDot.databinding.ItemProductVariationBinding
import com.g7.soft.pureDot.model.ProductVariationModel


class ProductVariantsAdapter(
    private val productVariations: List<ProductVariationModel>?
) :
    RecyclerView.Adapter<ProductVariantsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.from(viewGroup)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(productVariations?.get(position))

    override fun getItemCount(): Int = productVariations?.size ?: 0


    class ViewHolder private constructor(private val binding: ItemProductVariationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            dataModel: ProductVariationModel?
        ) {
            //binding.masterOrder = masterOrder
            binding.dataModel = dataModel
            binding.executePendingBindings()
        }

        companion object {
            internal fun from(viewGroup: ViewGroup) = ViewHolder(
                ItemProductVariationBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )
        }
    }
}