package com.g7.soft.pureDot.ext

import android.text.format.DateFormat
import java.util.*

fun Long.toFormattedDateTime(format: String): String = DateFormat.format(format, Date(this * 1000)).toString()