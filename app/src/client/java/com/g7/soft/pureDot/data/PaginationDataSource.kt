package com.g7.soft.pureDot.data

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.constant.ApiConstant
import com.g7.soft.pureDot.constant.ApiConstant.SliderOfferType.*
import com.g7.soft.pureDot.constant.ProjectConstant
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.*
import com.g7.soft.pureDot.ui.screen.browse.BrowseFragment
import com.g7.soft.pureDot.ui.screen.category.CategoryFragment
import com.g7.soft.pureDot.ui.screen.favourite.FavouritesFragment
import com.g7.soft.pureDot.ui.screen.filter.FilterFragment
import com.g7.soft.pureDot.ui.screen.myOrders.MyOrdersFragment
import com.g7.soft.pureDot.ui.screen.myWallet.MyWalletFragment
import com.g7.soft.pureDot.ui.screen.seeAll.categories.AllCategoriesFragment
import com.g7.soft.pureDot.ui.screen.seeAll.products.AllProductsFragment
import com.g7.soft.pureDot.ui.screen.seeAll.reviews.AllReviewsFragment
import com.g7.soft.pureDot.ui.screen.seeAll.stores.AllStoresFragment
import com.g7.soft.pureDot.ui.screen.services.ServicesFragment
import com.g7.soft.pureDot.ui.screen.store.StoreFragment
import com.g7.soft.pureDot.utils.ProjectDialogUtils
import com.zeugmasolutions.localehelper.currentLocale
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

