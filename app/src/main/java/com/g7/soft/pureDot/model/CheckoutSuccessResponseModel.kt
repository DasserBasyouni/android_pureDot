package com.g7.soft.pureDot.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CheckoutSuccessResponseModel(
    @Json(name = "orderId") val masterOrderId: String?,
    @Json(name = "orderNumber") val orderNumber: Int?,
    @Json(name = "discription") val description: String?,
    @Json(name = "orderAmount") val orderAmount: Double?,
)