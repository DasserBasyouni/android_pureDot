package com.g7.soft.pureDot.repo

import androidx.lifecycle.liveData
import com.g7.soft.pureDot.network.Fetcher
import com.g7.soft.pureDot.network.NetworkRequestHandler
import kotlinx.coroutines.Dispatchers

class CartRepository(private val langTag: String) {

    fun editCartQuantity(
        itemId: Int?,
        quantity: Int?,
        serviceDateTime: Long?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.editCartQuantity(
                itemId = itemId,
                quantity = quantity,
                serviceDateTime = serviceDateTime,
            )
        }))
    }

    fun getCartItems(
        tokenId: String?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.getCartItems(
                tokenId = tokenId,
            )
        }))
    }

    fun checkout(
        tokenId: String?,
        firstName: String?,
        lastName: String?,
        email: String?,
        phoneNumber: String?,
        addressId: Int?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.checkout(
                tokenId = tokenId,
                firstName = firstName,
                lastName = lastName,
                email = email,
                phoneNumber = phoneNumber,
                addressId = addressId,
            )
        }))
    }

    fun checkCoupon(
        tokenId: String?,
        coupon: String?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.checkCoupon(
                tokenId = tokenId,
                coupon = coupon,
            )
        }))
    }

    fun checkoutIsPaid(
        tokenId: String?,
        paidAmount: Double?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.checkoutIsPaid(
                tokenId = tokenId,
                paidAmount = paidAmount,
            )
        }))
    }

    fun getWishList(
        tokenId: String?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.getWishList(
                tokenId = tokenId,
            )
        }))
    }
}