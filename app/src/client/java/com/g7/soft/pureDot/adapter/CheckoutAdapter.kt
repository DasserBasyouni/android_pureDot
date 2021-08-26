package com.g7.soft.pureDot.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.g7.soft.pureDot.ui.screen.productCheckout.*


class CheckoutAdapter(private val fragment: ProductCheckoutFragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = if (fragment.viewModel.isProductCheckout) 4 else 3

    override fun createFragment(position: Int): Fragment {

        return if (fragment.viewModel.isProductCheckout)
            when (position) {
                1 -> ProductCheckoutShippingFragment(fragment.viewModel)
                2 -> ProductCheckoutPaymentFragment(fragment.viewModel)
                3 -> ProductCheckoutConfirmationFragment(fragment.viewModel)
                else -> ProductCheckoutDetailsFragment(fragment.viewModel)
            }
        else
            when (position) {
                1 -> ProductCheckoutPaymentFragment(fragment.viewModel)
                2 -> ProductCheckoutConfirmationFragment(fragment.viewModel)
                else -> ProductCheckoutDetailsFragment(fragment.viewModel)
            }
    }
}