package com.g7.soft.pureDot.util

import android.content.Context
import timber.log.Timber

// todo
class LogEventUtils {

    companion object{

        fun logApiError(errorField: String){
            Timber.e("LogEventUtils.logApiError: $errorField")
        }

        fun unhandledCase(context: Context?, details: String){
            // todo show crucial error engineers are working to fix it
        }

    }

}