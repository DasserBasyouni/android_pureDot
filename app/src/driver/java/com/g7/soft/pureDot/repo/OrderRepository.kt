package com.g7.soft.pureDot.repo

import androidx.lifecycle.liveData
import com.g7.soft.pureDot.network.Fetcher
import com.g7.soft.pureDot.network.NetworkRequestHandler
import kotlinx.coroutines.Dispatchers

class OrderRepository(private val langTag: String) {

    fun getPendingOrders(
        tokenId: String?,
        lat: Double?,
        lng: Double?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.getPendingOrders(
                tokenId = tokenId,
                lat = lat,
                lng = lng,

                )
        }))
    }

    fun cancelOrder(
        tokenId: String?,
        orderNumber: Int?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.cancelOrder(
                tokenId = tokenId,
                orderNumber = orderNumber,
            )
        }))
    }


    fun getMyOrders(tokenId: String?) =
        liveData(Dispatchers.IO) {
            emitSource(NetworkRequestHandler().handle(request = {
                return@handle Fetcher().getInstance(langTag)?.getMyOrders(
                    tokenId = tokenId,
                )
            }))
        }

    fun changeOrderStatus(
        tokenId: String?,
        orderNumber: Int?,
        status: Int?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.changeOrderStatus(
                tokenId = tokenId,
                orderNumber = orderNumber,
                status = status
            )
        }))
    }
}