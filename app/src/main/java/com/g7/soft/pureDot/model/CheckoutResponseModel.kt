package com.g7.soft.pureDot.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CheckoutResponseModel(
    @Json(name = "orderModel") val orderModel: MasterOrderModel?,
    @Json(name = "orderMinimumCharge") val orderMinimumCharge: Double?,
    @Json(name = "successModel") val checkoutSuccessResponse: CheckoutSuccessResponseModel?,
)