package com.g7.soft.pureDot.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.g7.soft.pureDot.databinding.ItemReviewBinding
import com.g7.soft.pureDot.model.ReviewModel


class PagedReviewsAdapter(private val fragment: Fragment) :
    PagedListAdapter<ReviewModel, PagedReviewsAdapter.ViewHolder>(PagedReviewsDiffCallback()) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.from(viewGroup)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position), fragment)


    class ViewHolder private constructor(private val binding: ItemReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            dataModel: ReviewModel?,
            fragment: Fragment,
        ) {
            binding.dataModel = dataModel
            binding.executePendingBindings()

            /*val buttonsClickListener = View.OnClickListener {
                ProductRepository(fragment.requireActivity().currentLocale.toLanguageTag()).markReview(
                    tokenId = "", // todo
                    reviewId = dataModel?.id,
                    isHelpful = it.id == R.id.yesBtn
                ).observeApiResponse(fragment,{
                    dataModel?.isMarked = true
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

class PagedReviewsDiffCallback : DiffUtil.ItemCallback<ReviewModel>() {
    override fun areItemsTheSame(
        oldItem: ReviewModel,
        newItem: ReviewModel,
    ): Boolean = oldItem == newItem

    override fun areContentsTheSame(
        oldItem: ReviewModel,
        newItem: ReviewModel,
    ): Boolean = oldItem == newItem
}