package com.g7.soft.pureDot.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.adapter.StaggeredProductsAdapter.ViewHolder.Companion.LARGE_VIEW_TYPE
import com.g7.soft.pureDot.adapter.StaggeredProductsAdapter.ViewHolder.Companion.SMALL_VIEW_TYPE
import com.g7.soft.pureDot.databinding.ItemStaggeredProductLargeBinding
import com.g7.soft.pureDot.databinding.ItemStaggeredProductSmallBinding
import com.g7.soft.pureDot.ext.dpToPx
import com.g7.soft.pureDot.model.ProductModel


class StaggeredProductsAdapter(private val fragment: Fragment) :
    ListAdapter<ProductModel, StaggeredProductsAdapter.ViewHolder>(StaggeredProductsDiffCallback()) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.from(viewGroup, viewType)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position), fragment)

    override fun getItemViewType(position: Int): Int =
        if (position == 0 || position % 3 == 0) LARGE_VIEW_TYPE else SMALL_VIEW_TYPE

    class ViewHolder private constructor(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            dataModel: ProductModel,
            fragment: Fragment,
        ) {
            if (binding is ItemStaggeredProductSmallBinding) {
                binding.dataModel = dataModel

                // setup margin
                if ((adapterPosition+2) % 3 == 0) {
                    (binding.root.layoutParams as StaggeredGridLayoutManager.LayoutParams).bottomMargin =
                        8.dpToPx()
                } else if ((adapterPosition+1) % 3 == 0) {
                    (binding.root.layoutParams as StaggeredGridLayoutManager.LayoutParams).topMargin =
                        8.dpToPx()
                }
            } else if (binding is ItemStaggeredProductLargeBinding) {
                binding.dataModel = dataModel
                createLayoutParams(binding.root)
            }

            binding.executePendingBindings()

            binding.root.setOnClickListener {
                val bundle = bundleOf("item" to dataModel)
                fragment.findNavController().navigate(R.id.productFragment, bundle)
            }
        }

        private fun createLayoutParams(targetView: View): View {
            val params: StaggeredGridLayoutManager.LayoutParams = StaggeredGridLayoutManager
                .LayoutParams(
                    StaggeredGridLayoutManager.LayoutParams.WRAP_CONTENT,
                    StaggeredGridLayoutManager.LayoutParams.WRAP_CONTENT
                )
            params.isFullSpan = true
            targetView.layoutParams = params
            return targetView
        }

        companion object {
            const val SMALL_VIEW_TYPE = 0
            const val LARGE_VIEW_TYPE = 1

            internal fun from(viewGroup: ViewGroup, viewType: Int) = ViewHolder(
                if (viewType == SMALL_VIEW_TYPE)
                    ItemStaggeredProductSmallBinding.inflate(
                        LayoutInflater.from(viewGroup.context),
                        viewGroup,
                        false
                    )
                else
                    ItemStaggeredProductLargeBinding.inflate(
                        LayoutInflater.from(viewGroup.context),
                        viewGroup,
                        false
                    )
            )
        }
    }


}

class StaggeredProductsDiffCallback : DiffUtil.ItemCallback<ProductModel>() {
    override fun areItemsTheSame(
        oldItem: ProductModel,
        newItem: ProductModel,
    ): Boolean = oldItem == newItem

    override fun areContentsTheSame(
        oldItem: ProductModel,
        newItem: ProductModel,
    ): Boolean = oldItem == newItem
}