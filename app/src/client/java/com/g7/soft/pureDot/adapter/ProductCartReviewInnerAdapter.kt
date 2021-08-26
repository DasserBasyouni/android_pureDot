package com.g7.soft.pureDot.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.g7.soft.pureDot.databinding.ItemCartReviewInnerBinding
import com.g7.soft.pureDot.model.ProductModel


class ProductCartReviewInnerAdapter(private val data: MutableList<ProductModel>?) :
    RecyclerView.Adapter<ProductCartReviewInnerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.from(viewGroup)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(
            data?.get(position),
        )

    override fun getItemCount(): Int = data?.size ?: 0


    class ViewHolder private constructor(private val binding: ItemCartReviewInnerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            dataModel: ProductModel?
        ) {
            binding.dataModel = dataModel
            binding.executePendingBindings()
        }

        companion object {
            internal fun from(viewGroup: ViewGroup) = ViewHolder(
                ItemCartReviewInnerBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )
        }
    }
}