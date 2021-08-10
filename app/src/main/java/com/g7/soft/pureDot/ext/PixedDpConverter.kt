package com.g7.soft.pureDot.ext

import android.content.res.Resources


fun Int.dpToPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

fun Int.pxToDp(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()