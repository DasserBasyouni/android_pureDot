package com.g7.soft.pureDot.ui.screen.seeAll.products

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.paging.PagedList
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.adapter.OffersSliderAdapter
import com.g7.soft.pureDot.adapter.PagedProductsAdapter
import com.g7.soft.pureDot.constant.ApiConstant
import com.g7.soft.pureDot.constant.ProjectConstant
import com.g7.soft.pureDot.data.PaginationDataSource
import com.g7.soft.pureDot.databinding.FragmentAllProductsBinding
import com.g7.soft.pureDot.ext.observeApiResponse
import com.g7.soft.pureDot.model.ProductModel
import com.g7.soft.pureDot.ui.screen.MainActivity
import com.g7.soft.pureDot.ui.screen.filter.FilterViewModel
import com.google.android.material.tabs.TabLayoutMediator
import com.zeugmasolutions.localehelper.currentLocale
import kotlinx.android.synthetic.client.activity_main.*
import android.view.inputmethod.EditorInfo

import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.g7.soft.pureDot.repo.ClientRepository
import kotlinx.coroutines.launch


// todo fix title of screens that appears in other screens, maybe that fix is in navigation also 3 title for this screen
class AllProductsFragment : Fragment() {
    private lateinit var binding: FragmentAllProductsBinding
    private lateinit var viewModelFactory: AllProductsViewModelFactory
    internal lateinit var viewModel: AllProductsViewModel
    internal val filterViewModel: FilterViewModel by viewModels(
        ownerProducer = { requireActivity() }
    )
    internal val args: AllProductsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                layoutInflater,
                R.layout.fragment_all_products,
                container,
                false
            )

        viewModelFactory = AllProductsViewModelFactory(
            sliderType = args.sliderType,
            storeId = args.storeId
        )
        viewModel = ViewModelProvider(this, viewModelFactory).get(AllProductsViewModel::class.java)

        binding.viewModel = viewModel
        binding.filterViewModel = filterViewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set screen title
        (requireActivity() as MainActivity).toolbar_title.text = when (args.sliderType) {
            ApiConstant.SliderOfferType.INNER_LATEST_PRODUCTS -> getString(R.string.latest_products)
            ApiConstant.SliderOfferType.INNER_BEST_SELLING -> getString(R.string.best_selling)
            ApiConstant.SliderOfferType.INNER_LATEST_OFFERS -> getString(R.string.latest_offers)
            else -> null
        }

        // fetch data
        viewModel.fetchScreenData(requireActivity().currentLocale.toLanguageTag())

        // setup pagination
        viewModel.productsPagedList =
            PaginationDataSource.initializedPagedListBuilder<ProductModel>(
                config = PagedList.Config.Builder()
                    .setPageSize(ProjectConstant.ITEMS_PER_PAGE)
                    .setEnablePlaceholders(true)
                    .build(),
                fragment = this@AllProductsFragment,
            ).build()

        // setup observers
        if (args.sliderType != ApiConstant.SliderOfferType.SEARCH_RESULTS) {
            val sliderOffersAdapter0 = OffersSliderAdapter(this)
            binding.sliderOffersLceeLayoutVp.adapter = sliderOffersAdapter0
            TabLayoutMediator(
                binding.sliderOffersLceeLayoutTl,
                binding.sliderOffersLceeLayoutVp
            ) { tab, position ->
                //Some implementation
            }.attach()
            viewModel.sliderOffersResponse.observe(viewLifecycleOwner, {
                viewModel.sliderOffersLcee.value!!.response.value = it
                sliderOffersAdapter0.submitList(it.data)
            })
            viewModel.sliderOffersPosition.observe(viewLifecycleOwner, {
                binding.sliderOffersLceeLayoutVp.currentItem = it
            })
        }

        val productsAdapter = PagedProductsAdapter(this, editWishList = this::editWishList)
        binding.productsRv.adapter = productsAdapter
        viewModel.productsPagedList?.observe(viewLifecycleOwner, {
            productsAdapter.submitList(it)
        })

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

        // setup onClick
        binding.root.findViewById<EditText>(R.id.appCompatEditText)
            .setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    refreshSearch()
                    true
                } else false
            }
        binding.root.findViewById<ImageView>(R.id.filterIv).setOnClickListener {
            lifecycleScope.launch {
                val currencySymbol =
                    ClientRepository("").getLocalUserData(requireContext()).currencySymbol

                val bundle = bundleOf("currency" to currencySymbol)
                findNavController().navigate(R.id.filterFragment, bundle)
            }
        }
        binding.root.findViewById<ImageView>(R.id.searchIv).setOnClickListener {
            refreshSearch()
        }
    }


    private fun refreshSearch() {
        viewModel.productsPagedList?.value?.dataSource?.invalidate()
    }

    private fun editWishList(
        productId: String?,
        doAdd: Boolean,
        onComplete: () -> Unit
    ) {
        lifecycleScope.launch {
            val tokenId =
                ClientRepository("").getLocalUserData(requireContext()).tokenId

            viewModel.editWishList(
                requireActivity().currentLocale.toLanguageTag(),
                tokenId = tokenId,
                productId = productId,
                doAdd = doAdd
            ).observeApiResponse(this@AllProductsFragment, { onComplete.invoke() })
        }
    }
}