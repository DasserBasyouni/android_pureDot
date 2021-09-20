package com.g7.soft.pureDot.model

import com.g7.soft.pureDot.constant.ApiConstant
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SliderOfferModel(
    @Json(name = "id") val id: String?,
    @Json(name = "imageUrl") val imageUrl: String?,
    @Json(name = "redirectId") val redirectId: String?,
    @Json(name = "redirectType") val redirectType: Int?
) {
    val redirectTypeEnum get() = ApiConstant.RedirectType.fromInt(redirectType)
}