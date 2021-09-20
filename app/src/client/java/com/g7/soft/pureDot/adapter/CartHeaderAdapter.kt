package com.g7.soft.pureDot.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.g7.soft.pureDot.databinding.ItemCartHeaderBinding
import com.g7.soft.pureDot.model.OrderModel
import com.g7.soft.pureDot.repo.UserRepository
import kotlinx.coroutines.launch


class CartHeaderAdapter(
    private val fragment: Fragment
) :
    ListAdapter<OrderModel, CartHeaderAdapter.ViewHolder>(FitnessTrainingClassesDiffCallback()) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.from(viewGroup)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position), fragment)


    class ViewHolder private constructor(private val binding: ItemCartHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            dataModel: OrderModel,
            fragment: Fragment,
        ) {
            fragment.lifecycleScope.launch {
                val currencySymbol = UserRepository("").getCurrencySymbol(fragment.requireContext())

                binding.storeName = dataModel.shop?.name
                binding.subTotal = dataModel.subTotal
                binding.vat = dataModel.vat
                binding.currency = currencySymbol
                binding.executePendingBindings()
            }

            binding.recyclerView.adapter =
                CartInnerAdapter(
                    fragment,
                    dataModel.shop
                ).also { it.submitList(dataModel.products) }
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

class FitnessTrainingClassesDiffCallback : DiffUtil.ItemCallback<OrderModel>() {
    override fun areItemsTheSame(
        oldItem: OrderModel,
        newItem: OrderModel,
    ): Boolean = oldItem == newItem

    override fun areContentsTheSame(
        oldItem: OrderModel,
        newItem: OrderModel,
    ): Boolean = oldItem == newItem
}