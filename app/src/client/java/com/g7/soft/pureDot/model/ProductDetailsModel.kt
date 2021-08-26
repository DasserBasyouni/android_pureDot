package com.g7.soft.pureDot.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProductDetailsModel(
    @Json(name = "branches") val branches: List<BranchModel>?,
    @Json(name = "variations") val variations: List<ProductVariationModel>?,
    @Json(name = "description") val description: String?,
    @Json(name = "similarProducts") val similarProducts: List<ProductModel>?,
    @Json(name = "quantityInCart") val quantityInCart: Int?,
    @Json(name = "imagesUrls") val images: List<String>?,
    @Json(name = "reviews") val reviews: DataWithCountModel<List<ReviewModel>>?,
    @Json(name = "userReview") var userReview: ReviewModel?,
)