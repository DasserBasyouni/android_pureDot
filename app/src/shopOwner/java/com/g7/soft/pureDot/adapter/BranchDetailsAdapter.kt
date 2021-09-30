package com.g7.soft.pureDot.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.databinding.ItemProfileBinding
import com.g7.soft.pureDot.model.UserDataModel


class BranchDetailsAdapter(private val fragment: Fragment, private val userData: UserDataModel?) :
    RecyclerView.Adapter<BranchDetailsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.from(viewGroup)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getDataList(position), fragment)

    override fun getItemCount(): Int = 4


    private fun getDataList(position: Int): Pair<Int, String?> = listOf(
        Pair(R.string.branch_name, userData?.branchName),
        Pair(R.string.branch_country, userData?.branchCountryName),
        Pair(R.string.branch_city, userData?.branchCityName),
        Pair(R.string.branch_category, userData?.branchCategoryName),
    )[position]


    class ViewHolder private constructor(private val binding: ItemProfileBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            dataModel: Pair<Int, String?>,
            fragment: Fragment,
        ) {
            binding.title = fragment.getString(dataModel.first)
            binding.value = dataModel.second
            binding.executePendingBindings()
        }

        companion object {
            internal fun from(viewGroup: ViewGroup) = ViewHolder(
                ItemProfileBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )
        }
    }


}
