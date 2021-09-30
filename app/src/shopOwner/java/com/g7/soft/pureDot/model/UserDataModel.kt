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
    @Json(name = "isMale") val isMale: Boolean?,
    @Json(name = "imageUrl") val imageUrl: String?,
    @Json(name = "points") val points: Int?,
    @Json(name = "balance") val credit: Double?,
    @Json(name = "email") val email: String?,
    @Json(name = "countryCode") val countryCode: String?,
    @Json(name = "country") val country: CountryModel?,
    @Json(name = "city") val city: CityModel?,
    @Json(name = "dateOfBirth") val dateOfBirth: Long?,
    @Json(name = "branchName") val branchName: String?,
    @Json(name = "branchCountryName") val branchCountryName: String?,
    @Json(name = "branchCityName") val branchCityName: String?,
    @Json(name = "branchCategoryName") val branchCategoryName: String?,
) : Parcelable