package com.g7.soft.pureDot.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ServiceDetailsModel(
    // service model
    @Json(name = "id") val id: String?,
    @Json(name = "name") val name: String?,
    @Json(name = "price") val price: Double?,
    @Json(name = "shop") val shop: StoreModel?,
    @Json(name = "imageUrl") val imageUrl: String?,
    @Json(name = "shortDescription") val shortDescription: String?,

    // service details
    @Json(name = "variations") val variations: List<ServiceVariationModel>?,
    @Json(name = "fullDescription") val fullDescription: String?,
    @Json(name = "similarServices") val similarServices: List<ServiceModel>?,
    @Json(name = "imagesUrls") val images: List<String>?,
    @Json(name = "reviews") val reviews: DataWithCountModel<List<ReviewModel>>?,
    @Json(name = "maxServants") val maxServants: Int?,
    @Json(name = "userReview") var userReview: ReviewModel? = null,
    @Json(name = "branches") val branches: List<IdNameModel>?,
)