package com.g7.soft.pureDot.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.databinding.ItemWorkingHourHeaderBinding
import com.g7.soft.pureDot.model.WorkingDayModel
import com.g7.soft.pureDot.model.WorkingTimesModel
import com.g7.soft.pureDot.ui.DividerItemDecorator
import com.g7.soft.pureDot.ui.screen.workingHours.WorkingHoursFragment


class WorkingHoursHeaderAdapter(
    private val fragment: WorkingHoursFragment,
    private val groupedData: WorkingTimesModel?
) :
    RecyclerView.Adapter<WorkingHoursHeaderAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.from(viewGroup)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getDayData(position), fragment, this)

    override fun getItemCount(): Int = 7

    // todo api note: ids of workingHourModel can't be repeated
    private fun getDayData(position: Int): WorkingDayModel? = when (position) {
        0 -> groupedData?.saturday
        1 -> groupedData?.sunday
        2 -> groupedData?.monday
        3 -> groupedData?.tuesday
        4 -> groupedData?.wednesday
        5 -> groupedData?.thursday
        6 -> groupedData?.friday
        else -> null
    }


    class ViewHolder private constructor(private val binding: ItemWorkingHourHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            dataModel: WorkingDayModel?,
            fragment: WorkingHoursFragment,
            adapter: WorkingHoursHeaderAdapter,
        ) {
            binding.dataModel = dataModel
            binding.adapterPosition = adapterPosition
            binding.executePendingBindings()

            // setup recyclerView
            val timesAdapter = WorkingHoursInnerAdapter(fragment, dataModel?.times)
            binding.timesRv.adapter = timesAdapter

            // add decoration divider
            binding.timesRv.addItemDecoration(
                DividerItemDecorator(
                    ContextCompat.getDrawable(fragment.requireContext(), R.drawable.reviews_divider_layer)!!
                )
            )

            // setup observables
            dataModel?.isEnableLiveData?.observe(fragment.viewLifecycleOwner, {
                dataModel.isEnabled = it
            })

            // setup onClick
            binding.root.setOnClickListener {
                fragment.viewModel.workingHoursResponse.value?.data?.setIsExpanded(adapterPosition)
                adapter.notifyItemChanged(adapterPosition)
            }
            binding.addTimeTv.setOnClickListener {
                timesAdapter.addNewTime()
            }
        }



        companion object {
            internal fun from(viewGroup: ViewGroup) = ViewHolder(
                ItemWorkingHourHeaderBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )

            )
        }
    }


}