package com.g7.soft.pureDot.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class StoreProductsCartDetailsModel(
    @Json(name = "vat") val vat: Double? = null,
    @Json(name = "discountedPrice") val discountedPrice: Double? = null,
    @Json(name = "subTotal") val subTotal: Double? = null,
    //@Json(name = "currency") val currency: String? = null,
    @Json(name = "shippingCost") val shippingCost: Double? = null,
    @Json(name = "products") val products: MutableList<ProductModel>?,
) : Parcelable {
    val totalCost = subTotal?.plus(shippingCost ?: 0.0)?.plus(vat ?: 0.0)
    /*val discountPercentageInHundred get() = discountPercentage?.times(100)?.toInt() ?: 0

    val priceWithDiscount get() = price?.minus(price.times(discountPercentage ?: 0f))*/
}