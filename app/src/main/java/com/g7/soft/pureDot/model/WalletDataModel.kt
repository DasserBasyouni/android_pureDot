package com.g7.soft.pureDot.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WalletDataModel(
    @Json(name = "balance") val balance: Double? = 0.0,
    @Json(name = "totalEarning") val totalEarning: Double? = 0.0,
    @Json(name = "totalDeliveries") val totalDeliveries: Int? = 0,
    @Json(name = "currentWallet") val currentWallet: Double? = 0.0,
    @Json(name = "totalTransfers") val totalTransfers: Double? = 0.0,
    @Json(name = "currency") val currency: String?,
    @Json(name = "ownerName") val ownerName: String?,
    @Json(name = "points") var points: Int? = 0,
    @Json(name = "pointsInCurrency") val pointsInCurrency: Double? = 0.0,
)