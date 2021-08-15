package com.g7.soft.pureDot.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class ReviewModel(
    @Json(name = "id") val id: Int?,
    @Json(name = "reviewerName") val reviewerName: String?,
    @Json(name = "reviewerImageUrl") val reviewerImageUrl: String?,
    @Json(name = "rating") val rating: Float?,
    @Json(name = "review") val review: String?,
    //@Json(name = "isMarked") var isMarked: Boolean?,
    @Json(name = "date") val date: Long?,
) : Parcelable