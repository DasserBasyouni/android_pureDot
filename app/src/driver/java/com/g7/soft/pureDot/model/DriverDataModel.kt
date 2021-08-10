package com.g7.soft.pureDot.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class DriverDataModel(
    @Json(name = "tokenId") val tokenId: String?,
    @Json(name = "firstName") val firstName: String? = "",
    @Json(name = "lastName") val lastName: String? = "",
    @Json(name = "mobileNumber") val mobileNumber: String?,
    @Json(name = "city") val city: CityModel?,
    @Json(name = "country") val country: CountryModel?,
    @Json(name = "isMale") val isMale: Boolean?,
    @Json(name = "carBrand") val carBrand: String?,
    @Json(name = "carFrontImageUrl") val carFrontImageUrl: String?,
    @Json(name = "carBackImageUrl") val carBackImageUrl: String?,
    @Json(name = "licenceImageUrl") val licenceImageUrl: String?,
    @Json(name = "nationalIdImageUrl") val nationalIdImageUrl: String?,
    @Json(name = "balance") val balance: Double?,
    @Json(name = "rating") val rating: Float?,
    @Json(name = "dateOfBirth") val dateOfBirth: Long?,
    @Json(name = "email") val email: String?,
    @Json(name = "profileImageUrl") val profileImageUrl: String?,
    @Json(name = "currency") val currency: String?,
) : Parcelable {
    val fullName get() = "$firstName $lastName"
}