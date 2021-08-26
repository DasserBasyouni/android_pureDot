package com.g7.soft.pureDot.ui.screen.productCheckout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.adapter.ProductCartReviewHeaderAdapter
import com.g7.soft.pureDot.databinding.FragmentCheckout2Binding
import com.kofigyan.stateprogressbar.StateProgressBar


class ProductCheckoutShippingFragment(private val viewModel: ProductCheckoutViewModel) : Fragment() {

    private lateinit var binding: FragmentCheckout2Binding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_checkout_2, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // setup adapter
        ProductCartReviewHeaderAdapter(this, viewModel.storesProductsCartDetails).let { adapter ->
            binding.cartReviewItemsRv.adapter = adapter
        }

        // setup listeners
        binding.nextBtn.setOnClickListener {
            viewModel.currentStateNumber.value = StateProgressBar.StateNumber.THREE
        }
    }

}