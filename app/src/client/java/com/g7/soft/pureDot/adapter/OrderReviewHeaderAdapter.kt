package com.g7.soft.pureDot.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.databinding.ItemOrderReviewHeaderBinding
import com.g7.soft.pureDot.model.MasterOrderModel
import com.g7.soft.pureDot.model.OrderModel
import com.g7.soft.pureDot.model.ProductModel


class OrderReviewHeaderAdapter(
    private val masterOrder: MasterOrderModel,
    private val fragment: Fragment,
    private val areTotalPricesVisible: Boolean = true,
    private val selectedProduct: ProductModel? = null
) :
    RecyclerView.Adapter<OrderReviewHeaderAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.from(viewGroup)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(
            masterOrder.orders?.get(position),
            masterOrder,
            fragment,
            areTotalPricesVisible,
            selectedProduct
        )

    override fun getItemCount(): Int = masterOrder.orders?.size ?: 0


    class ViewHolder private constructor(private val binding: ItemOrderReviewHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            dataModel: OrderModel?,
            masterOrder: MasterOrderModel,
            fragment: Fragment,
            areTotalPricesVisible: Boolean,
            selectedProduct: ProductModel?,
        ) {
            if (areTotalPricesVisible) {
                binding.order = dataModel
                binding.vat = dataModel?.vat
                binding.subTotal = dataModel?.totalCost
                binding.currency = dataModel?.currency
            }

            binding.storeName = dataModel?.shopName
            binding.executePendingBindings()

            binding.recyclerView.adapter =
                OrderReviewInnerAdapter(
                    dataModel,
                    fragment,
                    masterOrder = if (areTotalPricesVisible) masterOrder else null,
                    selectedProduct = selectedProduct
                )

            if (areTotalPricesVisible)
                binding.trackOrderBtn.setOnClickListener {
                    val bundle =
                        bundleOf("order" to dataModel, "masterOrderNumber" to masterOrder.number)
                    fragment.findNavController().navigate(R.id.trackOrderFragment, bundle)
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