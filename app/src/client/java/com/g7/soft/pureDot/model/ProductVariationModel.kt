package com.g7.soft.pureDot.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProductVariationModel(
    @Json(name = "title") val title: String?,
    @Json(name = "values") val values: List<IdValueModel>?,
    @Json(name = "isColor") val isColor: Boolean?,
)