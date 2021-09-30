package com.g7.soft.pureDot.adapter

import android.app.Dialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.databinding.ItemOrderReviewHeaderBinding
import com.g7.soft.pureDot.ext.observeApiResponse
import com.g7.soft.pureDot.model.MasterOrderModel
import com.g7.soft.pureDot.model.OrderModel
import com.g7.soft.pureDot.model.OrderReviewModel
import com.g7.soft.pureDot.model.ProductModel
import com.g7.soft.pureDot.repo.UserRepository
import com.g7.soft.pureDot.ui.screen.order.OrderFragment
import com.g7.soft.pureDot.utils.ProjectDialogUtils
import com.zeugmasolutions.localehelper.currentLocale
import kotlinx.android.synthetic.main.dialog_rating.*
import kotlinx.coroutines.launch


class OrderReviewHeaderAdapter(
    private val masterOrder: MasterOrderModel?,
    private val fragment: Fragment,
    private val areTotalPricesVisible: Boolean = true,
    private val selectedProduct: ProductModel? = null
) :
    RecyclerView.Adapter<OrderReviewHeaderAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.from(viewGroup)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(
            masterOrder?.orders?.get(position),
            masterOrder,
            fragment,
            areTotalPricesVisible,
            selectedProduct
        )

    override fun getItemCount(): Int = masterOrder?.orders?.size ?: 0


    class ViewHolder private constructor(private val binding: ItemOrderReviewHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            dataModel: OrderModel?,
            masterOrder: MasterOrderModel?,
            fragment: Fragment,
            areTotalPricesVisible: Boolean,
            selectedProduct: ProductModel?,
        ) {
            fragment.lifecycleScope.launch {
                val currencySymbol = UserRepository("").getCurrencySymbol(fragment.requireContext())

                if (areTotalPricesVisible) {
                    binding.order = dataModel
                    binding.vat = dataModel?.vat
                    binding.subTotal = dataModel?.subTotal
                    binding.currency = currencySymbol
                }

                binding.storeName = dataModel?.shop?.name
                binding.executePendingBindings()
            }

            binding.recyclerView.adapter =
                OrderReviewInnerAdapter(
                    dataModel,
                    fragment,
                    masterOrder = if (areTotalPricesVisible) masterOrder else null,
                    selectedProduct = selectedProduct
                )

            if (areTotalPricesVisible)
                binding.trackOrderBtn.setOnClickListener {
                    if (dataModel?.isDelivered == true)
                        fragment.lifecycleScope.launch {
                            val currencySymbol =
                                UserRepository("").getCurrencySymbol(fragment.requireContext())
                            var dialog: Dialog? = null

                            dialog = ProjectDialogUtils.showOrderRating(
                                fragment,
                                userReview = dataModel.userReview,
                                orderNumber = dataModel.number,
                                totalOrderCost = dataModel.totalOrderCost,
                                currencySymbol = currencySymbol,
                                onClickPositiveAction = {
                                    fragment.lifecycleScope.launch {
                                        val tokenId =
                                            UserRepository("").getTokenId(fragment.requireContext())

                                        if (fragment is OrderFragment)
                                            fragment.viewModel.rateOrder(
                                                langTag = fragment.requireActivity().currentLocale.toLanguageTag(),
                                                tokenId = tokenId,
                                                orderId = dataModel.id,
                                                orderRating = dialog!!.orderRatingRb.rating.toInt(),
                                                orderComment = dialog!!.orderCommentTil.editText?.text?.toString(),
                                                deliveryRating = dialog!!.deliveryRatingRb.rating.toInt(),
                                                deliveryComment = dialog!!.deliveryCommentTil.editText?.text?.toString()
                                            ).observeApiResponse(fragment, {
                                                fragment.viewModel.orderResponse.value?.data?.orders?.firstOrNull {
                                                    it.id == dataModel.id
                                                }?.userReview =
                                                    OrderReviewModel(
                                                        orderRating = dialog!!.orderRatingRb.rating.toInt(),
                                                        orderComment = dialog!!.orderCommentTil.editText?.text?.toString(),
                                                        deliveryRating = dialog!!.deliveryRatingRb.rating.toInt(),
                                                        deliveryComment = dialog!!.deliveryCommentTil.editText?.text?.toString()
                                                    )
                                                dialog?.dismiss()
                                                fragment.binding.invalidateAll()
                                            })
                                    }
                                }
                            )
                        }
                    else {
                        val bundle = bundleOf(
                            "orderId" to dataModel?.id,
                            "orderNumber" to dataModel?.number
                        )
                        fragment.findNavController().navigate(R.id.trackOrderFragment, bundle)
                    }
                }
        }

        companion object {
            internal fun from(viewGroup: ViewGroup) = ViewHolder(
                ItemOrderReviewHeaderBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )
        }
    }

}