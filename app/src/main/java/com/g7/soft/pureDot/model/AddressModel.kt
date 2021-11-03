package com.g7.soft.pureDot.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class AddressModel(
    @Json(name = "id") val id: String?,
    @Json(name = "flat") val flat: String?,
    @Json(name = "floor") val floor: String?,
    @Json(name = "buildingNumber") val buildingNumber: String?,
    @Json(name = "streetName") val streetName: String?,
    @Json(name = "areaName") val areaName: String?,
    @Json(name = "isMainAddress") val isMainAddress: Boolean?,
    @Json(name = "latitude") val latitude: String?,
    @Json(name = "longitude") val longitude: String?,
    @Json(name = "cityId") val cityId: String?,
    @Json(name = "zipCodeId") val zipCodeId: String?,
    @Json(name = "cityName") val cityName: String?,
) : Parcelable{
    val addressName = "$cityName $areaName $streetName"
}