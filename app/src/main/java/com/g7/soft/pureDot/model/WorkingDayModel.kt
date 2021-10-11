package com.g7.soft.pureDot.model

import androidx.lifecycle.MutableLiveData
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WorkingDayModel(
    @Json(name = "day") var day: Int?,
    @Json(name = "isEnabled") var isEnabled: Boolean?,
    @Json(name = "times") var times: MutableList<WorkingHourModel>? = mutableListOf(),
){
    //var isExpanded = false

    val isEnableLiveData = MutableLiveData<Boolean?>().also { it.postValue(isEnabled) }
}