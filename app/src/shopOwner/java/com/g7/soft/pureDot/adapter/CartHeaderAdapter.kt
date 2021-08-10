package com.g7.soft.pureDot.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.g7.soft.pureDot.databinding.ItemCartHeaderBinding
import com.g7.soft.pureDot.model.ProductModel


class CartHeaderAdapter(
    private val fragment: Fragment,
    private val items: MutableList<ProductModel>
) :
    ListAdapter<String, CartHeaderAdapter.ViewHolder>(FitnessTrainingClassesDiffCallback()) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.from(viewGroup)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position), fragment, items)


    class ViewHolder private constructor(private val binding: ItemCartHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            storeName: String,
            fragment: Fragment,
            items: MutableList<ProductModel>,
        ) {
            val brandItems = items.filter { item -> item.shop?.name == storeName } // todo tell api that store name must be unique
            binding.storeName = storeName
            binding.subTotal = brandItems.sumOf { it.quantityInCart!! * (it.priceWithDiscount ?: 0.00) }
            binding.vat = brandItems.sumOf { it.quantityInCart!! * (it.vat ?: 0.00) }
            binding.currency = items.first().currency
            binding.executePendingBindings()

            binding.recyclerView.adapter =
                CartInnerAdapter(fragment).also { it.submitList(brandItems) }
        }

        companion object {
            internal fun from(viewGroup: ViewGroup) = ViewHolder(
                ItemCartHeaderBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )
        }
    }


}

class FitnessTrainingClassesDiffCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(
        oldItem: String,
        newItem: String,
    ): Boolean = oldItem == newItem

    override fun areContentsTheSame(
        oldItem: String,
        newItem: String,
    ): Boolean = oldItem == newItem
}