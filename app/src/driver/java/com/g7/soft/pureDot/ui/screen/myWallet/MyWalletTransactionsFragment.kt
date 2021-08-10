package com.g7.soft.pureDot.ui.screen.myWallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.paging.PagedList
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.adapter.TransactionAdapter
import com.g7.soft.pureDot.constant.ProjectConstant
import com.g7.soft.pureDot.data.PaginationDataSource
import com.g7.soft.pureDot.databinding.FragmentMyWalletRecyclerviewBinding
import com.g7.soft.pureDot.model.TransactionModel

class MyWalletTransactionsFragment(internal val viewModel: MyWalletViewModel) : Fragment() {

    private lateinit var binding: FragmentMyWalletRecyclerviewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_my_wallet_recyclerview, container, false)


        binding.viewModel = viewModel
        binding.lifecycleOwner = this

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
                fragment = this@MyWalletTransactionsFragment,
            ).build()

        // setup observers
        val transitionAdapter = TransactionAdapter(this)
        binding.transactionRv.adapter = transitionAdapter
        viewModel.transactionPagedList?.observe(viewLifecycleOwner, {
            transitionAdapter.submitList(it)
        })

    }

}