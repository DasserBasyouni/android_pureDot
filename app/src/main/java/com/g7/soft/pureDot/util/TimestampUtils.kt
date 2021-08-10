package com.g7.soft.liveCoach.utils

import java.util.*

class TimestampUtils {
    companion object{
        val timeZoneOffsetInSec get() = TimeZone.getDefault().rawOffset / 1000
    }
}