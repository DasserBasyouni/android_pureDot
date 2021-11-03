package com.g7.soft.pureDot.adapter

import android.util.Log
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
import com.g7.soft.pureDot.ui.screen.filter.FilterFragment


class PagedCategoriesAdapter(
    private val fragment: Fragment,
    private val isGrid: Boolean = false,
    private val isSelectable: Boolean = false,
    private val shopId: String? = null
) :
    PagedListAdapter<CategoryModel, PagedCategoriesAdapter.ViewHolder>(PagedCategoriesDiffCallback()) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.from(viewGroup, isGrid)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position), fragment, isSelectable, shopId)


    class ViewHolder private constructor(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            dataModel: CategoryModel?,
            fragment: Fragment,
            isSelectable: Boolean,
            shopId: String?,
        ) {
            if (binding is ItemCategoryListViewBinding)
                binding.dataModel = dataModel
            else if (binding is ItemCategoryGridViewBinding)
                binding.dataModel = dataModel

            binding.executePendingBindings()

            if (isSelectable && fragment is FilterFragment)
                binding.root.isSelected =
                    fragment.viewModel.selectedCategoriesIds.contains(dataModel?.id)

            binding.root.setOnClickListener {
                if (isSelectable) {
                    binding.root.isSelected = !binding.root.isSelected
                    if (fragment is FilterFragment) {
                        val categoryId = dataModel?.id
                        categoryId?.let {
                            if (binding.root.isSelected)
                                fragment.viewModel.selectedCategoriesIds.add(dataModel.id)
                            else
                                fragment.viewModel.selectedCategoriesIds.remove(dataModel.id)
                        }
                    }
                } else {
                    Log.e("ZZ_", "shopId: $shopId")
                    val bundle = bundleOf("category" to dataModel, "shopId" to shopId)
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