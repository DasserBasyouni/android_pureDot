package com.g7.soft.pureDot.ui.screen.home

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.adapter.*
import com.g7.soft.pureDot.constant.ApiConstant
import com.g7.soft.pureDot.databinding.FragmentHomeBinding
import com.g7.soft.pureDot.ext.observeApiResponse
import com.g7.soft.pureDot.repo.UserRepository
import com.google.android.material.tabs.TabLayoutMediator
import com.zeugmasolutions.localehelper.currentLocale
import kotlinx.coroutines.launch

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
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // setup search
        requireActivity().findViewById<View>(R.id.filterIv).setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToFilterFragment()
            )
        }
        requireActivity().findViewById<View>(R.id.searchIv).setOnClickListener {
            val bundle = bundleOf(
                "sliderType" to ApiConstant.SliderOfferType.SEARCH_RESULTS,
            )
            findNavController().navigate(R.id.allProductsFragment, bundle)
        }

        // fetch data
        lifecycleScope.launch {
            val tokenId =
                UserRepository("").getTokenId(requireContext())
            viewModel.fetchScreenData(requireActivity().currentLocale.toLanguageTag(), tokenId)
        }

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

        val categoriesAdapter = CategoriesAdapter(this)
        binding.categoriesRv.adapter = categoriesAdapter
        viewModel.categoriesResponse.observe(viewLifecycleOwner, {
            viewModel.categoriesLcee.value!!.response.value = it
            categoriesAdapter.submitList(it.data?.data)
        })

        val latestOffersAdapter = ProductsAdapter(this, editWishList = this::editWishList)
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

        val latestProductsAdapter =
            StaggeredProductsAdapter(this, editWishList = this::editWishList)
        binding.latestProductsRv.adapter = latestProductsAdapter
        viewModel.latestProductsResponse.observe(viewLifecycleOwner, {
            viewModel.latestProductsLcee.value!!.response.value = it
            latestProductsAdapter.submitList(it.data?.data)
        })

        val bestSellingAdapter =
            ProductsAdapter(this, isGrid = false, editWishList = this::editWishList)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_notification, menu)
    }


    private fun editWishList(
        tokenId: String?,
        productId: String?,
        doAdd: Boolean,
        onComplete: () -> Unit
    ) {
        lifecycleScope.launch {
            val isGuestAccount =
                UserRepository("").getIsGuestAccount(requireContext())

            if (isGuestAccount)
                findNavController().navigate(R.id.loginFragment)
            else
                viewModel.editWishList(
                    requireActivity().currentLocale.toLanguageTag(),
                    tokenId = tokenId,
                    productId = productId,
                    doAdd = doAdd
                ).observeApiResponse(this@HomeFragment, { onComplete.invoke() })
        }
    }
}