package com.g7.soft.pureDot.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.g7.soft.pureDot.ui.screen.myWallet.MyWalletFragment
import com.g7.soft.pureDot.ui.screen.myWallet.MyWalletSummaryFragment
import com.g7.soft.pureDot.ui.screen.myWallet.MyWalletTransactionsFragment


class MyWalletPagerAdapter(private val fragment: MyWalletFragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> MyWalletTransactionsFragment(fragment.viewModel)
            else -> MyWalletSummaryFragment(fragment.viewModel)
        }
    }
}