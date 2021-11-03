package com.g7.soft.pureDot.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.constant.ApiConstant
import com.g7.soft.pureDot.databinding.ItemSliderOfferBinding
import com.g7.soft.pureDot.model.SliderOfferModel
import com.g7.soft.pureDot.utils.ProjectDialogUtils


class OffersSliderAdapter(private val fragment: Fragment) :
    ListAdapter<SliderOfferModel, OffersSliderAdapter.ViewHolder>(OffersSliderDiffCallback()) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.from(viewGroup)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position), fragment)


    class ViewHolder private constructor(private val binding: ItemSliderOfferBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            dataModel: SliderOfferModel,
            fragment: Fragment,
        ) {
            binding.imageUrl = dataModel.imageUrl
            binding.executePendingBindings()

            binding.root.setOnClickListener {
                if (dataModel.redirectId != null) {
                    if (dataModel.redirectType == ApiConstant.RedirectType.OFFER.value || dataModel.redirectType == ApiConstant.RedirectType.PRODUCT.value) {
                        val bundle = bundleOf("productId" to dataModel.redirectId)
                        fragment.findNavController().navigate(R.id.productFragment, bundle)

                    } else if (dataModel.redirectType == ApiConstant.RedirectType.SHOP.value) {
                        val bundle = bundleOf("shopId" to dataModel.redirectId)
                        fragment.findNavController().navigate(R.id.storeFragment, bundle)
                    }
                } else
                    ProjectDialogUtils.showSimpleMessage(
                        fragment.requireContext(),
                        R.string.something_went_wrong,
                        drawableResId = R.drawable.ic_cancel
                    )
            }
        }

        companion object {
            internal fun from(viewGroup: ViewGroup) = ViewHolder(
                ItemSliderOfferBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )
        }
    }


}

class OffersSliderDiffCallback : DiffUtil.ItemCallback<SliderOfferModel>() {
    override fun areItemsTheSame(
        oldItem: SliderOfferModel,
        newItem: SliderOfferModel,
    ): Boolean = oldItem == newItem

    override fun areContentsTheSame(
        oldItem: SliderOfferModel,
        newItem: SliderOfferModel,
    ): Boolean = oldItem == newItem
}