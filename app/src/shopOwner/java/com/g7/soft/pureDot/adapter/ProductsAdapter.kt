package com.g7.soft.pureDot.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.g7.soft.pureDot.databinding.ItemProductBinding
import com.g7.soft.pureDot.model.ProductModel


class ProductsAdapter(private val fragment: Fragment) :
    ListAdapter<ProductModel, ProductsAdapter.ViewHolder>(ProductsDiffCallback()) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder = ViewHolder.from(viewGroup)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position), fragment)

    // a fix @link: https://stackoverflow.com/a/50062174/5873832 todo apply to all pureDot & liveCoach?
    override fun submitList(list: MutableList<ProductModel>?) {
        super.submitList(list?.let { ArrayList(it) })
    }


    class ViewHolder private constructor(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            dataModel: ProductModel,
            fragment: Fragment,
        ) {
            binding.dataModel = dataModel
            binding.executePendingBindings()

            /*binding.removeTv.setOnClickListener {
                // todo
            }*/
        }

        companion object {
            internal fun from(viewGroup: ViewGroup) = ViewHolder(
                ItemProductBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )
        }
    }


}

class ProductsDiffCallback : DiffUtil.ItemCallback<ProductModel>() {
    override fun areItemsTheSame(
        oldItem: ProductModel,
        newItem: ProductModel,
    ): Boolean = oldItem == newItem

    override fun areContentsTheSame(
        oldItem: ProductModel,
        newItem: ProductModel,
    ): Boolean = oldItem == newItem
}