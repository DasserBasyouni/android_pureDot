package com.g7.soft.pureDot.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.g7.soft.pureDot.databinding.ItemCommentBinding
import com.g7.soft.pureDot.model.CommentModel


class CommentsAdapter(private val fragment: Fragment) :
    ListAdapter<CommentModel, CommentsAdapter.ViewHolder>(CommentsDiffCallback()) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder = ViewHolder.from(viewGroup)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position), fragment)


    class ViewHolder private constructor(private val binding: ItemCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            dataModel: CommentModel,
            fragment: Fragment,
        ) {
            binding.dataModel = dataModel
            binding.executePendingBindings()
        }

        companion object {
            internal fun from(viewGroup: ViewGroup) = ViewHolder(
                ItemCommentBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )
        }
    }


}

class CommentsDiffCallback : DiffUtil.ItemCallback<CommentModel>() {
    override fun areItemsTheSame(
        oldItem: CommentModel,
        newItem: CommentModel,
    ): Boolean = oldItem == newItem

    override fun areContentsTheSame(
        oldItem: CommentModel,
        newItem: CommentModel,
    ): Boolean = oldItem == newItem
}