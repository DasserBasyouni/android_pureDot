package com.g7.soft.pureDot.data

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.*
import com.g7.soft.pureDot.ui.screen.browse.BrowseFragment
import com.g7.soft.pureDot.ui.screen.category.CategoryFragment
import com.g7.soft.pureDot.ui.screen.favourite.FavouritesFragment
import com.g7.soft.pureDot.ui.screen.filter.FilterFragment
import com.g7.soft.pureDot.ui.screen.myWallet.MyWalletFragment
import com.g7.soft.pureDot.ui.screen.seeAll.categories.AllCategoriesFragment
import com.g7.soft.pureDot.ui.screen.seeAll.products.AllProductsFragment
import com.g7.soft.pureDot.ui.screen.seeAll.reviews.AllReviewsFragment
import com.g7.soft.pureDot.ui.screen.seeAll.stores.AllStoresFragment
import com.g7.soft.pureDot.ui.screen.services.ServicesFragment
import com.g7.soft.pureDot.ui.screen.store.StoreFragment
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


    private var pageNumber = 1


    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, T>
    ) {
        fragment.lifecycleScope.launch {
            try {
                fetchData(initCallback = callback, fragment = fragment, pageKey = ++pageNumber)
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
                    pageKey = ++pageNumber
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
                    pageKey = --pageNumber
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

        if (params == null || params.key != 0)
            when {
                fragment is FilterFragment && isFirstPagedList -> {
                    val viewModel = fragment.viewModel

                    if (isInitialLoad)
                        viewModel.categoriesLcee.value!!.response.value =
                            NetworkRequestResponse.loading<List<*>>()

                    CategoriesRepository(fragment.requireActivity().currentLocale.toLanguageTag()).getCategories(
                        pageNumber = pageNumber,
                        itemsPerPage = itemsPerPage,
                        searchText = null,
                        shopId = null,
                    ).observe(fragment, {
                        initCallback?.onResult((it.data?.data ?: listOf()) as List<T>, 0, pageKey)
                        callback?.onResult((it.data?.data ?: listOf()) as List<T>, pageKey)

                        if (isInitialLoad)
                            viewModel.categoriesLcee.value!!.response.value = it
                    })
                }
                fragment is FilterFragment && !isFirstPagedList -> {
                    val viewModel = fragment.viewModel

                    if (isInitialLoad)
                        viewModel.storesLcee.value!!.response.value =
                            NetworkRequestResponse.loading<List<*>>()

                    // todo  searchText = null, categoryId = null?
                    StoreRepository(fragment.requireActivity().currentLocale.toLanguageTag()).getAll(
                        pageNumber = pageNumber,
                        itemsPerPage = itemsPerPage,
                        searchText = null,
                        categoryId = null
                    ).observe(fragment, {
                        initCallback?.onResult((it.data?.data ?: listOf()) as List<T>, 0, pageKey)
                        callback?.onResult((it.data?.data ?: listOf()) as List<T>, pageKey)

                        if (isInitialLoad)
                            viewModel.storesLcee.value!!.response.value = it
                    })
                }
                fragment is AllProductsFragment -> {
                    val viewModel = fragment.viewModel

                    if (isInitialLoad)
                        viewModel.productsLcee.value!!.response.value =
                            NetworkRequestResponse.loading<List<*>>()

                    // todo use filter values with saving them locally maybe
                    ProductRepository(fragment.requireActivity().currentLocale.toLanguageTag()).getProducts(
                        pageNumber = pageNumber,
                        itemsPerPage = itemsPerPage,
                        searchText = null,
                        categoriesIds = null,
                        fromPrice = null,
                        toPrice = null,
                        storesIds = null,
                        minStars = null,
                    ).observe(fragment, {
                        initCallback?.onResult((it.data?.data ?: listOf()) as List<T>, 0, pageKey)
                        callback?.onResult((it.data?.data ?: listOf()) as List<T>, pageKey)

                        if (isInitialLoad)
                            viewModel.productsLcee.value!!.response.value = it
                    })
                }
                fragment is AllCategoriesFragment -> {
                    val viewModel = fragment.viewModel

                    if (isInitialLoad)
                        viewModel.categoriesLcee.value!!.response.value =
                            NetworkRequestResponse.loading<List<*>>()

                    CategoriesRepository(fragment.requireActivity().currentLocale.toLanguageTag()).getCategories(
                        pageNumber = pageNumber,
                        itemsPerPage = itemsPerPage,
                        searchText = null,
                        shopId = null,
                    ).observe(fragment, {
                        initCallback?.onResult((it.data?.data ?: listOf()) as List<T>, 0, pageKey)
                        callback?.onResult((it.data?.data ?: listOf()) as List<T>, pageKey)

                        if (isInitialLoad)
                            viewModel.categoriesLcee.value!!.response.value = it
                    })
                }
                fragment is AllStoresFragment -> {
                    val viewModel = fragment.viewModel

                    if (isInitialLoad)
                        viewModel.storesLcee.value!!.response.value =
                            NetworkRequestResponse.loading<List<*>>()

                    // todo  searchText = null, categoryId = null?
                    StoreRepository(fragment.requireActivity().currentLocale.toLanguageTag()).getAll(
                        pageNumber = pageNumber,
                        itemsPerPage = itemsPerPage,
                        searchText = null,
                        categoryId = null
                    ).observe(fragment, {
                        initCallback?.onResult((it.data?.data ?: listOf()) as List<T>, 0, pageKey)
                        callback?.onResult((it.data?.data ?: listOf()) as List<T>, pageKey)

                        if (isInitialLoad)
                            viewModel.storesLcee.value!!.response.value = it
                    })
                }
                fragment is BrowseFragment && isFirstPagedList -> {
                    val viewModel = fragment.viewModel

                    if (isInitialLoad)
                        viewModel.categoriesLcee.value!!.response.value =
                            NetworkRequestResponse.loading<List<*>>()

                    CategoriesRepository(fragment.requireActivity().currentLocale.toLanguageTag()).getCategories(
                        pageNumber = pageNumber,
                        itemsPerPage = itemsPerPage,
                        searchText = null,
                        shopId = null,
                    ).observe(fragment, {
                        initCallback?.onResult((it.data?.data ?: listOf()) as List<T>, 0, pageKey)
                        callback?.onResult((it.data?.data ?: listOf()) as List<T>, pageKey)

                        if (isInitialLoad)
                            viewModel.categoriesLcee.value!!.response.value = it
                    })
                }
                fragment is BrowseFragment && !isFirstPagedList -> {
                    val viewModel = fragment.viewModel

                    if (isInitialLoad)
                        viewModel.storesLcee.value!!.response.value =
                            NetworkRequestResponse.loading<List<*>>()

                    // todo  searchText = null, categoryId = null?
                    StoreRepository(fragment.requireActivity().currentLocale.toLanguageTag()).getAll(
                        pageNumber = pageNumber,
                        itemsPerPage = itemsPerPage,
                        searchText = null,
                        categoryId = null
                    ).observe(fragment, {
                        initCallback?.onResult((it.data?.data ?: listOf()) as List<T>, 0, pageKey)
                        callback?.onResult((it.data?.data ?: listOf()) as List<T>, pageKey)

                        if (isInitialLoad)
                            viewModel.storesLcee.value!!.response.value = it
                    })
                }
                fragment is StoreFragment -> {
                    val viewModel = fragment.viewModel

                    if (isInitialLoad)
                        viewModel.categoriesLcee.value!!.response.value =
                            NetworkRequestResponse.loading<List<*>>()

                    CategoriesRepository(fragment.requireActivity().currentLocale.toLanguageTag()).getCategories(
                        pageNumber = pageNumber,
                        itemsPerPage = itemsPerPage,
                        searchText = null,
                        shopId = null,
                    ).observe(fragment, {
                        initCallback?.onResult((it.data?.data ?: listOf()) as List<T>, 0, pageKey)
                        callback?.onResult((it.data?.data ?: listOf()) as List<T>, pageKey)

                        if (isInitialLoad)
                            viewModel.categoriesLcee.value!!.response.value = it
                    })
                }
                fragment is CategoryFragment -> {
                    val viewModel = fragment.viewModel

                    if (isInitialLoad)
                        viewModel.productsLcee.value!!.response.value =
                            NetworkRequestResponse.loading<List<*>>()

                    // todo use filter values with saving them locally maybe
                    ProductRepository(fragment.requireActivity().currentLocale.toLanguageTag()).getProducts(
                        pageNumber = pageNumber,
                        itemsPerPage = itemsPerPage,
                        searchText = null,
                        categoriesIds = listOfNotNull(viewModel.category?.id),
                        fromPrice = null,
                        toPrice = null,
                        storesIds = null,
                        minStars = null,
                    ).observe(fragment, {
                        initCallback?.onResult((it.data?.data ?: listOf()) as List<T>, 0, pageKey)
                        callback?.onResult((it.data?.data ?: listOf()) as List<T>, pageKey)

                        if (isInitialLoad)
                            viewModel.productsLcee.value!!.response.value = it
                    })
                }
                fragment is AllReviewsFragment -> {
                    val viewModel = fragment.viewModel

                    if (isInitialLoad)
                        viewModel.reviewsLcee.value!!.response.value =
                            NetworkRequestResponse.loading<List<*>>()

                    ProductRepository(fragment.requireActivity().currentLocale.toLanguageTag()).getItemReviews(
                        pageNumber = pageNumber,
                        itemsPerPage = itemsPerPage,
                        itemId = viewModel.itemId,
                        tokenId = viewModel.tokenId
                    ).observe(fragment, {
                        initCallback?.onResult((it.data?.data ?: listOf()) as List<T>, 0, pageKey)
                        callback?.onResult((it.data?.data ?: listOf()) as List<T>, pageKey)

                        if (isInitialLoad)
                            viewModel.reviewsLcee.value!!.response.value = it
                    })
                }
                fragment is MyWalletFragment -> {
                    val viewModel = fragment.viewModel

                    if (isInitialLoad)
                        viewModel.transactionLcee.value!!.response.value =
                            NetworkRequestResponse.loading<List<*>>()

                    WalletRepository(fragment.requireActivity().currentLocale.toLanguageTag()).getTransactions(
                        pageNumber = pageNumber,
                        itemsPerPage = itemsPerPage,
                        tokenId = viewModel.tokenId
                    ).observe(fragment, {
                        initCallback?.onResult((it.data ?: listOf()) as List<T>, 0, pageKey)
                        callback?.onResult((it.data ?: listOf()) as List<T>, pageKey)

                        if (isInitialLoad)
                            viewModel.transactionLcee.value!!.response.value = it
                    })
                }
                fragment is FavouritesFragment -> {
                    val viewModel = fragment.viewModel

                    if (isInitialLoad)
                        viewModel.productsLcee.value!!.response.value =
                            NetworkRequestResponse.loading<List<*>>()

                    ProductRepository(fragment.requireActivity().currentLocale.toLanguageTag()).getProducts(
                        pageNumber = pageNumber,
                        itemsPerPage = itemsPerPage,
                        searchText = null,
                        categoriesIds = null,
                        fromPrice = null,
                        toPrice = null,
                        storesIds = null,
                        minStars = null,
                    ).observe(fragment, {
                        initCallback?.onResult((it.data?.data ?: listOf()) as List<T>, 0, pageKey)
                        callback?.onResult((it.data?.data ?: listOf()) as List<T>, pageKey)

                        if (isInitialLoad)
                            viewModel.productsLcee.value!!.response.value = it
                    })
                }
                fragment is ServicesFragment -> {
                    val viewModel = fragment.viewModel

                    if (isInitialLoad)
                        viewModel.servicesLcee.value!!.response.value =
                            NetworkRequestResponse.loading<List<*>>()

                    ServiceRepository(fragment.requireActivity().currentLocale.toLanguageTag()).getServices(
                        pageNumber = pageNumber,
                        itemsPerPage = itemsPerPage,
                        categoryId = null,
                        tokenId = null, // todo
                        fromPrice = null,
                        toPrice = null,
                        shopId = null,
                        minStarts = null,
                    ).observe(fragment, {
                        initCallback?.onResult((it.data ?: listOf()) as List<T>, 0, pageKey)
                        callback?.onResult((it.data ?: listOf()) as List<T>, pageKey)

                        if (isInitialLoad)
                            viewModel.servicesLcee.value!!.response.value = it
                    })
                }
            }
    }
}