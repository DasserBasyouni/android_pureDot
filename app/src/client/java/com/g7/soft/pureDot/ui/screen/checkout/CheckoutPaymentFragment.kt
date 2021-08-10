package com.g7.soft.pureDot.ui.screen.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.databinding.FragmentCheckoutPaymentBinding
import com.kofigyan.stateprogressbar.StateProgressBar


class CheckoutPaymentFragment(private val viewModel: CheckoutViewModel) : Fragment() {

    private lateinit var binding: FragmentCheckoutPaymentBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                layoutInflater,
                R.layout.fragment_checkout_payment,
                container,
                false
            )

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // observe payment method
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            viewModel.isCashOnDelivery.value = checkedId == R.id.cashOnDelivery
        }

        // setup listeners
        binding.nextBtn.setOnClickListener {
            viewModel.currentStateNumber.value = StateProgressBar.StateNumber.FOUR
        }
        binding.stcPayIv.setOnClickListener {
            binding.stcPayIv.isChecked = true
            viewModel.isStcPayChecked.value = true

            // reset other methods
            binding.masterCardIv.isChecked = false
        }
        binding.masterCardIv.setOnClickListener {
            binding.masterCardIv.isChecked = true
            viewModel.isMasterCardChecked.value = true

            // reset other methods
            binding.stcPayIv.isChecked = false
        }

        // init selection
        binding.stcPayIv.isChecked = true
    }

}