package com.g7.soft.pureDot.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.g7.soft.pureDot.databinding.ItemServiceVariationBinding
import com.g7.soft.pureDot.model.ServiceVariationModel
import com.g7.soft.pureDot.ui.screen.service.ServiceViewModel


class ServiceVariantsAdapter(
    private val serviceVariations: List<ServiceVariationModel>?,
    private val viewModel: ServiceViewModel
) :
    RecyclerView.Adapter<ServiceVariantsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.from(viewGroup)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(serviceVariations?.get(position), viewModel)

    override fun getItemCount(): Int = serviceVariations?.size ?: 0


    class ViewHolder private constructor(private val binding: ItemServiceVariationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            dataModel: ServiceVariationModel?,
            viewModel: ServiceViewModel
        ) {
            val context = binding.root.context

            // setup spinner
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

            binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long
                ) {
                    viewModel.selectedVariationsMap.value =
                        viewModel.selectedVariationsMap.value.also {
                            it?.put(adapterPosition, dataModel?.values?.getOrNull(position - 1))
                        }
                }

                override fun onNothingSelected(parentView: AdapterView<*>?) = Unit
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