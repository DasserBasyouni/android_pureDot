package com.g7.soft.pureDot.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ZipCodeModel(
    @Json(name = "id") val id: Int?,
    @Json(name = "code") val code: String? = null
)