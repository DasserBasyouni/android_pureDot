package com.g7.soft.pureDot.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.databinding.ItemProfileBinding
import com.g7.soft.pureDot.ext.makeLinks
import com.g7.soft.pureDot.ext.toFormattedDateTime
import com.g7.soft.pureDot.model.UserDataModel


class ProfileAdapter(private val fragment: Fragment, private val userData: UserDataModel?) :
    RecyclerView.Adapter<ProfileAdapter.ViewHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.from(viewGroup)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(
            getDataList(holder.itemView.context, position),
            fragment,
            isLastItem = position + 1 == itemCount
        )

    override fun getItemCount(): Int = 8


    private fun getDataList(context: Context, position: Int): Pair<Int, String?> = listOf(
        Pair(R.string.full_name, userData?.name),
        Pair(R.string.email, userData?.email),
        Pair(R.string.phone_number, userData?.phoneNumber),
        Pair(
            R.string.date_of_birth,
            userData?.dateOfBirth?.toFormattedDateTime(context.getString(R.string.format_standard_date))
        ),
        Pair(
            R.string.gender,
            if (userData?.isMale == true) context.getString(R.string.male)
            else context.getString(R.string.female)
        ),
        Pair(R.string.country, userData?.country?.name),
        Pair(R.string.city, userData?.city?.name),
        Pair(R.string.password, context.getString(R.string.symbol_password)),
    )[position]


    class ViewHolder private constructor(private val binding: ItemProfileBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            dataModel: Pair<Int, String?>,
            fragment: Fragment,
            isLastItem: Boolean,
        ) {
            binding.title = fragment.getString(dataModel.first)
            binding.value = dataModel.second
            binding.isPassword = isLastItem
            binding.executePendingBindings()

            if (isLastItem)
                binding.changePasswordTv.makeLinks(
                    Pair(
                        fragment.getString(R.string.change_password),
                        View.OnClickListener {
                            fragment.findNavController().navigate(R.id.changePasswordFragment)
                        }), doChangeColor = false
                )
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
