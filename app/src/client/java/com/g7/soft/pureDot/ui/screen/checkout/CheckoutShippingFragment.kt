package com.g7.soft.pureDot.ui.screen.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.adapter.CartReviewHeaderAdapter
import com.g7.soft.pureDot.databinding.FragmentCheckout2Binding
import com.kofigyan.stateprogressbar.StateProgressBar
import com.zeugmasolutions.localehelper.currentLocale


class CheckoutShippingFragment(private val viewModel: CheckoutViewModel) : Fragment() {

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

        // fetch data
        viewModel.getCartItems(requireActivity().currentLocale.toLanguageTag(), "") //todo

        // observables
        viewModel.cartItemsResponse.observe(viewLifecycleOwner, {
            viewModel.cartItemsLcee.value!!.response.value = it

            CartReviewHeaderAdapter(it.data?.products ?: mutableListOf()).let { adapter ->
                binding.cartReviewItemsRv.adapter = adapter
                adapter.submitList(it.data?.products?.map { product ->
                    product.shop?.name
                }?.toSet()?.toList())
            }
        })

        // setup listeners
        binding.nextBtn.setOnClickListener {
            viewModel.currentStateNumber.value = StateProgressBar.StateNumber.THREE
        }
    }

}