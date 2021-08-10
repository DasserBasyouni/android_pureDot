package com.g7.soft.pureDot.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

//import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class ClientDataModel(
    @Json(name = "tokenId") val tokenId: String?,
    @Json(name = "firstName") val firstName: String? = "",
    @Json(name = "lastName") val lastName: String? = "",
    @Json(name = "mobileNumber") val phoneNumber: String?,
    @Json(name = "isMale") val isMale: Boolean?,
    @Json(name = "imageUrl") val imageUrl: String?,
    @Json(name = "points") val points: Int?,
    @Json(name = "credit") val credit: Double?,
    @Json(name = "email") val email: String?,
    @Json(name = "countryCode") val countryCode: String?,
    @Json(name = "currency") val currency: String?,
    @Json(name = "country") val country: CountryModel?,
    @Json(name = "city") val city: CityModel?,
    @Json(name = "dateOfBirth") val dateOfBirth: Long?,
    //@Json(name = "dailyNeeds") val dailyNeeds: DailyNeedsModel?
) : Parcelable {
    val fullName get() = "$firstName $lastName"
}