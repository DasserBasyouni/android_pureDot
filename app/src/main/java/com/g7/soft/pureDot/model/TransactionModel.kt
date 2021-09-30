package com.g7.soft.pureDot.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TransactionModel(
    @Json(name = "id") val id: String?,
    @Json(name = "dateTime") val dateTime: Long?,
    @Json(name = "type") val type: Int?,
    @Json(name = "amount") val amount: Double?,
    @Json(name = "typeName") val typeName: String?,
    @Json(name = "description") val description: String?,
    //@Json(name = "currency") val currency: String?,
)