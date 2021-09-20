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
                minStars = minStars,
                fromPrice = fromPrice,
                toPrice = toPrice,
                pageNumber = pageNumber,
                itemsPerPage = itemsPerPage,
                searchText = searchText,
            )
        }))
    }

    fun getLatestOffers(
        pageNumber: Int?,
        itemsPerPage: Int?,
        shopId: String?
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.getLatestOffers(
                pageNumber = pageNumber,
                itemsPerPage = itemsPerPage,
                shopId = shopId
            )
        }))
    }

    fun getLatestProducts(
        pageNumber: Int?,
        itemsPerPage: Int?,
        shopId: String?
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.getLatestProducts(
                pageNumber = pageNumber,
                itemsPerPage = itemsPerPage,
                shopId = shopId
            )
        }))
    }

    fun getBestSelling(
        pageNumber: Int?,
        itemsPerPage: Int?,
        shopId: String?
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.getBestSelling(
                pageNumber = pageNumber,
                itemsPerPage = itemsPerPage,
                shopId = shopId
            )
        }))
    }

    fun getSliderOffers(
        tokenId: String?,
        categoryId: String?,
        shopId: String?,
        type: Int,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.getSliderOffers(
                tokenId = tokenId,
                categoryId = categoryId,
                shopId = shopId,
                type = type
            )
        }))
    }

    fun getProductDetails(
        tokenId: String?,
        productId: String?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.getProductDetails(
                tokenId = tokenId,
                productId = productId,
            )
        }))
    }

    fun getReviews(
        productId: String?,
        pageNumber: Int?,
        itemsPerPage: Int?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.getProductReviews(
                productId = productId,
                pageNumber = pageNumber,
                itemsPerPage = itemsPerPage,
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

    fun getWishList(
        tokenId: String?,
        itemsPerPage: Int?,
        pageNumber: Int?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.getWishList(
                tokenId = tokenId,
                itemsPerPage = itemsPerPage,
                pageNumber = pageNumber,
            )
        }))
    }

    fun getCost(
        productId: String?,
        variations: List<String>?
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.getProductCost(
                productId = productId,
                variations = ArrayList(variations ?: listOf())
            )
        }))
    }
}