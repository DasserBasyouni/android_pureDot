package com.g7.soft.pureDot.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class UserDataModel(
    @Json(name = "tokenId") val tokenId: String?,
    @Json(name = "name") val name: String? = "",
    @Json(name = "phoneNumber") val phoneNumber: String?,
    @Json(name = "city") val city: CityModel? = null,
    @Json(name = "country") val country: CountryModel? = null,
    @Json(name = "isMale") val isMale: Boolean?,
    @Json(name = "carBrand") val carBrand: String?,
    @Json(name = "carFrontImageUrl") val carFrontImageUrl: String? = null,
    @Json(name = "carBackImageUrl") val carBackImageUrl: String? = null,
    @Json(name = "licenceImageUrl") val licenceImageUrl: String? = null,
    @Json(name = "nationalIdImageUrl") val nationalIdImageUrl: String? = null,
    @Json(name = "balance") val balance: Double? = null,
    @Json(name = "rating") val rating: Float? = null,
    @Json(name = "dateOfBirth") val dateOfBirth: Long?,
    @Json(name = "email") val email: String?,
    @Json(name = "profileImageUrl") val profileImageUrl: String? = null,
    //@Json(name = "currency") val currency: CurrencyModel?,
    @Json(name = "language") val language: String?,
    @Json(name = "doNotify") val doNotify: Boolean? = null,
) : Parcelable