package com.g7.soft.pureDot.repo

import com.g7.soft.pureDot.network.Fetcher
import com.g7.soft.pureDot.network.NetworkRequestHandler

open class OrderRepositoryFlavour(private val langTag: String) {

    fun getMyOrders(
        tokenId: String?,
        itemsPerPage: Int?,
        pageNumber: Int?,
    ) = androidx.lifecycle.liveData(kotlinx.coroutines.Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.getMyOrders(
                tokenId = tokenId,
                itemsPerPage = itemsPerPage,
                pageNumber = pageNumber,
            )
        }))
    }

    fun getNewOrders(
        tokenId: String?,
        itemsPerPage: Int?,
        pageNumber: Int?,
    ) = androidx.lifecycle.liveData(kotlinx.coroutines.Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.getNewOrders(
                tokenId = tokenId,
                itemsPerPage = itemsPerPage,
                pageNumber = pageNumber,
            )
        }))
    }

    fun getPendingOrders(
        tokenId: String?,
        itemsPerPage: Int?,
        pageNumber: Int?,
    ) = androidx.lifecycle.liveData(kotlinx.coroutines.Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.getPendingOrders(
                tokenId = tokenId,
                itemsPerPage = itemsPerPage,
                pageNumber = pageNumber,
            )
        }))
    }

}