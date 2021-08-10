package com.g7.soft.pureDot.data

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

class ReversedPaginationDataSource<T>(
    private val fragment: Fragment,
    private val itemsPerPage: Int
) : PageKeyedDataSource<Int, T>() {

    companion object {
        fun <T> initializedPagedListBuilder(
            config: PagedList.Config,
            fragment: Fragment
        ): LivePagedListBuilder<Int, T> {
            val dataSourceFactory = object : DataSource.Factory<Int, T>() {
                override fun create(): DataSource<Int, T> {
                    return ReversedPaginationDataSource(
                        fragment = fragment,
                        itemsPerPage = config.pageSize
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
                    pageKey = --pageNumber
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
                    pageKey = ++pageNumber
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
        pageKey: Int
    ) = coroutineScope {
        val isInitialLoad = params == null

        if (params == null || params.key != 0)
            when (fragment) {
                // todo
            }
    }
}