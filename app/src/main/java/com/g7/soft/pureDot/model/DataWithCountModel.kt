package com.g7.soft.pureDot.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DataWithCountModel<out T>(
    @Json(name = "totalCount") val totalCount: Int?,
    @Json(name = "data") val data: T? = null
)