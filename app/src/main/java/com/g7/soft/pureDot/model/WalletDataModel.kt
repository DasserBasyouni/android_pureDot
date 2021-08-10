package com.g7.soft.pureDot.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WalletDataModel(
    @Json(name = "balance") val balance: Double?,
    @Json(name = "totalEarning") val totalEarning: Double?,
    @Json(name = "totalDeliveries") val totalDeliveries: Int?,
    @Json(name = "currentWallet") val currentWallet: Double?,
    @Json(name = "totalTransfers") val totalTransfers: Double?,
    @Json(name = "currency") val currency: String?,
    @Json(name = "ownerName") val ownerName: String?,
    @Json(name = "points") var points: Int?,
    @Json(name = "pointsInCurrency") val pointsInCurrency: Double?,
)