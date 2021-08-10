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
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.databinding.ItemProductGridViewBinding
import com.g7.soft.pureDot.databinding.ItemProductLinearViewBinding
import com.g7.soft.pureDot.ext.observeApiResponse
import com.g7.soft.pureDot.model.ProductModel
import com.g7.soft.pureDot.ui.screen.home.HomeFragment
import com.zeugmasolutions.localehelper.currentLocale


class ProductsAdapter(private val fragment: Fragment, private val isGrid: Boolean = true) :
    ListAdapter<ProductModel, ProductsAdapter.ViewHolder>(LatestOffersDiffCallback()) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.from(viewGroup, isGrid)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position), fragment)


    class ViewHolder private constructor(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            dataModel: ProductModel,
            fragment: Fragment,
        ) {
            if (binding is ItemProductGridViewBinding)
                binding.dataModel = dataModel
            else if (binding is ItemProductLinearViewBinding)
                binding.dataModel = dataModel

            binding.executePendingBindings()

            /* old design:
            val addToCartOnClick = View.OnClickListener {
                if (fragment is HomeFragment) {
                    fragment.viewModel.editCartQuantity(
                        fragment.requireActivity().currentLocale.toLanguageTag(),
                        itemId = dataModel.id,
                        quantity = (dataModel.quantityInCart ?: 0) + 1
                    ).observeApiResponse(fragment, {
                        dataModel.quantityInCart = (dataModel.quantityInCart ?: 0) + 1

                        if (binding is ItemProductGridViewBinding)
                            binding.dataModel = dataModel
                        else if (binding is ItemProductLinearViewBinding)
                            binding.dataModel = dataModel

                        binding.executePendingBindings()
                    })
                }
            }
            binding.root.findViewById<View>(R.id.addToCartBtn).setOnClickListener(addToCartOnClick)
            binding.root.findViewById<View>(R.id.cartControlLayout)
                .findViewById<View>(R.id.increaseCartQuantityBtn)
                .setOnClickListener(addToCartOnClick)

            binding.root.findViewById<View>(R.id.cartControlLayout)
                .findViewById<View>(R.id.decreaseCartQuantityBtn).setOnClickListener {
                if (fragment is HomeFragment) {
                    fragment.viewModel.editCartQuantity(
                        fragment.requireActivity().currentLocale.toLanguageTag(),
                        itemId = dataModel.id,
                        quantity = (dataModel.quantityInCart ?: 0) - 1
                    ).observeApiResponse(fragment, {
                        dataModel.quantityInCart = (dataModel.quantityInCart ?: 0) - 1

                        if (binding is ItemProductGridViewBinding)
                            binding.dataModel = dataModel
                        else if (binding is ItemProductLinearViewBinding)
                            binding.dataModel = dataModel

                        binding.executePendingBindings()
                    })
                }
            } */

            binding.root.setOnClickListener {
                val bundle = bundleOf("item" to dataModel)
                fragment.findNavController().navigate(R.id.productFragment, bundle)
            }
        }

        companion object {
            internal fun from(viewGroup: ViewGroup, isGrid: Boolean) = ViewHolder(
                if (isGrid)
                    ItemProductGridViewBinding.inflate(
                        LayoutInflater.from(viewGroup.context),
                        viewGroup,
                        false
                    )
                else
                    ItemProductLinearViewBinding.inflate(
                        LayoutInflater.from(viewGroup.context),
                        viewGroup,
                        false
                    )
            )
        }
    }


}

class LatestOffersDiffCallback : DiffUtil.ItemCallback<ProductModel>() {
    override fun areItemsTheSame(
        oldItem: ProductModel,
        newItem: ProductModel,
    ): Boolean = oldItem == newItem

    override fun areContentsTheSame(
        oldItem: ProductModel,
        newItem: ProductModel,
    ): Boolean = oldItem == newItem
}