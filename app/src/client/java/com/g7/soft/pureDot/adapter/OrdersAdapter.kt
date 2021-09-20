package com.g7.soft.pureDot.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.databinding.ItemOrderBinding
import com.g7.soft.pureDot.model.MasterOrderModel
import com.g7.soft.pureDot.repo.UserRepository
import kotlinx.coroutines.launch


class OrdersAdapter(private val fragment: Fragment, private val isGrid: Boolean = true) :
    PagedListAdapter<MasterOrderModel, OrdersAdapter.ViewHolder>(OrdersDiffCallback()) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.from(viewGroup)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position), fragment, isLastItem = position + 1 == itemCount)


    class ViewHolder private constructor(private val binding: ItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            dataModel: MasterOrderModel?,
            fragment: Fragment,
            isLastItem: Boolean,
        ) {
            fragment.lifecycleScope.launch { // optimize this and make it sigle time call
                val currencySymbol = UserRepository("").getCurrencySymbol(fragment.requireContext())

                binding.currency = currencySymbol
                binding.dataModel = dataModel
                binding.isLastItem = isLastItem
                binding.executePendingBindings()
            }

            binding.root.setOnClickListener {
                val bundle = bundleOf("masterOrder" to dataModel)
                fragment.findNavController().navigate(R.id.orderFragment, bundle)
            }
        }

        companion object {
            internal fun from(viewGroup: ViewGroup) = ViewHolder(
                ItemOrderBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )

            )
        }
    }


}

class OrdersDiffCallback : DiffUtil.ItemCallback<MasterOrderModel>() {
    override fun areItemsTheSame(
        oldItem: MasterOrderModel,
        newItem: MasterOrderModel,
    ): Boolean = oldItem == newItem

    override fun areContentsTheSame(
        oldItem: MasterOrderModel,
        newItem: MasterOrderModel,
    ): Boolean = oldItem == newItem
}