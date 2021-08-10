package com.g7.soft.pureDot.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class ServiceModel(
    @Json(name = "id") val id: Int?,
    @Json(name = "name") val name: String?,
    @Json(name = "price") val price: Double?,
    @Json(name = "items") val items: CartItemsModel?,
    @Json(name = "shopLogoUrl") val shopLogoUrl: String?,
    @Json(name = "imageUrl") val imageUrl: String?,
    @Json(name = "description") val description: String?,
    @Json(name = "quantityInCart") var quantityInCart: Int? = null,
) : Parcelable {
    val currency get() = items?.products?.first()?.currency
}