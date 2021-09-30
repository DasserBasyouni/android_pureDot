package com.g7.soft.pureDot.model

import com.g7.soft.pureDot.ext.toFormattedDateTime
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class  WorkingHourModel(
    @Json(name = "id") val id: Int?,
    @Json(name = "fromTime") var fromTime: Long?,
    @Json(name = "toTime") var toTime: Long?,
    //@Json(name = "day") val day: Int?,
    //@Json(name = "isEnabled") val isEnabled: Boolean?,
) {
    //val fromTimeWithTimeZone get() = fromTime?.plus(Calendar.getInstance().timeZone.rawOffset.toLong() / 1000)
    //val toTimeWithTimeZone get() = toTime?.plus(Calendar.getInstance().timeZone.rawOffset.toLong() / 1000)

    val fromHour get() = fromTime?.toFormattedDateTime("hh")
    val fromMin get() = fromTime?.toFormattedDateTime("mm")
    val fromIsAm: Boolean
        get() = fromTime?.toFormattedDateTime("a")
            .equals("am", ignoreCase = true) // todo test ar locale

    val toHour get() = toTime?.toFormattedDateTime("hh")
    val toMin get() = toTime?.toFormattedDateTime("mm")
    val toIsAm
        get() = toTime?.toFormattedDateTime("a")
            .equals("am", ignoreCase = true)  // todo test ar locale
}