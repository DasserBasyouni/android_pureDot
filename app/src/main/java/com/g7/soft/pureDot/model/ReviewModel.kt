package com.g7.soft.pureDot.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class ReviewModel(
    @Json(name = "id") val id: String?,
    @Json(name = "reviewerName") val reviewerName: String? = null, // not received in case complaint service review
    @Json(name = "reviewerImageUrl") val reviewerImageUrl: String? = null, // not received in case complaint service review
    @Json(name = "rating") val rating: Int?,
    @Json(name = "review") val review: String?,
    //@Json(name = "isMarked") var isMarked: Boolean?,
    @Json(name = "date") val date: Long? = null, // not received in case complaint service review
) : Parcelable