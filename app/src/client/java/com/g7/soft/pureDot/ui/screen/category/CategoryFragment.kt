package com.g7.soft.pureDot.ui.screen.category

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
import com.g7.soft.pureDot.adapter.*
import com.g7.soft.pureDot.constant.ApiConstant
import com.g7.soft.pureDot.constant.ProjectConstant
import com.g7.soft.pureDot.data.PaginationDataSource
import com.g7.soft.pureDot.databinding.FragmentCategoryBinding
import com.g7.soft.pureDot.ext.observeApiResponse
import com.g7.soft.pureDot.model.ProductModel
import com.g7.soft.pureDot.repo.ClientRepository
import com.g7.soft.pureDot.ui.screen.MainActivity
import com.g7.soft.pureDot.ui.screen.filter.FilterViewModel
import com.google.android.material.tabs.TabLayoutMediator
import com.zeugmasolutions.localehelper.currentLocale
import kotlinx.android.synthetic.client.activity_main.*
import kotlinx.coroutines.launch

class CategoryFragment : Fragment() {
    private lateinit var binding: FragmentCategoryBinding
    private lateinit var viewModelFactory: CategoryViewModelFactory
    internal lateinit var viewModel: CategoryViewModel
    internal val filterViewModel: FilterViewModel by viewModels(
        ownerProducer = { requireActivity() }
    )
    private val args: CategoryFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_category, container, false)

        viewModelFactory = CategoryViewModelFactory(
            category = args.category,
        )
        viewModel = ViewModelProvider(this, viewModelFactory).get(CategoryViewModel::class.java)

        binding.filterViewModel = filterViewModel
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set screen title
        (requireActivity() as MainActivity).toolbar_title.text = args.category.name

        // setup pagination
        viewModel.productsPagedList =
            PaginationDataSource.initializedPagedListBuilder<ProductModel>(
                config = PagedList.Config.Builder()
                    .setPageSize(ProjectConstant.ITEMS_PER_PAGE)
                    .setEnablePlaceholders(true)
                    .build(),
                fragment = this@CategoryFragment,
            ).build()

        // fetch data
        viewModel.fetchScreenData(requireActivity().currentLocale.toLanguageTag())

        // setup observers
        /*val storesAdapter = StoresAdapter(this)
        binding.storesRv.adapter = storesAdapter
        viewModel.storesResponse.observe(viewLifecycleOwner, {
            viewModel.storesLcee.value!!.response.value = it
            storesAdapter.submitList(it.data?.data)
        })*/

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

        val productsAdapter = PagedProductsAdapter(this, editWishList = this::editWishList)
        binding.productsRv.adapter = productsAdapter
        viewModel.productsPagedList?.observe(viewLifecycleOwner, {
            productsAdapter.submitList(it)
        })

        // setup click listener
        binding.root.findViewById<EditText>(R.id.appCompatEditText)
            .setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    navigateToAllProductsSearch()
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
        val bundle = bundleOf(
            "sliderType" to ApiConstant.SliderOfferType.SEARCH_RESULTS,
        )
        findNavController().navigate(R.id.allProductsFragment, bundle)
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
            ).observeApiResponse(this@CategoryFragment, { onComplete.invoke() })
        }
    }
}