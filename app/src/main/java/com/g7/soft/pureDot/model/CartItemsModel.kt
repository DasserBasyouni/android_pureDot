package com.g7.soft.pureDot.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class CartItemsModel(
    @Json(name = "products") val products: MutableList<ProductModel>?,
    @Json(name = "serviceDateTime") val serviceDateTime: Long?,
) : Parcelable