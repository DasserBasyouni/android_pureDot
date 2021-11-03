package com.g7.soft.pureDot.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DriverAvailabilityModel(
    @Json(name = "isAvailable") var isAvailable: Boolean?,
    @Json(name = "currentShift") val currentShift: ShiftModel?,
    @Json(name = "nextShift") val nextShift: ShiftModel?,
) {

}