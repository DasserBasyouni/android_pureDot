package com.g7.soft.pureDot.ui.screen.submitComplain

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatSpinner
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.constant.ProjectConstant
import com.g7.soft.pureDot.databinding.FragmentSubmitComplainBinding
import com.g7.soft.pureDot.ext.observeApiResponse
import com.g7.soft.pureDot.model.CategoryModel
import com.g7.soft.pureDot.model.DataWithCountModel
import com.g7.soft.pureDot.model.OrderModel
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.zeugmasolutions.localehelper.currentLocale

class SubmitComplainFragment : Fragment() {
    private lateinit var binding: FragmentSubmitComplainBinding
    internal lateinit var viewModel: SubmitComplainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_submit_complain, container, false)

        viewModel = ViewModelProvider(this).get(SubmitComplainViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // fetch data
        val tokenId = "" // todo
        viewModel.fetchData(requireActivity().currentLocale.toLanguageTag(), tokenId = tokenId)

        viewModel.ordersResponse.observe(viewLifecycleOwner, {
            setupSpinner(
                binding.relatedOrderSpinner,
                viewModel.ordersResponse.value,
                initialText = getString(R.string.related_order)
            )
        })
        viewModel.categoriesResponse.observe(viewLifecycleOwner, {
            setupCategoriesSpinner(
                binding.categorySpinner,
                viewModel.categoriesResponse.value,
                initialText = getString(R.string.category)
            )
        })

        // setup onClick
        binding.submitBtn.setOnClickListener {
            val tokenId = "" // todo
            viewModel.submit(requireActivity().currentLocale.toLanguageTag(), tokenId = tokenId)
                .observeApiResponse(this, {
                    findNavController().popBackStack()
                })
        }
    }

    private fun setupSpinner(
        spinner: AppCompatSpinner,
        networkResponse: NetworkRequestResponse<List<OrderModel>?>?,
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
                val dataList = modelsList?.map { getString(R.string.conc_order_, it.number) }?.toTypedArray()

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
            spinner.setSelection(viewModel.ordersPosition.value!!)
        }
    }

    private fun setupCategoriesSpinner(
        spinner: AppCompatSpinner,
        networkResponse: NetworkRequestResponse<DataWithCountModel<List<CategoryModel>?>>?,
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
                val modelsList = networkResponse.data?.data
                val dataList = modelsList?.mapNotNull { it.name }?.toTypedArray()

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
            spinner.setSelection(viewModel.categoriesPosition.value!!)
        }
    }

}