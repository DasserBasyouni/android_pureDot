package com.g7.soft.pureDot.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiResponseModel<out T>(
    @Json(name = "Status") val status: Int?,
    @Json(name = "technicalError") val technicalError: String? = null,
    @Json(name = "Data") val data: T? = null
)