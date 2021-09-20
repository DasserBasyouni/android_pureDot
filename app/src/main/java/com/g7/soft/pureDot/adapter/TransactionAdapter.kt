package com.g7.soft.pureDot.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.g7.soft.pureDot.databinding.ItemTransactionBinding
import com.g7.soft.pureDot.model.TransactionModel
import com.g7.soft.pureDot.repo.UserRepository
import kotlinx.coroutines.launch


class TransactionAdapter(private val fragment: Fragment) :
    PagedListAdapter<TransactionModel, TransactionAdapter.ViewHolder>(MyWalletDiffCallback()) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.from(viewGroup)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position), fragment)


    class ViewHolder private constructor(private val binding: ItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            dataModel: TransactionModel?,
            fragment: Fragment,
        ) {
            fragment.lifecycleScope.launch { // optimize this and make it sigle time call
                val currencySymbol = UserRepository("").getCurrencySymbol(fragment.requireContext())

                binding.currency = currencySymbol
                binding.dataModel = dataModel
                binding.executePendingBindings()
            }
        }

        companion object {
            internal fun from(viewGroup: ViewGroup) = ViewHolder(
                ItemTransactionBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )
        }
    }


}

class MyWalletDiffCallback : DiffUtil.ItemCallback<TransactionModel>() {
    override fun areItemsTheSame(
        oldItem: TransactionModel,
        newItem: TransactionModel,
    ): Boolean = oldItem == newItem

    override fun areContentsTheSame(
        oldItem: TransactionModel,
        newItem: TransactionModel,
    ): Boolean = oldItem == newItem
}