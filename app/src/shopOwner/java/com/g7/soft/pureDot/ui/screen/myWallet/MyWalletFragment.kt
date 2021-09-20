package com.g7.soft.pureDot.ui.screen.myWallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagedList
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.adapter.TransactionAdapter
import com.g7.soft.pureDot.constant.ProjectConstant
import com.g7.soft.pureDot.data.PaginationDataSource
import com.g7.soft.pureDot.databinding.FragmentMyWalletBinding
import com.g7.soft.pureDot.model.TransactionModel
import com.g7.soft.pureDot.repo.UserRepository
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

        // setup pagination
        viewModel.transactionPagedList =
            PaginationDataSource.initializedPagedListBuilder<TransactionModel>(
                config = PagedList.Config.Builder()
                    .setPageSize(ProjectConstant.ITEMS_PER_PAGE)
                    .setEnablePlaceholders(true)
                    .build(),
                fragment = this@MyWalletFragment,
            ).build()

        // fetch data
        lifecycleScope.launch {
            val tokenId = UserRepository("").getTokenId(requireContext())
            viewModel.getWalletData(requireActivity().currentLocale.toLanguageTag(), tokenId)
        }

        // setup observers
        viewModel.walletResponse.observe(viewLifecycleOwner, {
            viewModel.walletLcee.value!!.response.value = it
        })
        val transitionAdapter = TransactionAdapter(this)
        binding.transactionRv.adapter = transitionAdapter
        viewModel.transactionPagedList?.observe(viewLifecycleOwner, {
            transitionAdapter.submitList(it)
        })
    }

}