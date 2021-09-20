package com.g7.soft.pureDot.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.databinding.ItemCartInnerBinding
import com.g7.soft.pureDot.model.ProductModel
import com.g7.soft.pureDot.model.StoreModel
import com.g7.soft.pureDot.repo.CartRepository
import com.g7.soft.pureDot.repo.UserRepository
import com.g7.soft.pureDot.ui.screen.cart.CartFragment
import com.zeugmasolutions.localehelper.currentLocale
import kotlinx.coroutines.launch


class CartInnerAdapter(
    private val fragment: Fragment,
    private val shop: StoreModel?,
) :
    ListAdapter<ProductModel, CartInnerAdapter.ViewHolder>(FitnessTrainingClassesInnerDiffCallback()) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.from(viewGroup)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position), fragment, shop)


    class ViewHolder private constructor(private val binding: ItemCartInnerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            dataModel: ProductModel,
            fragment: Fragment,
            shop: StoreModel?,
        ) {
            fragment.lifecycleScope.launch {
                val tokenId = UserRepository("").getTokenId(fragment.requireContext())

                binding.shop = shop
                binding.dataModel = dataModel
                binding.executePendingBindings()

                // setup onClick
                val quantityOnClick: View.OnClickListener = View.OnClickListener {
                    if (fragment is CartFragment) {
                        CartRepository("").updateProductQuantity(
                            fragment.lifecycleScope,
                            context = fragment.requireContext(),
                            productId = dataModel.id,
                            quantity =
                            if (it.id == R.id.increaseCartQuantityBtn)
                                dataModel.quantity?.plus(1)
                                    ?: 0 else dataModel.quantity?.minus(1) ?: 0,
                            onComplete = {
                                fragment.viewModel.checkCartItems(
                                    fragment.requireActivity().currentLocale.toLanguageTag(),
                                    tokenId = tokenId,
                                    context = fragment.requireContext()
                                )
                            }
                        )
                    }
                }
                binding.root.findViewById<View>(R.id.cartControlLayout)
                    .findViewById<View>(R.id.increaseCartQuantityBtn)
                    .setOnClickListener(quantityOnClick)
                binding.root.findViewById<View>(R.id.cartControlLayout)
                    .findViewById<View>(R.id.decreaseCartQuantityBtn)
                    .setOnClickListener(quantityOnClick)
                binding.root.setOnClickListener {
                    val bundle = bundleOf("item" to dataModel)
                    fragment.findNavController().navigate(R.id.productFragment, bundle)
                }
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