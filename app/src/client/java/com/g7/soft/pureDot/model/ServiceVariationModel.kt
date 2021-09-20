package com.g7.soft.pureDot.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class ServiceVariationModel(
    @Json(name = "title") val title: String?,
    @Json(name = "values") val values: List<ServiceVariationValueModel>?
): Parcelable