class PaginationDataSource<T>(
    private val fragment: Fragment,
    private val itemsPerPage: Int,
    private val isFirstPagedList: Boolean,
) : PageKeyedDataSource<Int, T>() {

    companion object {
        fun <T> initializedPagedListBuilder(
            config: PagedList.Config,
            fragment: Fragment,
            isFirstPagedList: Boolean = true,
        ): LivePagedListBuilder<Int, T> {
            val dataSourceFactory = object : DataSource.Factory<Int, T>() {
                override fun create(): DataSource<Int, T> {
                    return PaginationDataSource(
                        fragment = fragment,
                        itemsPerPage = config.pageSize,
                        isFirstPagedList = isFirstPagedList
                    )
                }
            }
            return LivePagedListBuilder(dataSourceFactory, config)
        }
    }


    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, T>
    ) {
        fragment.lifecycleScope.launch {
            try {
                fetchData(initCallback = callback, fragment = fragment, pageKey = 2)
            } catch (exception: Exception) {
                Timber.e("Failed to fetch data!")
            }

        }

    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {
        fragment.lifecycleScope.launch {
            try {
                fetchData(
                    params = params,
                    callback = callback,
                    fragment = fragment,
                    pageKey = params.key.inc()
                )
            } catch (exception: Exception) {
                Timber.e("Failed to fetch data!")
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {
        fragment.lifecycleScope.launch {
            try {
                fetchData(
                    params = params,
                    callback = callback,
                    fragment = fragment,
                    pageKey = params.key.dec()
                )
            } catch (exception: Exception) {
                Timber.e("Failed to fetch data!")
            }
        }
    }


    private suspend fun fetchData(
        params: LoadParams<Int>? = null,
        callback: LoadCallback<Int, T>? = null,
        initCallback: LoadInitialCallback<Int, T>? = null,
        fragment: Fragment,
        pageKey: Int,
    ) = coroutineScope {
        val isInitialLoad = params == null

        when {
            fragment is FilterFragment && isFirstPagedList -> {
                val viewModel = fragment.viewModel

                if (isInitialLoad)
                    viewModel.categoriesLcee.value!!.response.value =
                        NetworkRequestResponse.loading<List<*>>()

                CategoriesRepository(fragment.requireActivity().currentLocale.toLanguageTag()).getCategories(
                    pageNumber = if (isInitialLoad) 1 else params?.key,
                    itemsPerPage = itemsPerPage,
                    searchText = null,
                    shopId = null,
                ).observe(fragment, {
                    initCallback?.onResult((it.data?.data ?: listOf()) as List<T>, null, 2)
                    callback?.onResult((it.data?.data ?: listOf()) as List<T>, pageKey)

                    if (isInitialLoad)
                        viewModel.categoriesLcee.value!!.response.value = it

                    handleErrors(it.status, it.apiErrorStatus)
                })
            }
            fragment is FilterFragment && !isFirstPagedList -> {
                val viewModel = fragment.viewModel

                if (isInitialLoad)
                    viewModel.storesLcee.value!!.response.value =
                        NetworkRequestResponse.loading<List<*>>()

                // todo  searchText = null, categoryId = null?
                StoreRepository(fragment.requireActivity().currentLocale.toLanguageTag()).getAll(
                    pageNumber = if (isInitialLoad) 1 else params?.key,
                    itemsPerPage = itemsPerPage,
                    searchText = null,
                    categoryId = null
                ).observe(fragment, {
                    initCallback?.onResult((it.data?.data ?: listOf()) as List<T>, null, 2)
                    callback?.onResult((it.data?.data ?: listOf()) as List<T>, pageKey)

                    if (isInitialLoad)
                        viewModel.storesLcee.value!!.response.value = it

                    handleErrors(it.status, it.apiErrorStatus)
                })
            }
            fragment is AllProductsFragment && fragment.args.sliderType == INNER_LATEST_PRODUCTS -> {
                val viewModel = fragment.viewModel

                if (isInitialLoad)
                    viewModel.productsLcee.value!!.response.value =
                        NetworkRequestResponse.loading<List<*>>()

                // todo use filter values with saving them locally maybe
                ProductRepository(fragment.requireActivity().currentLocale.toLanguageTag()).getLatestProducts(
                    pageNumber = if (isInitialLoad) 1 else params?.key,
                    itemsPerPage = itemsPerPage,
                    shopId = null
                ).observe(fragment, {
                    initCallback?.onResult((it.data?.data ?: listOf()) as List<T>, null, 2)
                    callback?.onResult((it.data?.data ?: listOf()) as List<T>, pageKey)

                    if (isInitialLoad)
                        viewModel.productsLcee.value!!.response.value = it

                    handleErrors(it.status, it.apiErrorStatus)
                })
            }
            fragment is AllProductsFragment && fragment.args.sliderType == INNER_LATEST_OFFERS -> {
                val viewModel = fragment.viewModel

                if (isInitialLoad)
                    viewModel.productsLcee.value!!.response.value =
                        NetworkRequestResponse.loading<List<*>>()

                // todo use filter values with saving them locally maybe
                ProductRepository(fragment.requireActivity().currentLocale.toLanguageTag()).getLatestOffers(
                    pageNumber = if (isInitialLoad) 1 else params?.key,
                    itemsPerPage = itemsPerPage,
                    shopId = null
                ).observe(fragment, {
                    initCallback?.onResult((it.data?.data ?: listOf()) as List<T>, null, 2)
                    callback?.onResult((it.data?.data ?: listOf()) as List<T>, pageKey)

                    if (isInitialLoad)
                        viewModel.productsLcee.value!!.response.value = it

                    handleErrors(it.status, it.apiErrorStatus)
                })
            }
            fragment is AllProductsFragment && fragment.args.sliderType == INNER_BEST_SELLING -> {
                val viewModel = fragment.viewModel

                if (isInitialLoad)
                    viewModel.productsLcee.value!!.response.value =
                        NetworkRequestResponse.loading<List<*>>()

                // todo use filter values with saving them locally maybe
                ProductRepository(fragment.requireActivity().currentLocale.toLanguageTag()).getBestSelling(
                    pageNumber = if (isInitialLoad) 1 else params?.key,
                    itemsPerPage = itemsPerPage,
                    shopId = null
                ).observe(fragment, {
                    initCallback?.onResult((it.data?.data ?: listOf()) as List<T>, null, 2)
                    callback?.onResult((it.data?.data ?: listOf()) as List<T>, pageKey)

                    if (isInitialLoad)
                        viewModel.productsLcee.value!!.response.value = it

                    handleErrors(it.status, it.apiErrorStatus)
                })
            }
            fragment is AllProductsFragment && fragment.args.sliderType == SEARCH_RESULTS -> {
                val viewModel = fragment.viewModel
                val filterViewModel = fragment.filterViewModel

                if (isInitialLoad)
                    viewModel.productsLcee.value!!.response.value =
                        NetworkRequestResponse.loading<List<*>>()

                // todo use filter values with saving them locally maybe
                ProductRepository(fragment.requireActivity().currentLocale.toLanguageTag()).getProducts(
                    pageNumber = if (isInitialLoad) 1 else params?.key,
                    itemsPerPage = itemsPerPage,
                    categoriesIds = filterViewModel.selectedCategoriesIds,
                    fromPrice = filterViewModel.minPrice.value?.toInt(),
                    toPrice = filterViewModel.maxPrice.value?.toInt(),
                    storesIds = filterViewModel.selectedStoresIds,
                    minStars = filterViewModel.minStars,
                    searchText = filterViewModel.searchText.value,
                ).observe(fragment, {
                    initCallback?.onResult((it.data ?: listOf()) as List<T>, null, 2)
                    callback?.onResult((it.data ?: listOf()) as List<T>, pageKey)

                    if (isInitialLoad)
                        viewModel.productsLcee.value!!.response.value = it

                    handleErrors(it.status, it.apiErrorStatus)
                })
            }
            fragment is AllCategoriesFragment -> {
                val viewModel = fragment.viewModel

                if (isInitialLoad)
                    viewModel.categoriesLcee.value!!.response.value =
                        NetworkRequestResponse.loading<List<*>>()

                CategoriesRepository(fragment.requireActivity().currentLocale.toLanguageTag()).getCategories(
                    pageNumber = if (isInitialLoad) 1 else params?.key,
                    itemsPerPage = itemsPerPage,
                    searchText = null,
                    shopId = null,
                ).observe(fragment, {
                    initCallback?.onResult((it.data?.data ?: listOf()) as List<T>, null, 2)
                    callback?.onResult((it.data?.data ?: listOf()) as List<T>, pageKey)

                    if (isInitialLoad)
                        viewModel.categoriesLcee.value!!.response.value = it

                    handleErrors(it.status, it.apiErrorStatus)
                })
            }
            fragment is AllStoresFragment -> {
                val viewModel = fragment.viewModel

                if (isInitialLoad)
                    viewModel.storesLcee.value!!.response.value =
                        NetworkRequestResponse.loading<List<*>>()

                // todo  searchText = null, categoryId = null?
                StoreRepository(fragment.requireActivity().currentLocale.toLanguageTag()).getAll(
                    pageNumber = if (isInitialLoad) 1 else params?.key,
                    itemsPerPage = itemsPerPage,
                    searchText = null,
                    categoryId = null
                ).observe(fragment, {
                    initCallback?.onResult((it.data?.data ?: listOf()) as List<T>, null, 2)
                    callback?.onResult((it.data?.data ?: listOf()) as List<T>, pageKey)

                    if (isInitialLoad)
                        viewModel.storesLcee.value!!.response.value = it

                    handleErrors(it.status, it.apiErrorStatus)
                })
            }
            fragment is BrowseFragment && isFirstPagedList -> {
                val viewModel = fragment.viewModel

                if (isInitialLoad)
                    viewModel.categoriesLcee.value!!.response.value =
                        NetworkRequestResponse.loading<List<*>>()

                CategoriesRepository(fragment.requireActivity().currentLocale.toLanguageTag()).getCategories(
                    pageNumber = if (isInitialLoad) 1 else params?.key,
                    itemsPerPage = itemsPerPage,
                    searchText = null,
                    shopId = null,
                ).observe(fragment, {
                    initCallback?.onResult((it.data?.data ?: listOf()) as List<T>, null, 2)
                    callback?.onResult((it.data?.data ?: listOf()) as List<T>, pageKey)

                    if (isInitialLoad)
                        viewModel.categoriesLcee.value!!.response.value = it

                    handleErrors(it.status, it.apiErrorStatus)
                })
            }
            fragment is BrowseFragment && !isFirstPagedList -> {
                val viewModel = fragment.viewModel

                if (isInitialLoad)
                    viewModel.storesLcee.value!!.response.value =
                        NetworkRequestResponse.loading<List<*>>()

                // todo  searchText = null, categoryId = null?
                StoreRepository(fragment.requireActivity().currentLocale.toLanguageTag()).getAll(
                    pageNumber = if (isInitialLoad) 1 else params?.key,
                    itemsPerPage = itemsPerPage,
                    searchText = null,
                    categoryId = null
                ).observe(fragment, {
                    initCallback?.onResult((it.data?.data ?: listOf()) as List<T>, null, 2)
                    callback?.onResult((it.data?.data ?: listOf()) as List<T>, pageKey)

                    if (isInitialLoad)
                        viewModel.storesLcee.value!!.response.value = it

                    handleErrors(it.status, it.apiErrorStatus)
                })
            }
            fragment is StoreFragment -> {
                val viewModel = fragment.viewModel

                if (isInitialLoad)
                    viewModel.categoriesLcee.value!!.response.value =
                        NetworkRequestResponse.loading<List<*>>()

                CategoriesRepository(fragment.requireActivity().currentLocale.toLanguageTag()).getCategories(
                    pageNumber = if (isInitialLoad) 1 else params?.key,
                    itemsPerPage = itemsPerPage,
                    searchText = null,
                    shopId = viewModel.store?.id,
                ).observe(fragment, {
                    initCallback?.onResult((it.data?.data ?: listOf()) as List<T>, null, 2)
                    callback?.onResult((it.data?.data ?: listOf()) as List<T>, pageKey)

                    if (isInitialLoad)
                        viewModel.categoriesLcee.value!!.response.value = it

                    handleErrors(it.status, it.apiErrorStatus)
                })
            }
            fragment is CategoryFragment -> {
                val viewModel = fragment.viewModel

                if (isInitialLoad)
                    viewModel.productsLcee.value!!.response.value =
                        NetworkRequestResponse.loading<List<*>>()

                ProductRepository(fragment.requireActivity().currentLocale.toLanguageTag()).getProducts(
                    pageNumber = if (isInitialLoad) 1 else params?.key,
                    itemsPerPage = itemsPerPage,
                    searchText = null,
                    categoriesIds = listOfNotNull(viewModel.category?.id),
                    fromPrice = null,
                    toPrice = null,
                    storesIds = listOfNotNull(viewModel.shopId),
                    minStars = null,
                ).observe(fragment, {
                    initCallback?.onResult((it.data ?: listOf()) as List<T>, null, 2)
                    callback?.onResult((it.data ?: listOf()) as List<T>, pageKey)

                    if (isInitialLoad)
                        viewModel.productsLcee.value!!.response.value = it

                    handleErrors(it.status, it.apiErrorStatus)
                })
            }
            fragment is AllReviewsFragment && fragment.viewModel.isProduct -> {
                val viewModel = fragment.viewModel

                if (isInitialLoad)
                    viewModel.reviewsLcee.value!!.response.value =
                        NetworkRequestResponse.loading<List<*>>()

                ProductRepository(fragment.requireActivity().currentLocale.toLanguageTag()).getReviews(
                    pageNumber = if (isInitialLoad) 1 else params?.key,
                    itemsPerPage = itemsPerPage,
                    productId = viewModel.itemId,
                ).observe(fragment, {
                    initCallback?.onResult((it.data?.data ?: listOf()) as List<T>, null, 2)
                    callback?.onResult((it.data?.data ?: listOf()) as List<T>, pageKey)

                    if (isInitialLoad)
                        viewModel.reviewsLcee.value!!.response.value = it

                    handleErrors(it.status, it.apiErrorStatus)
                })
            }
            fragment is AllReviewsFragment && !fragment.viewModel.isProduct -> {
                val viewModel = fragment.viewModel

                if (isInitialLoad)
                    viewModel.reviewsLcee.value!!.response.value =
                        NetworkRequestResponse.loading<List<*>>()

                ServiceRepository(fragment.requireActivity().currentLocale.toLanguageTag()).getReviews(
                    pageNumber = if (isInitialLoad) 1 else params?.key,
                    itemsPerPage = itemsPerPage,
                    serviceId = viewModel.itemId,
                ).observe(fragment, {
                    initCallback?.onResult((it.data?.data ?: listOf()) as List<T>, null, 2)
                    callback?.onResult((it.data?.data ?: listOf()) as List<T>, pageKey)

                    if (isInitialLoad)
                        viewModel.reviewsLcee.value!!.response.value = it

                    handleErrors(it.status, it.apiErrorStatus)
                })
            }
            fragment is MyWalletFragment -> {
                val viewModel = fragment.viewModel

                if (isInitialLoad)
                    viewModel.transactionLcee.value!!.response.value =
                        NetworkRequestResponse.loading<List<*>>()

                WalletRepository(fragment.requireActivity().currentLocale.toLanguageTag()).getTransactions(
                    pageNumber = if (isInitialLoad) 1 else params?.key,
                    itemsPerPage = itemsPerPage,
                    tokenId = viewModel.tokenId
                ).observe(fragment, {
                    initCallback?.onResult((it.data ?: listOf()) as List<T>, null, 2)
                    callback?.onResult((it.data ?: listOf()) as List<T>, pageKey)

                    if (isInitialLoad)
                        viewModel.transactionLcee.value!!.response.value = it

                    handleErrors(it.status, it.apiErrorStatus)
                })
            }
            fragment is FavouritesFragment -> {
                val viewModel = fragment.viewModel

                if (isInitialLoad)
                    viewModel.productsLcee.value!!.response.value =
                        NetworkRequestResponse.loading<List<*>>()

                ProductRepository(fragment.requireActivity().currentLocale.toLanguageTag()).getWishList(
                    tokenId = viewModel.tokenId,
                    pageNumber = if (isInitialLoad) 1 else params?.key,
                    itemsPerPage = itemsPerPage,
                ).observe(fragment, {
                    initCallback?.onResult((it.data ?: listOf()) as List<T>, null, 2)
                    callback?.onResult((it.data ?: listOf()) as List<T>, pageKey)

                    if (isInitialLoad)
                        viewModel.productsLcee.value!!.response.value = it

                    handleErrors(it.status, it.apiErrorStatus)
                })
            }
            fragment is MyOrdersFragment -> {
                val viewModel = fragment.viewModel

                if (isInitialLoad)
                    viewModel.ordersLcee.value!!.response.value =
                        NetworkRequestResponse.loading<List<*>>()

                OrderRepository(fragment.requireActivity().currentLocale.toLanguageTag()).getMyOrders(
                    tokenId = viewModel.tokenId,
                    pageNumber = if (isInitialLoad) 1 else params?.key,
                    itemsPerPage = itemsPerPage,
                ).observe(fragment, {
                    initCallback?.onResult((it.data ?: listOf()) as List<T>, null, 2)
                    callback?.onResult((it.data ?: listOf()) as List<T>, pageKey)

                    if (isInitialLoad)
                        viewModel.ordersLcee.value!!.response.value = it

                    handleErrors(it.status, it.apiErrorStatus)
                })
            }
            fragment is ServicesFragment -> {
                val viewModel = fragment.viewModel

                if (isInitialLoad)
                    viewModel.servicesLcee.value!!.response.value =
                        NetworkRequestResponse.loading<List<*>>()

                ServiceRepository(fragment.requireActivity().currentLocale.toLanguageTag()).getServices(
                    tokenId = null,
                    categoryId = null,
                    minStarts = null,
                    pageNumber = if (isInitialLoad) 1 else params?.key,
                    itemsPerPage = itemsPerPage,
                ).observe(fragment, {
                    initCallback?.onResult((it.data ?: listOf()) as List<T>, null, 2)
                    callback?.onResult((it.data ?: listOf()) as List<T>, pageKey)

                    if (isInitialLoad)
                        viewModel.servicesLcee.value!!.response.value = it

                    handleErrors(it.status, it.apiErrorStatus)
                })
            }
        }
    }

    private fun handleErrors(
        status: ProjectConstant.Companion.Status,
        apiErrorStatus: ApiConstant.Status?
    ) {
        if (status == ProjectConstant.Companion.Status.API_ERROR)
            ProjectDialogUtils.showSimpleMessage(
                fragment.requireContext(),
                ApiConstant.Status.getMessageResId(apiErrorStatus),
                drawableResId = R.drawable.ic_secure_shield
            )
    }
}