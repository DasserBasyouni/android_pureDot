package com.g7.soft.pureDot.repo

import androidx.lifecycle.liveData
import com.g7.soft.pureDot.network.Fetcher
import com.g7.soft.pureDot.network.NetworkRequestHandler
import kotlinx.coroutines.Dispatchers

class ServiceRepository(private val langTag: String) {

    fun getServices(
        tokenId: String?,
        categoryId: String?,
        minStarts: List<Int>?,
        pageNumber: Int?,
        itemsPerPage: Int?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.getServices(
                tokenId = tokenId,
                categoryId = categoryId,
                minStars = minStarts,
                pageNumber = pageNumber,
                itemsPerPage = itemsPerPage
            )
        }))
    }

    fun getComplainComments(
        tokenId: String?,
        rating: Float?,
        message: String?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.getComplainComments(
                tokenId = tokenId,
                rating = rating,
                message = message,
            )
        }))
    }


    fun getServiceDetails(
        serviceId: String?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.getServiceDetails(
                serviceId = serviceId,
            )
        }))
    }


    fun addReview(
        tokenId: String?,
        productId: String?,
        rating: Float?,
        comment: String?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.addServiceReview(
                tokenId = tokenId,
                productId = productId,
                rating = rating,
                comment = comment
            )
        }))
    }
}