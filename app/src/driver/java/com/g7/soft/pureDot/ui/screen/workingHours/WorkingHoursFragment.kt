package com.g7.soft.pureDot.ui.screen.workingHours

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.databinding.FragmentWorkingHoursBinding
import com.g7.soft.pureDot.repo.UserRepository
import com.zeugmasolutions.localehelper.currentLocale
import kotlinx.coroutines.launch

class WorkingHoursFragment : Fragment() {
    private lateinit var binding: FragmentWorkingHoursBinding
    internal lateinit var viewModel: WorkingHoursViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                layoutInflater,
                R.layout.fragment_working_hours,
                container,
                false
            )

        viewModel = ViewModelProvider(this).get(WorkingHoursViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // fetch data
        lifecycleScope.launch {
            val tokenId = UserRepository("").getTokenId(requireContext())
            viewModel.getMyOrders(requireActivity().currentLocale.toLanguageTag(), tokenId)
        }

        // setup observers
        viewModel.workingHoursResponse.observe(viewLifecycleOwner, {
            viewModel.workingHoursLcee.value!!.response.value = it
            //binding.workingHoursRv.adapter = WorkingHoursHeaderAdapter(this, it.data)
        })

        // setup click listener

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_notification, menu)
    }
}