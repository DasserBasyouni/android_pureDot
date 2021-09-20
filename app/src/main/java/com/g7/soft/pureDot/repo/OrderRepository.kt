package com.g7.soft.pureDot.repo

import androidx.lifecycle.liveData
import com.g7.soft.pureDot.network.Fetcher
import com.g7.soft.pureDot.network.NetworkRequestHandler
import kotlinx.coroutines.Dispatchers

class OrderRepository(private val langTag: String) : OrderRepositoryFlavour(langTag) {

    fun changeOrderStatus(
        tokenId: String?,
        orderId: String?,
        status: Int?,
        isReturn: Boolean?
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.changeOrderStatus(
                tokenId = tokenId,
                orderId = orderId,
                status = status,
                isReturn = isReturn
            )
        }))
    }
}