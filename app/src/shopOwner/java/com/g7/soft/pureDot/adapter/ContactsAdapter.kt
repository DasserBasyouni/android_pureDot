package com.g7.soft.pureDot.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.g7.soft.pureDot.databinding.ItemContactBinding
import com.g7.soft.pureDot.model.ContactModel


class ContactsAdapter(private val fragment: Fragment) :
    ListAdapter<ContactModel, ContactsAdapter.ViewHolder>(ContactsDiffCallback()) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder = ViewHolder.from(viewGroup)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position), fragment)

    // a fix @link: https://stackoverflow.com/a/50062174/5873832 todo apply to all pureDot & liveCoach?
    override fun submitList(list: MutableList<ContactModel>?) {
        super.submitList(list?.let { ArrayList(it) })
    }


    class ViewHolder private constructor(private val binding: ItemContactBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            dataModel: ContactModel,
            fragment: Fragment,
        ) {
            binding.dataModel = dataModel
            binding.executePendingBindings()

            binding.removeTv.setOnClickListener {
                // todo
            }
        }

        companion object {
            internal fun from(viewGroup: ViewGroup) = ViewHolder(
                ItemContactBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )
        }
    }


}

class ContactsDiffCallback : DiffUtil.ItemCallback<ContactModel>() {
    override fun areItemsTheSame(
        oldItem: ContactModel,
        newItem: ContactModel,
    ): Boolean = oldItem == newItem

    override fun areContentsTheSame(
        oldItem: ContactModel,
        newItem: ContactModel,
    ): Boolean = oldItem == newItem
}