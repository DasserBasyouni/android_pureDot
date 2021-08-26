package com.g7.soft.pureDot.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.g7.soft.pureDot.databinding.ItemCartReviewHeaderBinding
import com.g7.soft.pureDot.model.StoreProductsCartDetailsModel


class ProductCartReviewHeaderAdapter(
    private val fragment: Fragment,
    private val data: MutableList<StoreProductsCartDetailsModel>?,
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
            dataModel: StoreProductsCartDetailsModel?,
            fragment: Fragment
        ) {
            binding.vat = dataModel?.vat
            binding.subTotal = dataModel?.totalCost
            binding.currency = dataModel?.currency

            binding.storeName = dataModel?.products?.firstOrNull()?.shop?.name
            binding.executePendingBindings()

            binding.recyclerView.adapter =
                ProductCartReviewInnerAdapter(
                    dataModel?.products
                )
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