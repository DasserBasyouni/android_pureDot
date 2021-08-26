package com.g7.soft.pureDot.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.g7.soft.pureDot.databinding.ItemCartHeaderBinding
import com.g7.soft.pureDot.model.StoreProductsCartDetailsModel


class CartHeaderAdapter(
    private val fragment: Fragment
) :
    ListAdapter<StoreProductsCartDetailsModel, CartHeaderAdapter.ViewHolder>(FitnessTrainingClassesDiffCallback()) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.from(viewGroup)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position), fragment)


    class ViewHolder private constructor(private val binding: ItemCartHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            dataModel: StoreProductsCartDetailsModel,
            fragment: Fragment,
        ) {
            //val brandItems = items.filter { item -> item.shop?.name == storeName } // todo tell api that store name must be unique
            binding.storeName = dataModel.products?.firstOrNull()?.shop?.name
            binding.subTotal = dataModel.subTotal
            binding.vat = dataModel.vat
            binding.currency = dataModel.currency
            binding.executePendingBindings()

            binding.recyclerView.adapter =
                CartInnerAdapter(fragment).also { it.submitList(dataModel.products) }
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

class FitnessTrainingClassesDiffCallback : DiffUtil.ItemCallback<StoreProductsCartDetailsModel>() {
    override fun areItemsTheSame(
        oldItem: StoreProductsCartDetailsModel,
        newItem: StoreProductsCartDetailsModel,
    ): Boolean = oldItem == newItem

    override fun areContentsTheSame(
        oldItem: StoreProductsCartDetailsModel,
        newItem: StoreProductsCartDetailsModel,
    ): Boolean = oldItem == newItem
}