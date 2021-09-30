package com.g7.soft.pureDot.data

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.constant.ApiConstant
import com.g7.soft.pureDot.constant.ProjectConstant
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.OrderRepository
import com.g7.soft.pureDot.repo.WalletRepository
import com.g7.soft.pureDot.ui.screen.home.HomeFragment
import com.g7.soft.pureDot.ui.screen.myOrders.MyOrdersFragment
import com.g7.soft.pureDot.ui.screen.myWallet.MyWalletFragment
import com.g7.soft.pureDot.utils.ProjectDialogUtils
import com.zeugmasolutions.localehelper.currentLocale
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

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
        Log.e("Z_", "loadInitial")
        fragment.lifecycleScope.launch {
            try {
                fetchData(initCallback = callback, fragment = fragment, pageKey = 2)
            } catch (exception: Exception) {
                Timber.e("Failed to fetch data!")
            }

        }

    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {
        Log.e("Z_", "loadAfter")
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
        Log.e("Z_", "loadBefore")
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
            fragment is MyOrdersFragment -> {
                val viewModel = fragment.viewModel
                val filterViewModel = fragment.filterViewModel

                if (isInitialLoad)
                    viewModel.ordersLcee.value!!.response.value =
                        NetworkRequestResponse.loading<List<*>>()

                var fromDateTimestamp: Timestamp? = null
                try {
                    val dateFormat =
                        SimpleDateFormat(fragment.getString(R.string.format_standard_date))
                    val parsedDate: Date = dateFormat.parse(filterViewModel.fromDate.value)
                    fromDateTimestamp = Timestamp(parsedDate.time)
                } catch (e: Exception) {
                }

                var toDateTimestamp: Timestamp? = null
                try {
                    val dateFormat =
                        SimpleDateFormat(fragment.getString(R.string.format_standard_date))
                    val parsedDate: Date = dateFormat.parse(filterViewModel.fromDate.value)
                    toDateTimestamp = Timestamp(parsedDate.time)
                } catch (e: Exception) {
                }

                OrderRepository(fragment.requireActivity().currentLocale.toLanguageTag()).getMyOrders(
                    tokenId = viewModel.tokenId,
                    pageNumber = if (isInitialLoad) 1 else params?.key,
                    itemsPerPage = itemsPerPage,
                    fromDate = fromDateTimestamp?.time?.div(1000),
                    toDate = toDateTimestamp?.time?.div(1000),
                    customerName = filterViewModel.customerName.value,
                    orderNumber = filterViewModel.orderNumber.value?.toIntOrNull(),
                    status = filterViewModel.selectedStatus?.value,
                ).observe(fragment, {
                    initCallback?.onResult((it.data ?: listOf()) as List<T>, null, 2)
                    callback?.onResult((it.data ?: listOf()) as List<T>, pageKey)

                    if (isInitialLoad)
                        viewModel.ordersLcee.value!!.response.value = it

                    handleErrors(it.status, it.apiErrorStatus)
                })
            }
            fragment is HomeFragment && fragment.viewModel.areNewOrdersSelected.value == true -> {
                val viewModel = fragment.viewModel

                if (isInitialLoad)
                    viewModel.newOrdersLcee.value!!.response.value =
                        NetworkRequestResponse.loading<List<*>>()

                OrderRepository(fragment.requireActivity().currentLocale.toLanguageTag()).getNewOrders(
                    tokenId = viewModel.tokenId,
                    pageNumber = if (isInitialLoad) 1 else params?.key,
                    itemsPerPage = itemsPerPage,
                ).observe(fragment, {
                    initCallback?.onResult((it.data ?: listOf()) as List<T>, null, 2)
                    callback?.onResult((it.data ?: listOf()) as List<T>, pageKey)

                    if (isInitialLoad)
                        viewModel.newOrdersLcee.value!!.response.value = it

                    handleErrors(it.status, it.apiErrorStatus)
                })
            }
            fragment is HomeFragment && fragment.viewModel.areNewOrdersSelected.value == false -> {
                val viewModel = fragment.viewModel

                if (isInitialLoad)
                    viewModel.pendingOrdersLcee.value!!.response.value =
                        NetworkRequestResponse.loading<List<*>>()

                OrderRepository(fragment.requireActivity().currentLocale.toLanguageTag()).getPendingOrders(
                    tokenId = viewModel.tokenId,
                    pageNumber = if (isInitialLoad) 1 else params?.key,
                    itemsPerPage = itemsPerPage,
                ).observe(fragment, {
                    initCallback?.onResult((it.data ?: listOf()) as List<T>, null, 2)
                    callback?.onResult((it.data ?: listOf()) as List<T>, pageKey)

                    if (isInitialLoad)
                        viewModel.pendingOrdersLcee.value!!.response.value = it

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