package com.g7.soft.pureDot.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class ApiShopOrderItemModel(
    @Json(name = "productId") val productId: String?,
    @Json(name = "categoryId") val categoryId: String?,
    @Json(name = "sourceId") val sourceId: String?,
    @Json(name = "stockId") val stockId: String?,
    @Json(name = "quantity") var quantity: Int?,
    var price: Double? = null,
    @Json(name = "variationsIds") val variationsIds: List<String>?,
) : Parcelable {
    val itemSubTotal get() = price?.times(quantity ?: 0)
}