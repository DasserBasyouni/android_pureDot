package com.g7.soft.pureDot.repo

import androidx.lifecycle.liveData
import com.g7.soft.pureDot.network.Fetcher
import com.g7.soft.pureDot.network.NetworkRequestHandler
import kotlinx.coroutines.Dispatchers

class CategoriesRepository(private val langTag: String) {

    fun getCategories(
        pageNumber: Int?,
        itemsPerPage: Int?,
        searchText: String?,
        shopId: String?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.getCategories(
                pageNumber = pageNumber,
                itemPerPage = itemsPerPage,
                searchText = searchText,
                shopId = shopId
            )
        }))
    }

}