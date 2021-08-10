package com.g7.soft.pureDot.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.databinding.ItemSideNavItemBinding
import com.g7.soft.pureDot.ui.screen.newHome.NewHomeFragment
import com.g7.soft.pureDot.util.ProjectDialogUtils


class SideNavMenuAdapter(private val fragment: NewHomeFragment) :
    RecyclerView.Adapter<SideNavMenuAdapter.ViewHolder>() {

    var walletBalance: Double? = null
    var walletCurrency: String? = null

    val list = listOf(
        Pair(R.drawable.ic_nav_home, R.string.home), // todo
        Pair(R.drawable.ic_nav_my_order, R.string.my_orders),
        Pair(R.drawable.ic_nav_my_account, R.string.my_account),
        Pair(R.drawable.ic_nav_my_wallet, R.string.my_wallet),
        Pair(R.drawable.ic_notification, R.string.notification),
        Pair(R.drawable.ic_nav_settings, R.string.settings),
        Pair(R.drawable.ic_nav_contact_us, R.string.contact_us),
        Pair(R.drawable.ic_nav_logout, R.string.logout),
    )


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.from(viewGroup)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(list[position], fragment, walletBalance, walletCurrency)

    override fun getItemCount(): Int = list.size
    fun updateWallet(balance: Double?, currency: String?) {
        walletBalance = balance
        walletCurrency = currency
        notifyItemChanged(3) // wallet item position
    }

    class ViewHolder private constructor(private val binding: ItemSideNavItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            dataModel: Pair<Int, Int>,
            fragment: NewHomeFragment,
            walletBalance: Double?,
            walletCurrency: String?,
        ) {
            binding.showWalletCurrency =  dataModel.first == R.drawable.ic_nav_my_wallet
            binding.walletBalance = walletBalance
            binding.walletCurrency = walletCurrency
            binding.iconResId = dataModel.first
            binding.navItemName = binding.root.context.getString(dataModel.second)
            binding.executePendingBindings()

            binding.root.setOnClickListener {
                fragment.closeSideNavigationMenu(onFinishRunnable = {
                    when (adapterPosition) {
                        1 -> fragment.findNavController().navigate(R.id.myOrdersFragment)
                        2 -> fragment.findNavController().navigate(R.id.profileFragment)
                        3 -> fragment.findNavController().navigate(R.id.myWalletFragment)
                        4 -> fragment.findNavController().navigate(R.id.notificationFragment)
                        5 -> fragment.findNavController().navigate(R.id.settingsFragment)
                        6 -> fragment.findNavController().navigate(R.id.contactUsFragment)
                        7 -> {
                            ProjectDialogUtils.showAskingDialog(
                                fragment.requireContext(),
                                R.drawable.ic_popup_logout,
                                R.string.sign_out,
                                R.string.question_sure_logout,
                                positiveRunnable = {
                                    // todo clear tokenId
                                    fragment.findNavController()
                                        .navigate(R.id.action_homeFragment_to_startFragment)
                                }
                            )
                        }
                    }
                })

            }
        }

        companion object {
            internal fun from(viewGroup: ViewGroup) = ViewHolder(
                ItemSideNavItemBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )
        }
    }


}
