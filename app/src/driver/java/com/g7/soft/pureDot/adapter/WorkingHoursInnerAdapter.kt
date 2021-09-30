package com.g7.soft.pureDot.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.databinding.ItemWorkingHourInnerBinding
import com.g7.soft.pureDot.model.WorkingHourModel
import com.g7.soft.pureDot.ui.screen.workingHours.WorkingHoursFragment
import com.google.android.material.textfield.TextInputLayout
import com.zeugmasolutions.localehelper.currentLocale
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class WorkingHoursInnerAdapter(
    private val fragment: WorkingHoursFragment,
    private var workingHours: MutableList<WorkingHourModel>? = mutableListOf()
) :
    RecyclerView.Adapter<WorkingHoursInnerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.from(viewGroup)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(workingHours?.get(position), fragment, this)

    override fun getItemCount(): Int = workingHours?.size ?: 0


    fun addNewTime() {
        workingHours = workingHours ?: mutableListOf()
        workingHours?.add(
            WorkingHourModel(
                fromTime = -(Calendar.getInstance().timeZone.rawOffset.toLong() / 1000),
                toTime = -(Calendar.getInstance().timeZone.rawOffset.toLong() / 1000) + 8 * 60 * 60,
                id = null
            )
        )
        notifyItemInserted(workingHours?.lastIndex!!)
    }

    fun deleteTime(position: Int) {
        workingHours?.removeAt(position)
        notifyItemRemoved(position)
    }

    fun getWorkingHours(llm: LinearLayoutManager): MutableList<WorkingHourModel>? {
        workingHours?.forEachIndexed { index, workingHour ->
            val view = llm.findViewByPosition(index)
            val fromLayout = view?.findViewById<View>(R.id.fromLayout)
            val toLayout = view?.findViewById<View>(R.id.toLayout)

            workingHour.fromTime = getTime(fromLayout, workingHour.fromTime)
            workingHour.toTime = getTime(toLayout, workingHour.toTime)
        }
        return workingHours
    }


    private fun getTime(view: View?, time: Long?): Long? {
        val hourEt = view?.findViewById<TextInputLayout>(R.id.hourTil)?.editText
        val minuteEt = view?.findViewById<TextInputLayout>(R.id.minuteTil)?.editText

        if (hourEt == null || minuteEt == null)
            return time ?: 0
        else {
            val hour = hourEt.text?.toString()?.toIntOrNull()
            val minutes = minuteEt.text?.toString()?.toIntOrNull()
            val timePeriod = if (view.findViewById<RadioButton>(R.id.amRb)?.isChecked == true)
                fragment.getString(R.string.am) else fragment.getString(R.string.pm)

            return if (hour == null || minutes == null)
                null
            else {
                val formatter: DateFormat =
                    SimpleDateFormat("hh-mm-a", fragment.requireActivity().currentLocale)
                val date = (formatter.parse("$hour-$minutes-$timePeriod") as Date)

                date.time.div(1000)
                    .plus(Calendar.getInstance().timeZone.rawOffset.div(1000)) // send it to API with timezone!
            }
        }
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