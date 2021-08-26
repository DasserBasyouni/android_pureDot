package com.g7.soft.pureDot.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ServiceDetailsModel(
    //@Json(name = "includingDescription") val includingDescription: String?,
    @Json(name = "variations") val variations: List<ServiceVariationModel>?,
    @Json(name = "fullDescription") val fullDescription: String?,
    @Json(name = "similarServices") val similarServices: List<ServiceModel>?,
    //@Json(name = "quantityInCart") val quantityInCart: Int?,
    @Json(name = "imagesUrls") val images: List<String>?,
    @Json(name = "reviews") val reviews: DataWithCountModel<List<ReviewModel>>?,
    @Json(name = "maxServants") val maxServants: Int?,
    @Json(name = "userReview") var userReview: ReviewModel? = null,
) {
}