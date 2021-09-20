package com.g7.soft.pureDot.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class ServiceVariationValueModel(
    @Json(name = "productId") val productId: String?,
    @Json(name = "sourceId") val sourceId: String?,
    @Json(name = "stockId") val stockId: String?,
    @Json(name = "categoryId") val categoryId: String?,
    @Json(name = "value") val value: String?,
): Parcelable