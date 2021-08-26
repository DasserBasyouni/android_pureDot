package com.g7.soft.pureDot.repo

import androidx.lifecycle.liveData
import com.g7.soft.pureDot.network.Fetcher
import com.g7.soft.pureDot.network.NetworkRequestHandler
import kotlinx.coroutines.Dispatchers

class StoreRepository(private val langTag: String) {

    fun getAll(
        pageNumber: Int?,
        itemsPerPage: Int?,
        searchText: String?,
        categoryId: String?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.getALlStores(
                pageNumber = pageNumber,
                itemPerPage = itemsPerPage,
                searchText = searchText,
                categoryId = categoryId
            )
        }))
    }

}