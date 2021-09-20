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
    @Json(name = "nameWithVariations") val nameWithVariations: String? = null,
    @Json(name = "quantity") val quantity: Int? = null, // receiving this in orders cycle only
    @Json(name = "imageUrl") val imageUrl: String? = null,
    @Json(name = "shop") val shop: StoreModel? = null,
    @Json(name = "isAvailable") val isAvailable: Boolean? = true, // receiving this in getWishList only so default value will be true for UI
    @Json(name = "discountPercentage") val discountPercentage: Float? = null,
    @Json(name = "isPercentageDiscount") val isPercentageDiscount: Boolean? = null,
    @Json(name = "unitPrice")  var unitPrice: Double? = null, // receiving this in CheckCartItems only
    @Json(name = "isInWishList") var isInWishList: Boolean? = null, // receiving this in checkCartProducts only
    @Json(name = "detailsId") var detailsId: String? = null, // receiving this in orders cycle only
    @Json(name = "categoryId") val categoryId: String? = null,
    @Json(name = "isCanReturn") val allowReturn: Boolean? = null,
) : Parcelable {
    //val discountPercentageInHundred get() = discountPercentage?.times(100)?.toInt() ?: 0

    //val priceWithDiscount =0.0 // todo delete
    //val priceWithDiscount get() = price?.minus(price.times(discountPercentage ?: 0f))
}