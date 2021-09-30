package com.g7.soft.pureDot.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.databinding.ItemSettingsBinding


class SettingsAdapter(private val fragment: Fragment, private val isGuestAccount: Boolean = false) :
    RecyclerView.Adapter<SettingsAdapter.ViewHolder>() {

    val list = listOf(
        Pair(R.drawable.ic_settings_locale, R.string.change_language),
        Pair(R.drawable.ic_settings_complaints, R.string.complaints),
        Pair(R.drawable.ic_settings_app_version, R.string.app_version),
        Pair(R.drawable.ic_settings_rate_app_on_store, R.string.rate_app_on_store),
        Pair(R.drawable.ic_about_us, R.string.about_us),
        Pair(R.drawable.ic_privacy_policy, R.string.privacy_policy),
        Pair(R.drawable.ic_settings_terms_and_conditions, R.string.terms_and_conditions),
    )


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.from(viewGroup)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(list[position], fragment, isGuestAccount)

    override fun getItemCount(): Int = list.size

    class ViewHolder private constructor(private val binding: ItemSettingsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            dataModel: Pair<Int, Int>,
            fragment: Fragment,
            isGuestAccount: Boolean,
        ) {
            binding.iconResId = dataModel.first
            binding.settingsName = binding.root.context.getString(dataModel.second)
            binding.executePendingBindings()

            binding.root.setOnClickListener {
                when (adapterPosition) {
                    0 -> fragment.findNavController().navigate(R.id.changeLanguageFragment)
                    1 -> fragment.findNavController().navigate(R.id.customerServiceFragment)
                    2 -> {
                        // todo nav to play store in-case there is an update
                    }
                    3 -> {
                        // todo nav to play store rating
                    }
                    4 -> fragment.findNavController().navigate(R.id.aboutUsFragment)
                    5 -> fragment.findNavController().navigate(R.id.policyFragment)
                    6 -> fragment.findNavController().navigate(R.id.termsFragment)
                }
            }
        }

        companion object {
            internal fun from(viewGroup: ViewGroup) = ViewHolder(
                ItemSettingsBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )
        }
    }


}
