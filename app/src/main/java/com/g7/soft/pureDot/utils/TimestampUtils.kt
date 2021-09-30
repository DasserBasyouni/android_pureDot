package com.g7.soft.pureDot.utils

import java.util.*

class TimestampUtils {
    companion object{
        val timeZoneOffsetInSec get() = TimeZone.getDefault().rawOffset / 1000
    }
}