package com.g7.soft.pureDot.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ShippingCostModel(
    @Json(name = "shippingCost") val shippingCost: Double?,
    @Json(name = "refundAmount") val refundAmount: Double?,
    @Json(name = "commission") val commission: Double?,
    @Json(name = "itemTotalPrice") val itemTotalPrice: Double?,
    @Json(name = "vat") val vat: Double?,
    @Json(name = "driverEarning") val driverEarning: Double?,
    @Json(name = "driverVat") val driverVat: Double?,
    @Json(name = "deliveryCommissionVat") val deliveryCommissionVat: Double?,
)