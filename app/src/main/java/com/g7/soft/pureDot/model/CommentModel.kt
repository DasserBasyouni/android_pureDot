package com.g7.soft.pureDot.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CommentModel(
    @Json(name = "id") val id: String?,
    @Json(name = "commenterName") val commenterName: String?,
    @Json(name = "dateTime") val dateTime: Long?,
    @Json(name = "commenterImageUrl") val commenterImageUrl: String?,
    @Json(name = "comment") val comment: String?,
)