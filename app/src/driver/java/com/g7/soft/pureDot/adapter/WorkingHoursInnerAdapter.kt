package com.g7.soft.pureDot.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.g7.soft.pureDot.databinding.ItemWorkingHourInnerBinding
import com.g7.soft.pureDot.model.WorkingHourModel
import com.g7.soft.pureDot.ui.screen.workingHours.WorkingHoursFragment


class WorkingHoursInnerAdapter(
    private val fragment: WorkingHoursFragment,
    private var times: MutableList<WorkingHourModel>? = mutableListOf()
) :
    RecyclerView.Adapter<WorkingHoursInnerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.from(viewGroup)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(times?.get(position), fragment, this)

    override fun getItemCount(): Int = times?.size ?: 0


    fun addNewTime() {
        times = times ?: mutableListOf()
        times?.add(WorkingHourModel(fromTime = 0, toTime = 0, id = null))
        notifyDataSetChanged()
    }

    fun deleteTime(position: Int) {
        times?.removeAt(position)
        notifyDataSetChanged()
    }


    class ViewHolder private constructor(private val binding: ItemWorkingHourInnerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            dataModel: WorkingHourModel?,
            fragment: WorkingHoursFragment,
            adapter: WorkingHoursInnerAdapter,
        ) {
            binding.dataModel = dataModel
            binding.adapterPosition = adapterPosition
            binding.executePendingBindings()

            // setup onClick
            binding.deleteIv.setOnClickListener { adapter.deleteTime(adapterPosition) }
        }


        companion object {
            internal fun from(viewGroup: ViewGroup) = ViewHolder(
                ItemWorkingHourInnerBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )

            )
        }
    }


}