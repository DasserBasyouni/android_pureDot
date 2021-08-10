package com.g7.soft.pureDot.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class StoreModel(
    @Json(name = "id") val id: Int?,
    @Json(name = "name") val name: String? = null,
    @Json(name = "logoImageUrl") val logoImageUrl: String? = null
): Parcelable