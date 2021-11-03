package com.g7.soft.pureDot.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.g7.soft.pureDot.databinding.ItemCartReviewHeaderBinding
import com.g7.soft.pureDot.model.OrderModel
import com.g7.soft.pureDot.repo.UserRepository
import kotlinx.coroutines.launch


class ProductCartReviewHeaderAdapter(
    private val fragment: Fragment,
    private val data: MutableList<OrderModel>?,
) :
    RecyclerView.Adapter<ProductCartReviewHeaderAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.from(viewGroup)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(
            data?.get(position),
            fragment,
        )

    override fun getItemCount(): Int = data?.size ?: 0


    class ViewHolder private constructor(private val binding: ItemCartReviewHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            dataModel: OrderModel?,
            fragment: Fragment
        ) {
            fragment.lifecycleScope.launch {
                val currencySymbol = UserRepository("").getCurrencySymbol(fragment.requireContext())

                Log.e("Z_", "shippingCost: ${dataModel?.shippingCost}")
                binding.subTotal = dataModel?.subTotal
                binding.itemsVat = dataModel?.itemsVat
                binding.shippingCost = dataModel?.shippingCost
                binding.deliveryVat = dataModel?.deliveryVat
                binding.currency = currencySymbol
                binding.storeName = dataModel?.shop?.name
                binding.executePendingBindings()
            }

            binding.recyclerView.adapter =
                ProductCartReviewInnerAdapter(dataModel?.products, fragment)
        }

        companion object {
            internal fun from(viewGroup: ViewGroup) = ViewHolder(
                ItemCartReviewHeaderBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )
        }
    }

}