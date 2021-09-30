package com.g7.soft.pureDot.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.g7.soft.pureDot.constant.ApiConstant
import com.g7.soft.pureDot.databinding.ItemOrderBinding
import com.g7.soft.pureDot.ext.observeApiResponse
import com.g7.soft.pureDot.model.MasterOrderModel
import com.g7.soft.pureDot.repo.UserRepository
import com.g7.soft.pureDot.ui.screen.home.HomeFragment
import com.zeugmasolutions.localehelper.currentLocale
import kotlinx.coroutines.launch


class PendingOrdersAdapter(private val fragment: HomeFragment) :
    PagedListAdapter<MasterOrderModel, PendingOrdersAdapter.ViewHolder>(OrdersDiffCallback()) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.from(viewGroup)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position), fragment, isLastItem = position + 1 == itemCount)

    /*// a fix @link: https://stackoverflow.com/a/50062174/5873832 todo apply to all pureDot & liveCoach?
    override fun submitList(list: MutableList<OrderModel>?) =
        super.submitList(list?.let { ArrayList(it) })*/


    class ViewHolder private constructor(private val binding: ItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            dataModel: MasterOrderModel?,
            fragment: HomeFragment,
            isLastItem: Boolean,
        ) {
            fragment.lifecycleScope.launch {
                val currencySymbol = UserRepository("").getCurrencySymbol(fragment.requireContext())

                binding.currency = currencySymbol
                binding.dataModel = dataModel
                binding.isLastItem = isLastItem
                binding.executePendingBindings()
            }

            binding.root.setOnClickListener {
                fragment.viewModel.selectedOrder.value = dataModel
            }

            binding.acceptBtn.setOnClickListener {
                fragment.lifecycleScope.launch {
                    val tokenId =
                        UserRepository("").getTokenId(fragment.requireContext())

                    fragment.viewModel.changeOrderStatus(
                        langTag = fragment.requireActivity().currentLocale.toLanguageTag(),
                        tokenId = tokenId,
                        orderId = dataModel?.firstOrder?.id,
                        status = ApiConstant.OrderDeliveryStatus.ACCEPTED.value,
                        isReturn = dataModel?.firstOrder?.isReturn
                    ).observeApiResponse(fragment, {
                        fragment.viewModel.selectedOrder.value = dataModel
                        fragment.binding.positiveBtn.performClick()
                        fragment.viewModel.ordersPagedList?.value?.dataSource?.invalidate()
                    })
                }
            }
            binding.rejectBtn.setOnClickListener {
                fragment.lifecycleScope.launch {
                    val tokenId =
                        UserRepository("").getTokenId(fragment.requireContext())

                    fragment.viewModel.changeOrderStatus(
                        langTag = fragment.requireActivity().currentLocale.toLanguageTag(),
                        tokenId = tokenId,
                        orderId = dataModel?.firstOrder?.id,
                        status = ApiConstant.OrderDeliveryStatus.REJECTED.value,
                        isReturn = dataModel?.firstOrder?.isReturn
                    ).observeApiResponse(fragment, {
                        fragment.viewModel.ordersPagedList?.value?.dataSource?.invalidate()
                    })
                }
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