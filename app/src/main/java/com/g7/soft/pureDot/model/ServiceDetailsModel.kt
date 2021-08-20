package com.g7.soft.pureDot.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ServiceDetailsModel(
    @Json(name = "includingDescription") val includingDescription: String?,
    @Json(name = "variations") val variations: List<ServiceVariationModel>?,
    @Json(name = "description") val description: String?,
    @Json(name = "similarItems") val similarItems: List<ServiceModel>?,
    @Json(name = "quantityInCart") val quantityInCart: Int?,
    @Json(name = "images") val images: List<String>?,
    @Json(name = "reviews") val reviews: DataWithCountModel<List<ReviewModel>>?,
    @Json(name = "maxServants") val maxServants: Int?,
) {
}