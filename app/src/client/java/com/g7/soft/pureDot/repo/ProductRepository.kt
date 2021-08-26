package com.g7.soft.pureDot.repo

import androidx.lifecycle.liveData
import com.g7.soft.pureDot.network.Fetcher
import com.g7.soft.pureDot.network.NetworkRequestHandler
import kotlinx.coroutines.Dispatchers

class ProductRepository(private val langTag: String) {

    fun getProducts(
        categoriesIds: List<String>?,
        storesIds: List<String>?,
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
        shopId: String?
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.getLatestOffers(
                pageNumber = pageNumber,
                itemPerPage = itemPerPage,
                shopId = shopId
            )
        }))
    }

    fun getLatestProducts(
        pageNumber: Int?,
        itemPerPage: Int?,
        shopId: String?
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.getLatestProducts(
                pageNumber = pageNumber,
                itemPerPage = itemPerPage,
                shopId = shopId
            )
        }))
    }

    fun getBestSelling(
        pageNumber: Int?,
        itemPerPage: Int?,
        shopId: String?
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.getBestSelling(
                pageNumber = pageNumber,
                itemPerPage = itemPerPage,
                shopId = shopId
            )
        }))
    }

    fun getSliderOffers(
        categoryId: String?,
        shopId: String?,
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
        productId: String?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.getProductDetails(
                productId = productId,
            )
        }))
    }

    fun getItemReviews(
        itemId: String?,
        tokenId: String?,
        pageNumber: Int?,
        itemsPerPage: Int?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.getProductReviews(
                itemId = itemId,
                tokenId = tokenId,
                pageNumber = pageNumber,
                itemPerPage = itemsPerPage,
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
            return@handle Fetcher().getInstance(langTag)?.addProductReview(
                tokenId = tokenId,
                productId = productId,
                rating = rating,
                comment = comment
            )
        }))
    }

    fun editWishList(
        tokenId: String?,
        productId: String?,
        doAdd: Boolean?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.editWishList(
                tokenId = tokenId,
                productId = productId,
                doAdd = doAdd,
            )
        }))
    }
}