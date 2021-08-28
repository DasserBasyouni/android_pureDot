package com.g7.soft.pureDot.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ComplaintOrderModel(
    @Json(name = "id") val id: String?,
    @Json(name = "code") val number: Int?,
)