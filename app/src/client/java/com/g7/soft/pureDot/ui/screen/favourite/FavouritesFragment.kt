package com.g7.soft.pureDot.ui.screen.favourite

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.adapter.PagedProductsAdapter
import com.g7.soft.pureDot.constant.ProjectConstant
import com.g7.soft.pureDot.data.PaginationDataSource
import com.g7.soft.pureDot.databinding.FragmentFavouritesBinding
import com.g7.soft.pureDot.ext.observeApiResponse
import com.g7.soft.pureDot.model.ProductModel
import com.zeugmasolutions.localehelper.currentLocale

class FavouritesFragment : Fragment() {
    private lateinit var binding: FragmentFavouritesBinding
    internal lateinit var viewModel: FavouritesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_favourites, container, false)

        viewModel = ViewModelProvider(this).get(FavouritesViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // setup pagination
        viewModel.productsPagedList =
            PaginationDataSource.initializedPagedListBuilder<ProductModel>(
                config = PagedList.Config.Builder()
                    .setPageSize(ProjectConstant.ITEMS_PER_PAGE)
                    .setEnablePlaceholders(true)
                    .build(),
                fragment = this@FavouritesFragment,
            ).build()

        // setup observers
        val productsAdapter = PagedProductsAdapter(this, editWishList = this::editWishList)
        binding.productsRv.adapter = productsAdapter
        viewModel.productsPagedList?.observe(viewLifecycleOwner, {
            productsAdapter.submitList(it)
        })

        // setup click listener

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_notification, menu)
    }


    private fun editWishList(
        tokenId: String,
        productId: String?,
        doAdd: Boolean,
        onComplete: () -> Unit
    ) {
        viewModel.editWishList(
            requireActivity().currentLocale.toLanguageTag(),
            tokenId = tokenId,
            productId = productId,
            doAdd = doAdd
        ).observeApiResponse(this, { onComplete.invoke() })
    }
}