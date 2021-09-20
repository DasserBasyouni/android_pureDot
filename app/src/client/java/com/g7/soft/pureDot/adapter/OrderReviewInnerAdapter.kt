package com.g7.soft.pureDot.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.databinding.ItemOrderReviewInnerBinding
import com.g7.soft.pureDot.model.MasterOrderModel
import com.g7.soft.pureDot.model.OrderModel
import com.g7.soft.pureDot.model.ProductModel
import com.g7.soft.pureDot.repo.UserRepository
import kotlinx.coroutines.launch


class OrderReviewInnerAdapter(
    private val order: OrderModel?,
    private val fragment: Fragment,
    private val masterOrder: MasterOrderModel? = null,
    private val selectedProduct: ProductModel? = null
) :
    RecyclerView.Adapter<OrderReviewInnerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.from(viewGroup)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(
            if (selectedProduct == null) order?.products?.get(position)
            else order?.products?.firstOrNull { it.id == selectedProduct.id },
            order,
            fragment,
            masterOrder
        )

    override fun getItemCount(): Int = when {
        selectedProduct == null -> order?.products?.size ?: 0
        order?.products != null -> 1
        else -> 0
    }


    class ViewHolder private constructor(private val binding: ItemOrderReviewInnerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            dataModel: ProductModel?,
            order: OrderModel?,
            fragment: Fragment,
            masterOrder: MasterOrderModel?,
        ) {
            fragment.lifecycleScope.launch { // optimize this and make it single time call
                val currencySymbol = UserRepository("").getCurrencySymbol(fragment.requireContext())

                binding.currency = currencySymbol
                binding.masterOrder = masterOrder
                binding.order = order
                binding.dataModel = dataModel
                binding.executePendingBindings()
            }

            binding.refundIv.setOnClickListener {
                val bundle = bundleOf(
                    "masterOrder" to masterOrder,
                    "selectedProduct" to dataModel
                )
                fragment.findNavController().navigate(R.id.returnFragment, bundle)
            }
        }

        companion object {
            internal fun from(viewGroup: ViewGroup) = ViewHolder(
                ItemOrderReviewInnerBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )
        }
    }
}