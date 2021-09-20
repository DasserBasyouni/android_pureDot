package com.g7.soft.pureDot.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class OrderReviewModel(
    @Json(name = "orderRating") val orderRating: Int?,
    @Json(name = "orderComment") val orderComment: String?,
    @Json(name = "deliveryRating") val deliveryRating: Int?,
    @Json(name = "deliveryComment") val deliveryComment: String?,
) : Parcelable