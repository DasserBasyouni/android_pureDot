package com.g7.soft.pureDot.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.g7.soft.pureDot.databinding.ItemProductVariationBinding
import com.g7.soft.pureDot.model.ProductVariationModel
import com.g7.soft.pureDot.ui.screen.product.ProductViewModel
import com.google.android.material.chip.Chip


class ProductVariantsAdapter(
    private val productVariations: List<ProductVariationModel>?,
    private val viewModel: ProductViewModel
) :
    RecyclerView.Adapter<ProductVariantsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.from(viewGroup)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(productVariations?.get(position), viewModel)

    override fun getItemCount(): Int = productVariations?.size ?: 0


    class ViewHolder private constructor(private val binding: ItemProductVariationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            dataModel: ProductVariationModel?,
            viewModel: ProductViewModel
        ) {
            //binding.masterOrder = masterOrder
            binding.dataModel = dataModel
            binding.executePendingBindings()

            binding.chipGroup.setOnCheckedChangeListener { _, checkedId ->
                viewModel.selectedVariationsIdsMap.value =
                    viewModel.selectedVariationsIdsMap.value.also {
                        it?.put(
                            adapterPosition,
                            binding.root.findViewById<Chip>(checkedId)?.tag?.toString()
                        )
                    }


            }
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