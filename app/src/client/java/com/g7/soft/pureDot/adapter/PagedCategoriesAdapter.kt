package com.g7.soft.pureDot.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.databinding.ItemCategoryGridViewBinding
import com.g7.soft.pureDot.databinding.ItemCategoryListViewBinding
import com.g7.soft.pureDot.model.CategoryModel


class PagedCategoriesAdapter(
    private val fragment: Fragment,
    private val isGrid: Boolean = false,
    private val isSelectable: Boolean  = false
) :
    PagedListAdapter<CategoryModel, PagedCategoriesAdapter.ViewHolder>(PagedCategoriesDiffCallback()) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.from(viewGroup, isGrid)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position), fragment, isSelectable)


    class ViewHolder private constructor(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            dataModel: CategoryModel?,
            fragment: Fragment,
            isSelectable: Boolean,
        ) {
            if (binding is ItemCategoryListViewBinding)
                binding.dataModel = dataModel
            else if (binding is ItemCategoryGridViewBinding)
                binding.dataModel = dataModel

            binding.executePendingBindings()


            binding.root.setOnClickListener {
                // todo add or get those selected
                if (isSelectable)
                    binding.root.isSelected = !binding.root.isSelected
                else {
                    val bundle = bundleOf("category" to dataModel)
                    fragment.findNavController().navigate(R.id.categoryFragment, bundle)
                }
            }
        }

        companion object {
            internal fun from(viewGroup: ViewGroup, isGrid: Boolean) = ViewHolder(
                if (isGrid)
                    ItemCategoryGridViewBinding.inflate(
                        LayoutInflater.from(viewGroup.context),
                        viewGroup,
                        false
                    )
                else
                    ItemCategoryListViewBinding.inflate(
                        LayoutInflater.from(viewGroup.context),
                        viewGroup,
                        false
                    )
            )
        }
    }


}

class PagedCategoriesDiffCallback : DiffUtil.ItemCallback<CategoryModel>() {
    override fun areItemsTheSame(
        oldItem: CategoryModel,
        newItem: CategoryModel,
    ): Boolean = oldItem == newItem

    override fun areContentsTheSame(
        oldItem: CategoryModel,
        newItem: CategoryModel,
    ): Boolean = oldItem == newItem
}