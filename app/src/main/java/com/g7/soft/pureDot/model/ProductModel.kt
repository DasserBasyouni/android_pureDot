package com.g7.soft.pureDot.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class ProductModel(
    @Json(name = "id") val id: Int?,
    @Json(name = "name") val name: String? = null,
    @Json(name = "price") val price: Double? = null,
    @Json(name = "quantity") var quantity: Int? = null,
    @Json(name = "measureUnit") val measureUnit: String? = null,
    @Json(name = "imageUrl") val imageUrl: String? = null,
    @Json(name = "vat") val vat: Double? = null,
    @Json(name = "shop") val shop: StoreModel? = null,
    @Json(name = "isAvailable") val isAvailable: Boolean? = null,
    @Json(name = "currency") val currency: String? = null,
    @Json(name = "discountPercentage") val discountPercentage: Float? = null,
    var quantityInCart: Int? = null,
    @Json(name = "userReview") var userReview: ReviewModel? = null,
    @Json(name = "availableQuantity") var availableQuantity: Int? = null,
) : Parcelable {
    val discountPercentageInHundred get() = discountPercentage?.times(100)?.toInt() ?: 0

    val priceWithDiscount get() = price?.minus(price.times(discountPercentage ?: 0f))
}