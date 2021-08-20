package com.g7.soft.pureDot.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.g7.soft.pureDot.databinding.ItemServiceVariationBinding
import com.g7.soft.pureDot.model.ServiceVariationModel


class ServiceVariantsAdapter(
    private val serviceVariations: List<ServiceVariationModel>?
) :
    RecyclerView.Adapter<ServiceVariantsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.from(viewGroup)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(serviceVariations?.get(position))

    override fun getItemCount(): Int = serviceVariations?.size ?: 0


    class ViewHolder private constructor(private val binding: ItemServiceVariationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            dataModel: ServiceVariationModel?
        ) {
            val context = binding.root.context

            val modelsList = dataModel?.values
            val dataList = modelsList?.mapNotNull { it.value }?.toTypedArray()
            val spinnerData = arrayListOf(dataModel?.title).apply {
                this.addAll((dataList ?: arrayOf()))
            }


            ArrayAdapter(
                context,
                android.R.layout.simple_spinner_item,
                spinnerData
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinner.adapter = adapter
                //spinner.setSelection(viewModel.selectedBranchPosition.value!!)
            }

            binding.dataModel = dataModel
            binding.executePendingBindings()
        }


        companion object {
            internal fun from(viewGroup: ViewGroup) = ViewHolder(
                ItemServiceVariationBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )
        }
    }
}