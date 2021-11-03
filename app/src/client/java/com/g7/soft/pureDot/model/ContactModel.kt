package com.g7.soft.pureDot.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ContactModel(
    @Json(name = "id") val id: String?,
    @Json(name = "name") val fullName: String? = "",
    @Json(name = "profileImage") val imageUrl: String?
)