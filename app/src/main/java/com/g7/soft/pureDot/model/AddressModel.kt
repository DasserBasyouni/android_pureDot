package com.g7.soft.pureDot.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class AddressModel(
    @Json(name = "id") val id: Int?,
    @Json(name = "flat") val flat: String?,
    @Json(name = "floor") val floor: String?,
    @Json(name = "homeNumber") val homeNumber: String?,
    @Json(name = "streetName") val streetName: String?,
    @Json(name = "area") val area: String?,
    @Json(name = "isMainAddress") val isMainAddress: Boolean?,
) : Parcelable