package com.g7.soft.pureDot.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NotificationModel(
    @Json(name = "id") val id: Int?,
    @Json(name = "dateTime") val dateTime: Long?,
    @Json(name = "text") val text: String?,
    @Json(name = "isRead") val isRead: Boolean?,
)