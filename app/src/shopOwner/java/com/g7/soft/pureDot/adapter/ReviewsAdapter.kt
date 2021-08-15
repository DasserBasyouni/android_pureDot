package com.g7.soft.pureDot.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.databinding.ItemReviewBinding
import com.g7.soft.pureDot.ext.observeApiResponse
import com.g7.soft.pureDot.model.ReviewModel
import com.g7.soft.pureDot.repo.ProductRepository
import com.zeugmasolutions.localehelper.currentLocale


class ReviewsAdapter(private val fragment: Fragment) :
    ListAdapter<ReviewModel, ReviewsAdapter.ViewHolder>(ReviewsDiffCallback()) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder = ViewHolder.from(viewGroup)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position), fragment)


    class ViewHolder private constructor(private val binding: ItemReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            dataModel: ReviewModel,
            fragment: Fragment,
        ) {
            binding.dataModel = dataModel
            binding.executePendingBindings()

            /*val buttonsClickListener = View.OnClickListener {
                ProductRepository(fragment.requireActivity().currentLocale.toLanguageTag()).markReview(
                    tokenId = "", // todo
                    reviewId = dataModel.id,
                    isHelpful = it.id == R.id.yesBtn
                ).observeApiResponse(fragment,{
                    dataModel.isMarked = true
                    binding.dataModel = dataModel
                    binding.executePendingBindings()
                })
            }
            binding.yesBtn.setOnClickListener(buttonsClickListener)
            binding.noBtn.setOnClickListener(buttonsClickListener)*/
        }

        companion object {
            internal fun from(viewGroup: ViewGroup) = ViewHolder(
                ItemReviewBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )
        }
    }


}

class ReviewsDiffCallback : DiffUtil.ItemCallback<ReviewModel>() {
    override fun areItemsTheSame(
        oldItem: ReviewModel,
        newItem: ReviewModel,
    ): Boolean = oldItem == newItem

    override fun areContentsTheSame(
        oldItem: ReviewModel,
        newItem: ReviewModel,
    ): Boolean = oldItem == newItem
}