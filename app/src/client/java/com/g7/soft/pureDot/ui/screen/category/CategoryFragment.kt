package com.g7.soft.pureDot.ui.screen.category

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.paging.PagedList
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.adapter.*
import com.g7.soft.pureDot.constant.ProjectConstant
import com.g7.soft.pureDot.data.PaginationDataSource
import com.g7.soft.pureDot.databinding.FragmentCategoryBinding
import com.g7.soft.pureDot.model.ProductModel
import com.g7.soft.pureDot.ui.screen.MainActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.zeugmasolutions.localehelper.currentLocale
import kotlinx.android.synthetic.client.activity_main.*

class CategoryFragment : Fragment() {
    private lateinit var binding: FragmentCategoryBinding
    private lateinit var viewModelFactory: CategoryViewModelFactory
    internal lateinit var viewModel: CategoryViewModel
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

        val productsAdapter = PagedProductsAdapter(this)
        binding.productsRv.adapter = productsAdapter
        viewModel.productsPagedList?.observe(viewLifecycleOwner, {
            productsAdapter.submitList(it)
        })

        // setup click listener
        /*binding.storesSeeAllTv.setOnClickListener {
            findNavController().navigate(R.id.allStoresFragment)
        }*/

    }

}