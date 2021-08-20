package com.g7.soft.pureDot.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.databinding.ItemCartInnerBinding
import com.g7.soft.pureDot.model.ProductModel


class CartInnerAdapter(
    private val fragment: Fragment,
) :
    ListAdapter<ProductModel, CartInnerAdapter.ViewHolder>(FitnessTrainingClassesInnerDiffCallback()) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.from(viewGroup)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position), fragment, this)


    class ViewHolder private constructor(private val binding: ItemCartInnerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            dataModel: ProductModel,
            fragment: Fragment,
            adapter: CartInnerAdapter,
        ) {

            binding.dataModel = dataModel
            binding.executePendingBindings()

            // setup onClick
            binding.root.findViewById<View>(R.id.cartControlLayout)
                .findViewById<View>(R.id.increaseCartQuantityBtn)
                .setOnClickListener {
                    // todo
                    /*if (fragment is CartFragment) {
                        fragment.viewModel.editCartQuantity(
                            fragment.requireActivity().currentLocale.toLanguageTag(),
                            itemId = dataModel.id,
                            quantity = (dataModel.quantityInCart ?: 0) + 1
                        ).observeApiResponse(fragment, {
                            dataModel.quantityInCart = (dataModel.quantityInCart ?: 0) + 1
                            adapter.notifyItemChanged(adapterPosition)
                        })
                    }*/
                }

            binding.root.findViewById<View>(R.id.cartControlLayout)
                .findViewById<View>(R.id.decreaseCartQuantityBtn).setOnClickListener {
                    //todo
                    /*if (fragment is CartFragment) {
                        fragment.viewModel.editCartQuantity(
                            fragment.requireActivity().currentLocale.toLanguageTag(),
                            itemId = dataModel.id,
                            quantity = (dataModel.quantityInCart ?: 0) - 1
                        ).observeApiResponse(fragment, {
                            if (dataModel.quantityInCart == 1) {
                                fragment.viewModel.cartItemsResponse.value = fragment.viewModel.cartItemsResponse.value.also {
                                    it?.data?.products?.remove(dataModel)
                                }
                            } else {
                                dataModel.quantityInCart = (dataModel.quantityInCart ?: 1) - 1
                                adapter.notifyItemChanged(adapterPosition)
                            }
                        })
                    }*/
                }


            binding.root.setOnClickListener {
                val bundle = bundleOf("item" to dataModel)
                fragment.findNavController().navigate(R.id.productFragment, bundle)
            }
        }

        companion object {
            internal fun from(viewGroup: ViewGroup) = ViewHolder(
                ItemCartInnerBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )
        }
    }


}

class FitnessTrainingClassesInnerDiffCallback : DiffUtil.ItemCallback<ProductModel>() {
    override fun areItemsTheSame(
        oldItem: ProductModel,
        newItem: ProductModel,
    ): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: ProductModel,
        newItem: ProductModel,
    ): Boolean = oldItem == newItem
}