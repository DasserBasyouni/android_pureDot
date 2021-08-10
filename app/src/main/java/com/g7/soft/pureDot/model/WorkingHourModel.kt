package com.g7.soft.pureDot.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WorkingHourModel(
    @Json(name = "id") val id: Int?,
    @Json(name = "fromTime") val fromTime: Long?,
    @Json(name = "toTime") val toTime: Long?,
    //@Json(name = "isEnabled") val isEnabled: Boolean?,
)