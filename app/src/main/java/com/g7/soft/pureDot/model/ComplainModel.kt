package com.g7.soft.pureDot.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class ComplainModel(
    @Json(name = "id") val id: String?,
    @Json(name = "status") val status: Int?,
    @Json(name = "dateTime") val dateTime: Long?,
    @Json(name = "description") val description: String?,
    @Json(name = "number") val number: Int?,
): Parcelable