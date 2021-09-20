package com.g7.soft.pureDot.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProductDetailsModel(
    @Json(name = "branches") val branches: List<IdNameModel>?,
    @Json(name = "variations") val variations: List<ProductVariationModel>?,
    @Json(name = "description") val description: String?,
    @Json(name = "similarProducts") val similarProducts: List<ProductModel>?,
    //@Json(name = "quantityInCart") val quantityInCart: Int?,
    @Json(name = "imagesUrls") val images: List<String>?,
    @Json(name = "reviews") val reviews: DataWithCountModel<List<ReviewModel>>?,
    @Json(name = "userReview") var userReview: ReviewModel?,

    // OrderModel
    @Json(name = "id") val id: String?,
    @Json(name = "name") val name: String? = null,
    @Json(name = "imageUrl") val imageUrl: String? = null,
    @Json(name = "shop") val shop: StoreModel? = null,
    @Json(name = "isAvailable") val isAvailable: Boolean? = true, // receiving this in getWishList only so default value will be true for UI
    @Json(name = "discountPercentage") val discountPercentage: Float? = null,
    @Json(name = "isPercentageDiscount") val isPercentageDiscount: Boolean? = null,
    @Json(name = "isInWishList") var isInWishList: Boolean? = null, // receiving this in checkCartProducts only
    @Json(name = "categoryId") val categoryId: String? = null,
)