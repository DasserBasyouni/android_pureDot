package com.g7.soft.pureDot.ui.screen.store

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.PagedList
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.adapter.OffersSliderAdapter
import com.g7.soft.pureDot.adapter.PagedCategoriesAdapter
import com.g7.soft.pureDot.adapter.ProductsAdapter
import com.g7.soft.pureDot.adapter.StaggeredProductsAdapter
import com.g7.soft.pureDot.constant.ApiConstant
import com.g7.soft.pureDot.constant.ProjectConstant
import com.g7.soft.pureDot.data.PaginationDataSource
import com.g7.soft.pureDot.databinding.FragmentStoreBinding
import com.g7.soft.pureDot.ext.observeApiResponse
import com.g7.soft.pureDot.model.CategoryModel
import com.g7.soft.pureDot.repo.UserRepository
import com.g7.soft.pureDot.ui.GridSpacingItemDecoration
import com.g7.soft.pureDot.ui.screen.MainActivity
import com.g7.soft.pureDot.ui.screen.filter.FilterViewModel
import com.g7.soft.pureDot.ui.screen.order.OrderFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.zeugmasolutions.localehelper.currentLocale
import kotlinx.android.synthetic.client.activity_main.*
import kotlinx.coroutines.launch

class StoreFragment : Fragment() {


    companion object {
        var refreshData: ((String) -> Unit)? = null
        var isRunning = false
    }


    private lateinit var binding: FragmentStoreBinding
    private lateinit var viewModelFactory: StoreViewModelFactory
    internal lateinit var viewModel: StoreViewModel
    internal val filterViewModel: FilterViewModel by viewModels(
        ownerProducer = { requireActivity() }
    )
    private val args: StoreFragmentArgs by navArgs()


    override fun onStart() {
        super.onStart()
        OrderFragment.isRunning = true
    }

    override fun onStop() {
        super.onStop()
        OrderFragment.isRunning = false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_store, container, false)

        viewModelFactory = StoreViewModelFactory(
            store = args.store,
            shopId = args.store?.id ?: args.shopId,
        )
        viewModel = ViewModelProvider(this, viewModelFactory).get(StoreViewModel::class.java)

        binding.filterViewModel = filterViewModel
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        refreshData = { storeId ->
            if (storeId == viewModel.store?.id) {
                // re-get paged data
                viewModel.categoriesPagedList?.value?.dataSource?.invalidate()

                // fetch data
                lifecycleScope.launch {
                    val tokenId = UserRepository("").getTokenId(requireContext())
                    viewModel.fetchScreenData(
                        requireActivity().currentLocale.toLanguageTag(),
                        tokenId
                    )
                }
            }
        }

        // todo cover case in which storeId is only available so name is not available (getStoreData then duplicate the line belwo)
        // set screen title
        (requireActivity() as MainActivity).toolbar_title.text = args.store?.name

        // setup pagination
        viewModel.categoriesPagedList =
            PaginationDataSource.initializedPagedListBuilder<CategoryModel>(
                config = PagedList.Config.Builder()
                    .setPageSize(ProjectConstant.ITEMS_PER_PAGE)
                    .setEnablePlaceholders(true)
                    .build(),
                fragment = this@StoreFragment,
            ).build()

        // fetch data
        lifecycleScope.launch {
            val tokenId = UserRepository("").getTokenId(requireContext())
            viewModel.fetchScreenData(requireActivity().currentLocale.toLanguageTag(), tokenId)
        }

        // setup observers
        val categoriesAdapter = PagedCategoriesAdapter(this, isGrid = true, shopId = (args.store?.id ?: args.shopId))
        binding.categoriesRv.adapter = categoriesAdapter
        viewModel.categoriesPagedList?.observe(viewLifecycleOwner, {
            categoriesAdapter.submitList(it)
        })

        // decoration
        binding.categoriesRv.addItemDecoration(GridSpacingItemDecoration(3, 30, true))

        val sliderOffersAdapter = OffersSliderAdapter(this)
        binding.sliderOffersLceeLayoutVp.adapter = sliderOffersAdapter
        TabLayoutMediator(
            binding.sliderOffersLceeLayoutTl,
            binding.sliderOffersLceeLayoutVp
        ) { _, _ -> }.attach()
        viewModel.sliderOffersResponse.observe(viewLifecycleOwner, {
            viewModel.sliderOffersLcee.value!!.response.value = it
            sliderOffersAdapter.submitList(it.data)
        })
        viewModel.sliderOffersPosition.observe(viewLifecycleOwner, {
            binding.sliderOffersLceeLayoutVp.currentItem = it
        })

        val latestOffersAdapter = ProductsAdapter(this, editWishList = this::editWishList)
        binding.latestOffersRv.adapter = latestOffersAdapter
        viewModel.latestOffersResponse.observe(viewLifecycleOwner, {
            viewModel.latestOffersLcee.value!!.response.value = it
            latestOffersAdapter.submitList(it.data?.data)
        })

        val latestProductsAdapter =
            StaggeredProductsAdapter(this, editWishList = this::editWishList)
        binding.latestProductsRv.adapter = latestProductsAdapter
        viewModel.latestProductsResponse.observe(viewLifecycleOwner, {
            viewModel.latestProductsLcee.value!!.response.value = it
            latestProductsAdapter.submitList(it.data?.data)
        })

        // setup click listener
        binding.latestOffersSeeAllTv.setOnClickListener {
            val bundle = bundleOf(
                "sliderType" to ApiConstant.SliderOfferType.INNER_LATEST_OFFERS,
                "storeId" to (args.store?.id ?: args.shopId)
            )
            findNavController().navigate(R.id.allProductsFragment, bundle)
        }
        binding.latestProductsSellAllTv.setOnClickListener {
            val bundle = bundleOf(
                "sliderType" to ApiConstant.SliderOfferType.INNER_LATEST_PRODUCTS,
                "storeId" to (args.store?.id ?: args.shopId)
            )
            findNavController().navigate(R.id.allProductsFragment, bundle)
        }

        // setup search bar
        binding.root.findViewById<EditText>(R.id.appCompatEditText)
            .setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    navigateToAllProductsSearch()
                    true
                } else false
            }
        binding.root.findViewById<ImageView>(R.id.filterIv).setOnClickListener {
            findNavController().navigate(R.id.filterFragment)
        }
        binding.root.findViewById<ImageView>(R.id.searchIv).setOnClickListener {
            navigateToAllProductsSearch()
        }

        // fix non-working observer of search include layout
        binding.root.findViewById<EditText>(R.id.appCompatEditText).addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

            override fun afterTextChanged(s: Editable?) {
                filterViewModel.searchText.value = s.toString()
            }
        })
    }


    private fun navigateToAllProductsSearch() {
        if (args.shopId != null) {
            filterViewModel.resetFilter()
            filterViewModel.selectedStoresIds = mutableListOf(args.shopId!!)
        }

        val bundle = bundleOf(
            "sliderType" to ApiConstant.SliderOfferType.SEARCH_RESULTS,
        )
        findNavController().navigate(R.id.allProductsFragment, bundle)
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
                ).observeApiResponse(this@StoreFragment, { onComplete.invoke() })
        }
    }
}