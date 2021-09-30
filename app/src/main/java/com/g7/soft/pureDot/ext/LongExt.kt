package com.g7.soft.pureDot.ext

import android.text.format.DateFormat
import java.util.*

fun Long.toFormattedDateTime(
    format: String,
    locale: Locale? = null,
    withoutTimeZone: Boolean? = true
): String {
    return if (withoutTimeZone == true) {
        DateFormat.format(
            format, Date(
                (this.times(1000))
                    .minus((Calendar.getInstance().timeZone.rawOffset))
            )
        ).toString()
    } else
        DateFormat.format(format, Date(this * 1000)).toString()
}

// return in seconds
fun Long.toDateWithoutTime(): Long {
    val dayInTimestamp = 86400 //(24*60*60)
    val daysInTimestamp: Int =
        (this / 1000 / dayInTimestamp).toInt() // converting millis to seconds with "/1000"
    return (daysInTimestamp * dayInTimestamp).toLong()
}