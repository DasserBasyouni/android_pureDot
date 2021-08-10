package com.g7.soft.pureDot.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.g7.soft.pureDot.databinding.ItemNotificationBinding
import com.g7.soft.pureDot.model.NotificationModel


class NotificationsAdapter(private val fragment: Fragment) :
    ListAdapter<NotificationModel, NotificationsAdapter.ViewHolder>(NotificationsDiffCallback()) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder = ViewHolder.from(viewGroup)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position), fragment)


    class ViewHolder private constructor(private val binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            dataModel: NotificationModel,
            fragment: Fragment,
        ) {
            binding.dataModel = dataModel
            binding.executePendingBindings()

            binding.root.setOnClickListener {
                /*val bundle = bundleOf("category" to dataModel)
                fragment.findNavController().navigate(R.id.categoryFragment, bundle)*/
            }
        }

        companion object {
            internal fun from(viewGroup: ViewGroup) = ViewHolder(
                ItemNotificationBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )
        }
    }


}

class NotificationsDiffCallback : DiffUtil.ItemCallback<NotificationModel>() {
    override fun areItemsTheSame(
        oldItem: NotificationModel,
        newItem: NotificationModel,
    ): Boolean = oldItem == newItem

    override fun areContentsTheSame(
        oldItem: NotificationModel,
        newItem: NotificationModel,
    ): Boolean = oldItem == newItem
}