package com.g7.soft.pureDot.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.databinding.ItemWorkingHourHeaderBinding
import com.g7.soft.pureDot.model.WorkingDayModel
import com.g7.soft.pureDot.ui.DividerItemDecorator
import com.g7.soft.pureDot.ui.screen.workingHours.WorkingHoursFragment


class WorkingHoursHeaderAdapter(
    private val fragment: WorkingHoursFragment,
    private val workingDays: List<WorkingDayModel>?
) :
    RecyclerView.Adapter<WorkingHoursHeaderAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.from(viewGroup)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(workingDays?.get(position), fragment, this)

    override fun getItemCount(): Int = workingDays?.size ?: 0


    fun getWorkingDays(llm: LinearLayoutManager): List<WorkingDayModel>? {
        workingDays?.forEachIndexed { index, _ ->
            val view = llm.findViewByPosition(index)
            val innerRv = view?.findViewById<RecyclerView>(R.id.timesRv)

            workingDays[index].times = (innerRv?.adapter as WorkingHoursInnerAdapter)
                .getWorkingHours(innerRv.layoutManager as LinearLayoutManager)
        }
        return workingDays
    }


    class ViewHolder private constructor(private val binding: ItemWorkingHourHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            dataModel: WorkingDayModel?,
            fragment: WorkingHoursFragment,
            adapter: WorkingHoursHeaderAdapter,
        ) {
            binding.isExpanded = fragment.viewModel.expandedIndex.value == adapterPosition
            binding.dataModel = dataModel
            binding.adapterPosition = adapterPosition
            binding.executePendingBindings()

            // setup recyclerView
            val timesAdapter = WorkingHoursInnerAdapter(fragment, dataModel?.times)
            binding.timesRv.adapter = timesAdapter

            // add decoration divider
            binding.timesRv.addItemDecoration(
                DividerItemDecorator(
                    ContextCompat.getDrawable(
                        fragment.requireContext(),
                        R.drawable.reviews_divider_layer
                    )!!
                )
            )

            // setup observables
            dataModel?.isEnableLiveData?.observe(fragment.viewLifecycleOwner, {
                dataModel.isEnabled = it
            })

            // setup onClick
            binding.root.setOnClickListener {
                val expandedIndex = fragment.viewModel.expandedIndex.value

                if (expandedIndex != adapterPosition)
                    fragment.viewModel.expandedIndex.value = adapterPosition
                else
                    fragment.viewModel.expandedIndex.value = null

                if (expandedIndex != null)
                adapter.notifyItemChanged(expandedIndex)
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