package com.g7.soft.pureDot.ui.screen.productCheckout

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatSpinner
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.constant.ProjectConstant
import com.g7.soft.pureDot.databinding.FragmentCheckout1Binding
import com.g7.soft.pureDot.ext.observeApiResponse
import com.g7.soft.pureDot.model.AddressModel
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.util.PermissionsHelper
import com.kofigyan.stateprogressbar.StateProgressBar
import com.zeugmasolutions.localehelper.currentLocale


class ProductCheckoutDetailsFragment(private val viewModel: ProductCheckoutViewModel) : Fragment() {

    private lateinit var binding: FragmentCheckout1Binding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_checkout_1, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // style
        binding.addNewLocation.paintFlags =
            binding.addNewLocation.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        // fetch data
        viewModel.getUserData(requireActivity().currentLocale.toLanguageTag(), "")//todo
        viewModel.getAddresses(requireActivity().currentLocale.toLanguageTag(), "") //todo

        // observables
        viewModel.userDataResponse.observe(viewLifecycleOwner, {
            viewModel.userDataLcee.value!!.response.value = it
        })
        viewModel.addressesResponse.observe(viewLifecycleOwner, {
            setupSpinner(
                binding.countriesSpinner,
                viewModel.addressesResponse.value,
                initialText = getString(R.string.select_address)
            )
        })

        // setup listeners
        binding.addNewLocation.setOnClickListener {
            PermissionsHelper.requestLocationPermission(requireContext(), grantedRunnable = {
                findNavController().navigate(R.id.addressFragment)
            })
        }
        binding.nextBtn.setOnClickListener {
            viewModel.checkout(requireActivity().currentLocale.toLanguageTag(), "") //todo
                .observeApiResponse(this, {
                    viewModel.shippingCostModel = it
                    viewModel.currentStateNumber.value = StateProgressBar.StateNumber.TWO
                })
        }
    }

    private fun setupSpinner(
        spinner: AppCompatSpinner,
        networkResponse: NetworkRequestResponse<List<AddressModel>?>?,
        initialText: String
    ) {
        val spinnerData = when (networkResponse?.status) {
            ProjectConstant.Companion.Status.IDLE -> {
                spinner.isEnabled = false
                arrayListOf(initialText)
            }
            ProjectConstant.Companion.Status.LOADING -> {
                spinner.isEnabled = false
                arrayListOf(getString(R.string.loading_))
            }
            ProjectConstant.Companion.Status.SUCCESS -> {
                val modelsList = networkResponse.data
                val dataList = modelsList?.mapNotNull { it.streetName }?.toTypedArray()

                spinner.isEnabled = true
                arrayListOf(initialText).apply {
                    this.addAll((dataList ?: arrayOf()))
                }
            }
            else -> {
                spinner.isEnabled = false
                arrayListOf(getString(R.string.something_went_wrong))
            }
        }

        ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            spinnerData
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
            spinner.setSelection(viewModel.selectedAddressPosition.value!!)
        }
    }


}