package com.g7.soft.pureDot.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.g7.soft.pureDot.ui.screen.checkout.*


class CheckoutAdapter(private val fragment: CheckoutFragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = if (fragment.viewModel.isProductCheckout) 4 else 3

    override fun createFragment(position: Int): Fragment {

        return if (fragment.viewModel.isProductCheckout)
            when (position) {
                1 -> CheckoutShippingFragment(fragment.viewModel)
                2 -> CheckoutPaymentFragment(fragment.viewModel)
                3 -> CheckoutConfirmationFragment(fragment.viewModel)
                else -> CheckoutDetailsFragment(fragment.viewModel)
            }
        else
            when (position) {
                1 -> CheckoutPaymentFragment(fragment.viewModel)
                2 -> CheckoutConfirmationFragment(fragment.viewModel)
                else -> CheckoutDetailsFragment(fragment.viewModel)
            }
    }
}