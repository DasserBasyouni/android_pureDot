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
import com.g7.soft.pureDot.databinding.ItemMyOrderBinding
import com.g7.soft.pureDot.model.OrderModel


class MyOrdersAdapter(private val fragment: Fragment) :
    ListAdapter<OrderModel, MyOrdersAdapter.ViewHolder>(MyOrdersDiffCallback()) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.from(viewGroup)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position), fragment, isLastItem = position + 1 == itemCount)


    class ViewHolder private constructor(private val binding: ItemMyOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            dataModel: OrderModel,
            fragment: Fragment,
            isLastItem: Boolean,
        ) {
            binding.dataModel = dataModel
            binding.isLastItem = isLastItem
            binding.executePendingBindings()

            binding.root.setOnClickListener {
                val bundle = bundleOf("masterOrder" to dataModel)
                fragment.findNavController().navigate(R.id.orderFragment, bundle)
            }
        }

        companion object {
            internal fun from(viewGroup: ViewGroup) = ViewHolder(
                ItemMyOrderBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )

            )
        }
    }


}

class MyOrdersDiffCallback : DiffUtil.ItemCallback<OrderModel>() {
    override fun areItemsTheSame(
        oldItem: OrderModel,
        newItem: OrderModel,
    ): Boolean = oldItem == newItem

    override fun areContentsTheSame(
        oldItem: OrderModel,
        newItem: OrderModel,
    ): Boolean = oldItem == newItem
}