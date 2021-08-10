package com.g7.soft.pureDot.ui.screen.services

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.adapter.PagedServicesAdapter
import com.g7.soft.pureDot.constant.ProjectConstant
import com.g7.soft.pureDot.data.PaginationDataSource
import com.g7.soft.pureDot.databinding.FragmentServicesBinding
import com.g7.soft.pureDot.model.ServiceModel

class ServicesFragment : Fragment() {
    private lateinit var binding: FragmentServicesBinding
    internal lateinit var viewModel: ServicesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_services, container, false)

        viewModel = ViewModelProvider(this).get(ServicesViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // setup pagination
        viewModel.servicesPagedList =
            PaginationDataSource.initializedPagedListBuilder<ServiceModel>(
                config = PagedList.Config.Builder()
                    .setPageSize(ProjectConstant.ITEMS_PER_PAGE)
                    .setEnablePlaceholders(true)
                    .build(),
                fragment = this@ServicesFragment,
            ).build()

        // setup observers
        val servicesAdapter = PagedServicesAdapter(this)
        binding.servicesRv.adapter = servicesAdapter
        viewModel.servicesPagedList?.observe(viewLifecycleOwner, {
            servicesAdapter.submitList(it)
        })

        // setup click listener

    }

}