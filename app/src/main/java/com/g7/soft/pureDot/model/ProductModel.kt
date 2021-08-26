package com.g7.soft.pureDot.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class ProductModel(
    @Json(name = "id") val id: String?,
    @Json(name = "name") val name: String? = null,
    @Json(name = "price") val price: Double? = null, // (price without any discounts) receiving this in checkCartProducts only
    //@Json(name = "quantity") var quantity: Int? = null, // receiving this in checkCartProducts only
    //@Json(name = "measureUnit") val measureUnit: String? = null,
    @Json(name = "imageUrl") val imageUrl: String? = null,
    //@Json(name = "vat") val vat: Double? = null, // receiving this in checkCartProducts only
    @Json(name = "shop") val shop: StoreModel? = null,
    @Json(name = "isAvailable") val isAvailable: Boolean? = null,
    @Json(name = "currency") val currency: String? = null, // receiving this in checkCartProducts only
    @Json(name = "discountPercentage") val discountPercentage: Float? = null,
    @Json(name = "isPercentageDiscount") val isPercentageDiscount: Boolean? = null,
    var quantityInCart: Int? = null,
    //@Json(name = "userReview") var userReview: ReviewModel? = null,
    //@Json(name = "availableQuantity") var availableQuantity: Int? = null,
    @Json(name = "isInWishList") var isInWishList: Boolean? = null, // receiving this in checkCartProducts only

    ) : Parcelable {
    val discountPercentageInHundred get() = discountPercentage?.times(100)?.toInt() ?: 0

    val priceWithDiscount get() = price?.minus(price.times(discountPercentage ?: 0f))
}