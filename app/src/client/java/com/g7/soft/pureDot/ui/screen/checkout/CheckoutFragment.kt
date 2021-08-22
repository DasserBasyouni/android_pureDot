package com.g7.soft.pureDot.ui.screen.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.adapter.CheckoutAdapter
import com.g7.soft.pureDot.databinding.FragmentCheckoutBinding
import com.g7.soft.pureDot.ui.screen.MainActivity
import com.kofigyan.stateprogressbar.StateProgressBar


class CheckoutFragment : Fragment() {
    private lateinit var binding: FragmentCheckoutBinding
    private lateinit var viewModelFactory: CheckoutViewModelFactory
    internal lateinit var viewModel: CheckoutViewModel
    internal val args: CheckoutFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_checkout, container, false)

        viewModelFactory = CheckoutViewModelFactory(
            service = args.service,
            selectedVariations = args.selectedVariations,
            servantsNumber = args.servantsNumber,
            time = args.time,
            date = args.date,
            quantity = args.quantity,
        )
        viewModel = ViewModelProvider(this, viewModelFactory).get(CheckoutViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val isProductCheckout = args.service == null

        // init data
        binding.checkoutSpb.setMaxStateNumber(
            if (isProductCheckout) StateProgressBar.StateNumber.FOUR
            else StateProgressBar.StateNumber.THREE
        )
        binding.checkoutSpb.setStateDescriptionData(
            if (isProductCheckout)
                arrayListOf(
                    getString(R.string.details),
                    getString(R.string.shipping),
                    getString(R.string.payment),
                    getString(R.string.confirmation),
                )
            else
                arrayListOf(
                    getString(R.string.details),
                    getString(R.string.payment),
                    getString(R.string.confirmation),
                )
        )

        // setup observers
        viewModel.currentStateNumber.observe(viewLifecycleOwner, {
            when (it) {
                StateProgressBar.StateNumber.ONE -> binding.viewPager.currentItem = 0
                StateProgressBar.StateNumber.TWO -> binding.viewPager.currentItem = 1
                StateProgressBar.StateNumber.THREE -> binding.viewPager.currentItem = 2
                StateProgressBar.StateNumber.FOUR -> binding.viewPager.currentItem = 3
                else -> {
                }
            }
        })

        binding.viewPager.adapter = CheckoutAdapter(this)
        binding.viewPager.isUserInputEnabled = false
    }

    fun doBackPressed() {
        when (viewModel.currentStateNumber.value) {
            StateProgressBar.StateNumber.TWO -> binding.viewPager.currentItem = 0
            StateProgressBar.StateNumber.THREE -> binding.viewPager.currentItem = 1
            StateProgressBar.StateNumber.FOUR -> binding.viewPager.currentItem = 2
            else -> (requireActivity() as MainActivity).superOnBackPressed()
        }
    }

}