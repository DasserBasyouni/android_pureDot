package com.g7.soft.pureDot.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.databinding.ItemSummaryBinding
import com.g7.soft.pureDot.repo.UserRepository
import com.g7.soft.pureDot.ui.screen.myWallet.MyWalletViewModel
import kotlinx.coroutines.launch


class SummaryAdapter(
    private val viewModel: MyWalletViewModel,
    private val fragment: Fragment
) :
    RecyclerView.Adapter<SummaryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.from(viewGroup)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(dataList[position], viewModel, fragment)

    override fun getItemCount(): Int = 4

    val dataList = listOf(
        Pair(R.drawable.ic_wallet_total_earnings, R.string.total_earnings),
        Pair(R.drawable.ic_wallet_total_deliveries, R.string.total_deliveries),
        Pair(R.drawable.ic_wallet_current_wallet, R.string.current_balance),
        Pair(R.drawable.ic_wallet_total_transfers, R.string.total_transfers),
    )

    class ViewHolder private constructor(private val binding: ItemSummaryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            dataModel: Pair<Int, Int>,
            viewModel: MyWalletViewModel,
            fragment: Fragment,
        ) {
            fragment.lifecycleScope.launch {
                val currencySymbol = UserRepository("").getCurrencySymbol(fragment.requireContext())
                val context = binding.root.context

                binding.iconResId = dataModel.first
                binding.title = context.getString(dataModel.second)
                binding.value = when (adapterPosition) {
                    0 -> context.getString(
                        R.string.format_price_with_currency,
                        viewModel.walletResponse.value?.data?.totalEarning,
                        currencySymbol
                    )
                    1 -> viewModel.walletResponse.value?.data?.totalDeliveries.toString()
                    2 -> context.getString(
                        R.string.format_price_with_currency,
                        viewModel.walletResponse.value?.data?.currentWallet,
                        currencySymbol
                    )
                    3 -> context.getString(
                        R.string.format_price_with_currency,
                        viewModel.walletResponse.value?.data?.totalTransfers,
                        currencySymbol
                    )
                    else -> null
                }
                binding.executePendingBindings()
            }
        }

        companion object {
            internal fun from(viewGroup: ViewGroup) = ViewHolder(
                ItemSummaryBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )
        }
    }


}
