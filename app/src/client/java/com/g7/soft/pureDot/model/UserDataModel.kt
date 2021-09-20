package com.g7.soft.pureDot.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

//import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class UserDataModel(
    @Json(name = "tokenId") val tokenId: String?,
    @Json(name = "firstName") val firstName: String? = "",
    @Json(name = "lastName") val lastName: String? = "",
    @Json(name = "phoneNumber") val phoneNumber: String?,
    @Json(name = "isMale") val isMale: Boolean?,
    @Json(name = "imageUrl") val imageUrl: String?,
    @Json(name = "points") val points: Int?,
    @Json(name = "credit") val credit: Double? = 0.0,
    @Json(name = "email") val email: String?,
    @Json(name = "countryCode") val countryCode: String?,
    //@Json(name = "currency") val currency: CurrencyModel?,
    @Json(name = "country") val country: CountryModel?,
    @Json(name = "city") val city: CityModel?,
    @Json(name = "zipCode") val zipCode: ZipCodeModel?,
    @Json(name = "dateOfBirth") val dateOfBirth: Long?,
    //@Json(name = "dailyNeeds") val dailyNeeds: DailyNeedsModel?
) : Parcelable {
    val name get() = "$firstName $lastName"
}