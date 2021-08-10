package com.g7.soft.pureDot.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TransactionModel(
    @Json(name = "id") val id: Int?,
    @Json(name = "dateTime") val dateTime: Long?,
    @Json(name = "type") val type: Int?,
    @Json(name = "amount") val amount: Double?,
    @Json(name = "currency") val currency: String?,
)