package com.g7.soft.pureDot.ui.screen.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.adapter.OffersSliderAdapter
import com.g7.soft.pureDot.adapter.ProductsAdapter
import com.g7.soft.pureDot.adapter.StaggeredProductsAdapter
import com.g7.soft.pureDot.adapter.StoresAdapter
import com.g7.soft.pureDot.constant.ApiConstant
import com.g7.soft.pureDot.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.zeugmasolutions.localehelper.currentLocale

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    internal lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_home, container, false)

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // setup search
        requireActivity().findViewById<View>(R.id.filterIv).setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToFilterFragment(
                    viewModel.latestOffersResponse.value?.data?.data?.first()?.currency
                )
            )
        }
        requireActivity().findViewById<View>(R.id.searchIv).setOnClickListener {
            // todo
        }

        // fetch data
        viewModel.fetchScreenData(requireActivity().currentLocale.toLanguageTag())

        // setup observers
        val sliderOffersAdapter0 = OffersSliderAdapter(this)
        binding.sliderOffersLceeLayoutVp0.adapter = sliderOffersAdapter0
        TabLayoutMediator(
            binding.sliderOffersLceeLayoutTl0,
            binding.sliderOffersLceeLayoutVp0
        ) { tab, position ->
            //Some implementation
        }.attach()
        viewModel.sliderOffersResponse0.observe(viewLifecycleOwner, {
            viewModel.sliderOffersLcee0.value!!.response.value = it
            sliderOffersAdapter0.submitList(it.data)
        })
        viewModel.sliderOffersPosition0.observe(viewLifecycleOwner, {
            binding.sliderOffersLceeLayoutVp0.currentItem = it
        })

        val storesAdapter = StoresAdapter(this)
        binding.storesRv.adapter = storesAdapter
        viewModel.storesResponse.observe(viewLifecycleOwner, {
            viewModel.storesLcee.value!!.response.value = it
            storesAdapter.submitList(it.data?.data)
        })

        val sliderOffersAdapter1 = OffersSliderAdapter(this)
        binding.sliderOffersLceeLayoutVp1.adapter = sliderOffersAdapter1
        TabLayoutMediator(
            binding.sliderOffersLceeLayoutTl1,
            binding.sliderOffersLceeLayoutVp1
        ) { tab, position ->
            //Some implementation
        }.attach()
        viewModel.sliderOffersResponse1.observe(viewLifecycleOwner, {
            viewModel.sliderOffersLcee1.value!!.response.value = it
            sliderOffersAdapter1.submitList(it.data)
        })
        viewModel.sliderOffersPosition1.observe(viewLifecycleOwner, {
            binding.sliderOffersLceeLayoutVp1.currentItem = it
        })

        val latestOffersAdapter = ProductsAdapter(this)
        binding.latestOffersRv.adapter = latestOffersAdapter
        viewModel.latestOffersResponse.observe(viewLifecycleOwner, {
            viewModel.latestOffersLcee.value!!.response.value = it
            latestOffersAdapter.submitList(it.data?.data)
        })

        val sliderOffersAdapter2 = OffersSliderAdapter(this)
        binding.sliderOffersLceeLayoutVp2.adapter = sliderOffersAdapter2
        TabLayoutMediator(
            binding.sliderOffersLceeLayoutTl2,
            binding.sliderOffersLceeLayoutVp2
        ) { tab, position ->
            //Some implementation
        }.attach()
        viewModel.sliderOffersResponse2.observe(viewLifecycleOwner, {
            viewModel.sliderOffersLcee2.value!!.response.value = it
            sliderOffersAdapter2.submitList(it.data)
        })
        viewModel.sliderOffersPosition2.observe(viewLifecycleOwner, {
            binding.sliderOffersLceeLayoutVp2.currentItem = it
        })

        val latestProductsAdapter = StaggeredProductsAdapter(this)
        binding.latestProductsRv.adapter = latestProductsAdapter
        viewModel.latestProductsResponse.observe(viewLifecycleOwner, {
            viewModel.latestProductsLcee.value!!.response.value = it
            latestProductsAdapter.submitList(it.data?.data)
        })

        val bestSellingAdapter = ProductsAdapter(this, isGrid = false)
        binding.bestSellingRv.adapter = bestSellingAdapter
        viewModel.bestSellingResponse.observe(viewLifecycleOwner, {
            viewModel.bestSellingLcee.value!!.response.value = it
            bestSellingAdapter.submitList(it.data?.data)
        })


        // setup click listener
        binding.storesSeeAllTv.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_allStoresFragment)
        }
        binding.categoriesSeeAllTv.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_allCategoriesFragment)
        }
        binding.categoriesSeeAllTv.setOnClickListener { // todo all stores
            findNavController().navigate(R.id.action_homeFragment_to_allCategoriesFragment)
        }
        binding.latestOffersSeeAllTv.setOnClickListener {
            val bundle = bundleOf(
                "sliderType" to ApiConstant.SliderOfferType.INNER_LATEST_OFFERS
            )
            findNavController().navigate(R.id.allProductsFragment, bundle)
        }
        binding.latestProductsSellAllTv.setOnClickListener {
            val bundle = bundleOf(
                "sliderType" to ApiConstant.SliderOfferType.INNER_LATEST_PRODUCTS
            )
            findNavController().navigate(R.id.allProductsFragment, bundle)
        }
        binding.bestSellingSellAllTv.setOnClickListener {
            val bundle = bundleOf(
                "sliderType" to ApiConstant.SliderOfferType.INNER_BEST_SELLING
            )
            findNavController().navigate(R.id.allProductsFragment, bundle)
        }

    }
}