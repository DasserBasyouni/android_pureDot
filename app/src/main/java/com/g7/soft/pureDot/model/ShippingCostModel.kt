package com.g7.soft.pureDot.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ShippingCostModel(
    @Json(name = "shippingCost") val shippingCost: Double?,
)