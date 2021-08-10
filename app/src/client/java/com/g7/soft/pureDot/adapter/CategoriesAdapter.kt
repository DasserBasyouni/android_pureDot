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
import com.g7.soft.pureDot.databinding.ItemCategoryListViewBinding
import com.g7.soft.pureDot.model.CategoryModel


class CategoriesAdapter(private val fragment: Fragment) :
    ListAdapter<CategoryModel, CategoriesAdapter.ViewHolder>(CategoriesDiffCallback()) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder = ViewHolder.from(viewGroup)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position), fragment)


    class ViewHolder private constructor(private val binding: ItemCategoryListViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            dataModel: CategoryModel,
            fragment: Fragment,
        ) {
            binding.dataModel = dataModel
            binding.executePendingBindings()

            binding.root.setOnClickListener {
                val bundle = bundleOf("category" to dataModel)
                fragment.findNavController().navigate(R.id.categoryFragment, bundle)
            }
        }

        companion object {
            internal fun from(viewGroup: ViewGroup) = ViewHolder(
                ItemCategoryListViewBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )
        }
    }


}

class CategoriesDiffCallback : DiffUtil.ItemCallback<CategoryModel>() {
    override fun areItemsTheSame(
        oldItem: CategoryModel,
        newItem: CategoryModel,
    ): Boolean = oldItem == newItem

    override fun areContentsTheSame(
        oldItem: CategoryModel,
        newItem: CategoryModel,
    ): Boolean = oldItem == newItem
}