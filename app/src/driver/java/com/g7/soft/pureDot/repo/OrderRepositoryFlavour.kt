package com.g7.soft.pureDot.repo

import androidx.lifecycle.liveData
import com.g7.soft.pureDot.network.Fetcher
import com.g7.soft.pureDot.network.NetworkRequestHandler
import kotlinx.coroutines.Dispatchers

open class OrderRepositoryFlavour(private val langTag: String) {

    fun getPendingOrders(
        tokenId: String?,
        itemsPerPage: Int?,
        pageNumber: Int?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.getPendingOrders(
                tokenId = tokenId,
                itemsPerPage = itemsPerPage,
                pageNumber = pageNumber,
            )
        }))
    }

    fun getMyOrders(
        tokenId: String?,
        itemsPerPage: Int?,
        pageNumber: Int?,
    ) =
        liveData(Dispatchers.IO) {
            emitSource(NetworkRequestHandler().handle(request = {
                return@handle Fetcher().getInstance(langTag)?.getMyOrders(
                    tokenId = tokenId,
                    itemsPerPage = itemsPerPage,
                    pageNumber = pageNumber,
                )
            }))
        }

    fun getMasterOrder(tokenId: String?, id: String?) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.getMasterOrder(tokenId = tokenId, id = id)
        }))
    }

    suspend fun setCurrentLocation(
        tokenId: String?,
        latitude: Double?,
        longitude: Double?,
        areaName: String?
    ) = Fetcher().getInstance(langTag)?.setCurrentLocation(
        tokenId = tokenId,
        latitude = latitude,
        longitude = longitude,
        areaName = areaName
    )
}