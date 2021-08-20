package com.g7.soft.pureDot.repo

import com.g7.soft.pureDot.network.Fetcher
import com.g7.soft.pureDot.network.NetworkRequestHandler

class OrderRepository(private val langTag: String) {

    fun getMyOrders(
        tokenId: String?,
    ) = androidx.lifecycle.liveData(kotlinx.coroutines.Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.getMyOrders(
                tokenId = tokenId,
            )
        }))
    }

    fun trackOrder(
        tokenId: String?,
        orderNumber: Int?,
    ) = androidx.lifecycle.liveData(kotlinx.coroutines.Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.trackOrder(
                tokenId = tokenId,
                orderNumber = orderNumber,
            )
        }))
    }

    fun cancelOrder(
        tokenId: String?,
        orderNumber: Int?,
    ) = androidx.lifecycle.liveData(kotlinx.coroutines.Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.cancelOrder(
                tokenId = tokenId,
                orderNumber = orderNumber,
            )
        }))
    }

    fun rateOrder(
        tokenId: String?,
        orderNumber: Int?,
        orderRating: Float?,
        orderComment: String?,
        deliveryRating: Float?,
        deliveryComment: String?,
    ) = androidx.lifecycle.liveData(kotlinx.coroutines.Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.rateOrder(
                tokenId = tokenId,
                orderNumber = orderNumber,
                orderRating = orderRating,
                orderComment = orderComment,
                deliveryRating = deliveryRating,
                deliveryComment = deliveryComment,
            )
        }))
    }

    fun calculateRefundShipping(
        orderNumber: Int?,
        productId: Int?,
        shippingMethod: Int?,
    ) = androidx.lifecycle.liveData(kotlinx.coroutines.Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.calculateRefundShipping(
                orderNumber = orderNumber,
                productId = productId,
                shippingMethod = shippingMethod
            )
        }))
    }

    fun refund(
        orderNumber: Int?,
        productId: Int?,
        shippingMethod: Int?,
    ) = androidx.lifecycle.liveData(kotlinx.coroutines.Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.refund(
                orderNumber = orderNumber,
                productId = productId,
                shippingMethod = shippingMethod
            )
        }))
    }
}