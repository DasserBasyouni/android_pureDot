package com.g7.soft.pureDot.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.g7.soft.pureDot.ui.screen.checkout.*


class CheckoutAdapter(private val fragment: CheckoutFragment, private val currencySymbol: String) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int =
        if (fragment.viewModel.masterOrder?.isService == true) 3 else 4

    override fun createFragment(position: Int): Fragment {

        return if (fragment.viewModel.masterOrder?.isService == true)
            when (position) {
                1 -> CheckoutPaymentFragment(fragment.viewModel)
                2 -> CheckoutConfirmationFragment(fragment.viewModel, currencySymbol)
                else -> CheckoutDetailsFragment(fragment.viewModel)
            }
        else
            when (position) {
                1 -> CheckoutShippingFragment(fragment.viewModel, currencySymbol)
                2 -> CheckoutPaymentFragment(fragment.viewModel)
                3 -> CheckoutConfirmationFragment(fragment.viewModel, currencySymbol)
                else -> CheckoutDetailsFragment(fragment.viewModel)
            }
    }
}