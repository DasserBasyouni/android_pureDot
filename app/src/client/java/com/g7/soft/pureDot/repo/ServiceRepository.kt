package com.g7.soft.pureDot.repo

import androidx.lifecycle.liveData
import com.g7.soft.pureDot.model.ServiceVariationValueModel
import com.g7.soft.pureDot.network.Fetcher
import com.g7.soft.pureDot.network.NetworkRequestHandler
import kotlinx.coroutines.Dispatchers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

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
        rating: Int?,
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
        tokenId: String?,
        serviceId: String?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.getServiceDetails(
                tokenId = tokenId,
                serviceId = serviceId,
            )
        }))
    }


    fun addReview(
        tokenId: String?,
        productId: String?,
        rating: Int?,
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

    fun getReviews(
        serviceId: String?,
        pageNumber: Int?,
        itemsPerPage: Int?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.getServiceReviews(
                productId = serviceId,
                pageNumber = pageNumber,
                itemsPerPage = itemsPerPage,
            )
        }))
    }

    fun getCost(
        serviceId: String?,
        servants: Int?,
        variations: List<ServiceVariationValueModel>?
    ) = liveData(Dispatchers.IO) {
        val jsonObject = JSONObject()
        jsonObject.put("serviceId", serviceId)
        jsonObject.put("servants", servants)
        jsonObject.put("variations", JSONArray().apply {
            for (variation in variations ?: listOf())
                this.put(JSONObject().apply {
                    this.put("stockId", variation.stockId)
                    this.put("sourceId", variation.sourceId)
                    this.put("productId", variation.productId)
                })
        })
        val body = jsonObject.toString()
            .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.getServiceCost(body)
        }))
    }
}