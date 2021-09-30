package com.g7.soft.pureDot.ui.screen.filter

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.constant.ApiConstant
import com.g7.soft.pureDot.databinding.FragmentFilterBinding
import com.g7.soft.pureDot.ext.makeLinks
import com.g7.soft.pureDot.repo.UserRepository
import com.zeugmasolutions.localehelper.currentLocale
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class FilterFragment : Fragment() {
    private lateinit var binding: FragmentFilterBinding
    internal val viewModel: FilterViewModel by viewModels(
        ownerProducer = { requireActivity() }
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_filter, container, false)

        lifecycleScope.launch {
            val currencySymbol = UserRepository("").getCurrencySymbol(requireContext())

            binding.currency = currencySymbol
            binding.viewModel = viewModel
            binding.lifecycleOwner = this@FilterFragment
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fromDateTil.editText?.setOnClickListener {
            val calendar = viewModel.fromDateCalendar.value!!
            DatePickerDialog(
                requireContext(),
                { _, year, monthOfYear, dayOfMonth ->
                    viewModel.fromDateCalendar.value?.set(Calendar.YEAR, year)
                    viewModel.fromDateCalendar.value?.set(Calendar.MONTH, monthOfYear)
                    viewModel.fromDateCalendar.value?.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    viewModel.fromDate.value = SimpleDateFormat(
                        getString(R.string.format_standard_date),
                        requireActivity().currentLocale
                    ).format(calendar.time)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.toDateTil.editText?.setOnClickListener {
            val calendar = viewModel.toDateCalendar.value!!
            DatePickerDialog(
                requireContext(),
                { _, year, monthOfYear, dayOfMonth ->
                    viewModel.toDateCalendar.value?.set(Calendar.YEAR, year)
                    viewModel.toDateCalendar.value?.set(Calendar.MONTH, monthOfYear)
                    viewModel.toDateCalendar.value?.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    viewModel.toDate.value = SimpleDateFormat(
                        getString(R.string.format_standard_date),
                        requireActivity().currentLocale
                    ).format(calendar.time)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            ApiConstant.OrderStatus.getSpinnerData(requireContext())
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.statusSpinner.adapter = adapter
            binding.statusSpinner.setSelection(viewModel.selectedStatusPosition.value!!)
        }

        viewModel.selectedStatusPosition.observe(viewLifecycleOwner, {
            Log.e("Z_", "selc: ${it}")
        })

        // setup click listener
        binding.applyBtn.setOnClickListener { findNavController().popBackStack() }
        binding.resetFilterTv.makeLinks(
            Pair(
                getString(R.string.reset_filter),
                View.OnClickListener {
                    viewModel.resetFilter()
                }), doChangeColor = false
        )
    }

}