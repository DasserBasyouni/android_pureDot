package com.g7.soft.pureDot.ui.screen.myWallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.adapter.MyWalletPagerAdapter
import com.g7.soft.pureDot.databinding.FragmentMyWalletBinding
import com.g7.soft.pureDot.repo.UserRepository
import com.google.android.material.tabs.TabLayoutMediator
import com.zeugmasolutions.localehelper.currentLocale
import kotlinx.coroutines.launch

class MyWalletFragment : Fragment() {
    private lateinit var binding: FragmentMyWalletBinding
    private lateinit var viewModelFactory: MyWalletViewModelFactory
    internal lateinit var viewModel: MyWalletViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_my_wallet, container, false)

        lifecycleScope.launch {
            val tokenId = UserRepository("").getTokenId(requireContext())
            val currencySymbol = UserRepository("").getCurrencySymbol(requireContext())

            viewModelFactory = MyWalletViewModelFactory(
                tokenId = tokenId,
            )
            viewModel = ViewModelProvider(
                this@MyWalletFragment,
                viewModelFactory
            ).get(MyWalletViewModel::class.java)

            binding.currency = currencySymbol
            binding.viewModel = viewModel
            binding.lifecycleOwner = this@MyWalletFragment
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // fetch data
        lifecycleScope.launch {
            val tokenId =
                UserRepository("").getTokenId(requireContext())
            viewModel.getWalletData(requireActivity().currentLocale.toLanguageTag(), tokenId)
        }

        // setup observers
        viewModel.walletResponse.observe(viewLifecycleOwner, {
            viewModel.walletLcee.value!!.response.value = it
        })

        // setup pager
        binding.viewPager.adapter = MyWalletPagerAdapter(this)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = if (position == 0)
                getString(R.string.transaction_list)
            else
                getString(R.string.summary)
        }.attach()

        // setup onClick

    }

}