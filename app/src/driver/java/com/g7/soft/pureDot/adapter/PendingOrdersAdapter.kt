package com.g7.soft.pureDot.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.g7.soft.pureDot.constant.ApiConstant
import com.g7.soft.pureDot.databinding.ItemOrderBinding
import com.g7.soft.pureDot.ext.observeApiResponse
import com.g7.soft.pureDot.model.OrderModel
import com.g7.soft.pureDot.ui.screen.home.HomeFragment
import com.zeugmasolutions.localehelper.currentLocale


class PendingOrdersAdapter(private val fragment: HomeFragment) :
    ListAdapter<OrderModel, PendingOrdersAdapter.ViewHolder>(OrdersDiffCallback()) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.from(viewGroup)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position), fragment, isLastItem = position + 1 == itemCount)

    // a fix @link: https://stackoverflow.com/a/50062174/5873832 todo apply to all pureDot & liveCoach?
    override fun submitList(list: MutableList<OrderModel>?) =
        super.submitList(list?.let { ArrayList(it) })


    class ViewHolder private constructor(private val binding: ItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            dataModel: OrderModel,
            fragment: HomeFragment,
            isLastItem: Boolean,
        ) {
            binding.dataModel = dataModel
            binding.isLastItem = isLastItem
            binding.executePendingBindings()

            binding.root.setOnClickListener {
                fragment.viewModel.selectedOrder.value = dataModel
            }

            binding.acceptBtn.setOnClickListener {
                fragment.lifecycleScope.launch {
                    val tokenId =
                        ClientRepository("").getLocalUserData(fragment.requireContext()).tokenId

                    fragment.viewModel.changeOrderStatus(
                        langTag = fragment.requireActivity().currentLocale.toLanguageTag(),
                        tokenId = tokenId,
                        orderNumber = dataModel.number,
                        status = ApiConstant.OrderDeliveryStatus.ACCEPTED.value
                    ).observeApiResponse(fragment, {
                        fragment.viewModel.selectedOrder.value = dataModel
                        fragment.binding.positiveBtn.performClick()
                    })
                }
            }
            binding.rejectBtn.setOnClickListener {
                fragment.lifecycleScope.launch {
                    val tokenId =
                        ClientRepository("").getLocalUserData(fragment.requireContext()).tokenId

                    fragment.viewModel.changeOrderStatus(
                        langTag = fragment.requireActivity().currentLocale.toLanguageTag(),
                        tokenId = tokenId,
                        orderNumber = dataModel.number,
                        status = ApiConstant.OrderDeliveryStatus.ACCEPTED.value
                    ).observeApiResponse(fragment, {
                        fragment.viewModel.ordersResponse.value =
                            fragment.viewModel.ordersResponse.value.also {
                                it?.data?.removeAt(it.data.indexOfFirst { order -> order.number == dataModel.number })
                            }
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

class OrdersDiffCallback : DiffUtil.ItemCallback<OrderModel>() {
    override fun areItemsTheSame(
        oldItem: OrderModel,
        newItem: OrderModel,
    ): Boolean = oldItem == newItem

    override fun areContentsTheSame(
        oldItem: OrderModel,
        newItem: OrderModel,
    ): Boolean = oldItem == newItem
}