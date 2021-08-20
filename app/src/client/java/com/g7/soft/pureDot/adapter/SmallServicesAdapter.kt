package com.g7.soft.pureDot.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.databinding.ItemSmallServiceBinding
import com.g7.soft.pureDot.model.ServiceModel


class SmallServicesAdapter(private val fragment: Fragment) :
    ListAdapter<ServiceModel, SmallServicesAdapter.ViewHolder>(SmallServicesDiffCallback()) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.from(viewGroup)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position), fragment)


    class ViewHolder private constructor(private val binding: ItemSmallServiceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            dataModel: ServiceModel?,
            fragment: Fragment,
        ) {
            binding.dataModel = dataModel
            binding.executePendingBindings()

            val detailsOnClick = View.OnClickListener {
                val bundle = bundleOf("service" to dataModel)
                fragment.findNavController().navigate(R.id.serviceFragment, bundle)
            }
            binding.detailsBtn.setOnClickListener(detailsOnClick)
            binding.root.setOnClickListener(detailsOnClick)

            binding.buyNowBtn.setOnClickListener {

            }
        }

        companion object {
            internal fun from(viewGroup: ViewGroup) = ViewHolder(
                ItemSmallServiceBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )
        }
    }


}

class SmallServicesDiffCallback : DiffUtil.ItemCallback<ServiceModel>() {
    override fun areItemsTheSame(
        oldItem: ServiceModel,
        newItem: ServiceModel,
    ): Boolean = oldItem == newItem

    override fun areContentsTheSame(
        oldItem: ServiceModel,
        newItem: ServiceModel,
    ): Boolean = oldItem == newItem
}