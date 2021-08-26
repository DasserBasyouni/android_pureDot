package com.g7.soft.pureDot.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SliderOfferModel(
    @Json(name = "id") val id: Int?,
    @Json(name = "imageUrl") val imageUrl: String?,
    @Json(name = "redirectId") val redirectId: Int?,
    @Json(name = "redirectType") val redirectType: Int?
)