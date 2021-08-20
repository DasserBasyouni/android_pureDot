package com.g7.soft.pureDot.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BranchModel(
    @Json(name = "id") val id: Int?,
    @Json(name = "name") val name: String?,
)