package com.g7.soft.pureDot.repo

import androidx.lifecycle.liveData
import com.g7.soft.pureDot.network.Fetcher
import com.g7.soft.pureDot.network.NetworkRequestHandler
import kotlinx.coroutines.Dispatchers

class ProductRepository(private val langTag: String) {

    fun getProducts(
        categoriesIds: List<Int>?,
        storesIds: List<Int>?,
        minStars: List<Int>?,
        fromPrice: Int?,
        toPrice: Int?,
        pageNumber: Int?,
        itemsPerPage: Int?,
        searchText: String?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.getProducts(
                categoriesIds = categoriesIds,
                storesIds = storesIds,
                minStarts = minStars,
                fromPrice = fromPrice,
                toPrice = toPrice,
                pageNumber = pageNumber,
                itemPerPage = itemsPerPage,
                searchText = searchText,
            )
        }))
    }

    fun getLatestOffers(
        pageNumber: Int?,
        itemPerPage: Int?,
        searchText: String?,
        shopId: Int?
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.getLatestOffers(
                pageNumber = pageNumber,
                itemPerPage = itemPerPage,
                searchText = searchText,
                shopId = shopId
            )
        }))
    }

    fun getLatestProducts(
        pageNumber: Int?,
        itemPerPage: Int?,
        searchText: String?,
        shopId: Int?
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.getLatestProducts(
                pageNumber = pageNumber,
                itemPerPage = itemPerPage,
                searchText = searchText,
                shopId = shopId
            )
        }))
    }

    fun getBestSelling(
        pageNumber: Int?,
        itemPerPage: Int?,
        searchText: String?,
        shopId: Int?
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.getBestSelling(
                pageNumber = pageNumber,
                itemPerPage = itemPerPage,
                searchText = searchText,
                shopId = shopId
            )
        }))
    }

    fun getSliderOffers(
        categoryId: Int?,
        shopId: Int?,
        type: Int,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.getSliderOffers(
                categoryId = categoryId,
                shopId = shopId,
                type = type
            )
        }))
    }

    fun getProductDetails(
        productId: Int?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.getProductDetails(
                productId = productId,
            )
        }))
    }

    fun getItemReviews(
        itemId: Int?,
        tokenId: String?,
        pageNumber: Int?,
        itemsPerPage: Int?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.getItemReviews(
                itemId = itemId,
                tokenId = tokenId,
                pageNumber = pageNumber,
                itemPerPage = itemsPerPage,
            )
        }))
    }

    fun addReview(
        tokenId: String?,
        productId: Int?,
        rating: Float?,
        comment: String?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.addProductReview(
                tokenId = tokenId,
                productId = productId,
                rating = rating,
                comment = comment
            )
        }))
    }

}