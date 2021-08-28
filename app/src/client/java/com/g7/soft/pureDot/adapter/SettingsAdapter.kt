package com.g7.soft.pureDot.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.databinding.ItemSettingsBinding
import com.g7.soft.pureDot.ext.observeApiResponse
import com.g7.soft.pureDot.repo.ClientRepository
import com.g7.soft.pureDot.ui.screen.myAccount.MyAccountFragment
import com.g7.soft.pureDot.util.ProjectDialogUtils
import com.zeugmasolutions.localehelper.currentLocale
import kotlinx.coroutines.launch


class SettingsAdapter(private val fragment: MyAccountFragment) :
    RecyclerView.Adapter<SettingsAdapter.ViewHolder>() {

    val list = listOf(
        Pair(R.drawable.ic_settings_locale, R.string.change_language),
        Pair(R.drawable.ic_my_order, R.string.my_orders),
        Pair(R.drawable.ic_wallet, R.string.my_wallet),
        Pair(R.drawable.ic_my_favourite, R.string.my_favourite),
        Pair(R.drawable.ic_customer_support, R.string.customer_support),
        Pair(R.drawable.ic_contact_us, R.string.contact_us),
        Pair(R.drawable.ic_about_us, R.string.about_us),
        Pair(R.drawable.ic_privacy_policy, R.string.privacy_policy),
        Pair(R.drawable.ic_settings_terms_and_conditions, R.string.terms_and_conditions),
        Pair(R.drawable.ic_logout, R.string.logout),
    )


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.from(viewGroup)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(list[position], fragment)

    override fun getItemCount(): Int = list.size

    class ViewHolder private constructor(private val binding: ItemSettingsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            dataModel: Pair<Int, Int>,
            fragment: MyAccountFragment,
        ) {
            binding.iconResId = dataModel.first
            binding.settingsName = binding.root.context.getString(dataModel.second)
            binding.executePendingBindings()

            binding.root.setOnClickListener {
                when (adapterPosition) {
                    0 -> fragment.findNavController().navigate(R.id.changeLanguageFragment)
                    1 -> fragment.findNavController().navigate(R.id.myOrdersFragment)
                    2 -> fragment.findNavController().navigate(R.id.myWalletFragment)
                    3 -> fragment.findNavController().navigate(R.id.favouritesFragment)
                    4 -> fragment.findNavController().navigate(R.id.customerServiceFragment)
                    5 -> fragment.findNavController().navigate(R.id.contactUsFragment)
                    6 -> fragment.findNavController().navigate(R.id.aboutUsFragment)
                    7 -> fragment.findNavController().navigate(R.id.policyFragment)
                    8 -> fragment.findNavController().navigate(R.id.termsFragment)
                    9 -> {
                        ProjectDialogUtils.showAskingDialog(
                            fragment.requireContext(),
                            R.drawable.ic_popup_logout,
                            R.string.sign_out,
                            R.string.question_sure_logout,
                            positiveRunnable = {
                                fragment.viewModel.viewModelScope.launch {
                                    val isGuestAccount =
                                        ClientRepository("").getLocalUserData(fragment.requireContext()).isGuestAccount

                                    if (isGuestAccount)
                                        clearUserDataThenNavigate(fragment)
                                    else
                                        fragment.viewModel.logout(fragment.requireActivity().currentLocale.toLanguageTag())
                                            .observeApiResponse(fragment, {
                                                clearUserDataThenNavigate(fragment)
                                            })
                                }
                            }
                        )
                    }
                }
            }
        }


        private fun clearUserDataThenNavigate(fragment: MyAccountFragment) {
            fragment.viewModel.viewModelScope.launch {
                ClientRepository("").clearUserData(fragment.requireContext())
                fragment.findNavController()
                    .navigate(R.id.action_myAccountFragment_to_startFragment)
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
