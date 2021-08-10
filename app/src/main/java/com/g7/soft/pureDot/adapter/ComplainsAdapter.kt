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
import com.g7.soft.pureDot.databinding.ItemComplainBinding
import com.g7.soft.pureDot.model.ComplainModel


class ComplainsAdapter(private val fragment: Fragment) :
    ListAdapter<ComplainModel, ComplainsAdapter.ViewHolder>(ComplainDiffCallback()) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.from(viewGroup)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position), fragment, isLastItem = position+1 == itemCount)


    class ViewHolder private constructor(private val binding: ItemComplainBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            dataModel: ComplainModel,
            fragment: Fragment,
            isLastItem: Boolean,
        ) {
            binding.dataModel = dataModel
            binding.executePendingBindings()

            binding.root.setOnClickListener {
                val bundle = bundleOf("complain" to dataModel)
                fragment.findNavController().navigate(R.id.complainFragment, bundle)
            }
        }

        companion object {
            internal fun from(viewGroup: ViewGroup) = ViewHolder(
                ItemComplainBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )

            )
        }
    }


}

class ComplainDiffCallback : DiffUtil.ItemCallback<ComplainModel>() {
    override fun areItemsTheSame(
        oldItem: ComplainModel,
        newItem: ComplainModel,
    ): Boolean = oldItem == newItem

    override fun areContentsTheSame(
        oldItem: ComplainModel,
        newItem: ComplainModel,
    ): Boolean = oldItem == newItem
}