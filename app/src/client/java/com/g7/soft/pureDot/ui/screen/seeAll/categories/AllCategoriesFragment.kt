package com.g7.soft.pureDot.ui.screen.seeAll.categories

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
import androidx.navigation.fragment.findNavController
import androidx.paging.PagedList
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.adapter.PagedCategoriesAdapter
import com.g7.soft.pureDot.constant.ApiConstant
import com.g7.soft.pureDot.constant.ProjectConstant
import com.g7.soft.pureDot.data.PaginationDataSource
import com.g7.soft.pureDot.databinding.FragmentAllCategoriesBinding
import com.g7.soft.pureDot.model.CategoryModel
import com.g7.soft.pureDot.ui.GridSpacingItemDecoration
import com.g7.soft.pureDot.ui.screen.filter.FilterViewModel

class AllCategoriesFragment : Fragment() {
    private lateinit var binding: FragmentAllCategoriesBinding
    internal lateinit var viewModel: AllCategoriesViewModel
    internal val filterViewModel: FilterViewModel by viewModels(
        ownerProducer = { requireActivity() }
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                layoutInflater,
                R.layout.fragment_all_categories,
                container,
                false
            )

        viewModel = ViewModelProvider(this).get(AllCategoriesViewModel::class.java)

        binding.filterViewModel = filterViewModel
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // setup pagination
        viewModel.categoriesPagedList =
            PaginationDataSource.initializedPagedListBuilder<CategoryModel>(
                config = PagedList.Config.Builder()
                    .setPageSize(ProjectConstant.ITEMS_PER_PAGE)
                    .setEnablePlaceholders(true)
                    .build(),
                fragment = this@AllCategoriesFragment,
            ).build()

        // setup observers
        val categoriesAdapter = PagedCategoriesAdapter(this, isGrid = true)
        binding.categoriesRv.adapter = categoriesAdapter
        viewModel.categoriesPagedList?.observe(viewLifecycleOwner, {
            categoriesAdapter.submitList(it)
        })

        // decoration
        binding.categoriesRv.addItemDecoration(GridSpacingItemDecoration(3, 50, true))

        // setup click listener
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_notification, menu)
    }


    private fun navigateToAllProductsSearch() {
        val bundle = bundleOf(
            "sliderType" to ApiConstant.SliderOfferType.SEARCH_RESULTS,
        )
        findNavController().navigate(R.id.allProductsFragment, bundle)
    }
}