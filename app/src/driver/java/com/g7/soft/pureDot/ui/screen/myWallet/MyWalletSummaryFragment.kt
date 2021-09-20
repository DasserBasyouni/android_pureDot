package com.g7.soft.pureDot.ui.screen.myWallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.adapter.SummaryAdapter
import com.g7.soft.pureDot.databinding.FragmentMyWalletRecyclerviewBinding

class MyWalletSummaryFragment(internal val viewModel: MyWalletViewModel) : Fragment() {

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

        binding.transactionRv.adapter = SummaryAdapter(viewModel, this@MyWalletSummaryFragment)
    }

}