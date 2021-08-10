package com.g7.soft.pureDot.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ContactModel(
    @Json(name = "id") val id: String?,
    @Json(name = "firstName") val firstName: String? = "",
    @Json(name = "lastName") val lastName: String? = "",
    @Json(name = "imageUrl") val imageUrl: String?,
    @Json(name = "phoneNumber") val phoneNumber: String?
) {
    val fullName get() = "$firstName $lastName"
}