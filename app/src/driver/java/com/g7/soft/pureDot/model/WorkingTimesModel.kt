package com.g7.soft.pureDot.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WorkingTimesModel(
    @Json(name = "saturday") val saturday: WorkingDayModel? = WorkingDayModel(false, null),
    @Json(name = "sunday") val sunday: WorkingDayModel? = WorkingDayModel(false, null),
    @Json(name = "monday") val monday: WorkingDayModel? = WorkingDayModel(false, null),
    @Json(name = "tuesday") val tuesday: WorkingDayModel? = WorkingDayModel(false, null),
    @Json(name = "wednesday") val wednesday: WorkingDayModel? = WorkingDayModel(false, null),
    @Json(name = "thursday") val thursday: WorkingDayModel? = WorkingDayModel(false, null),
    @Json(name = "friday") val friday: WorkingDayModel? = WorkingDayModel(false, null),
) {
    fun setIsExpanded(adapterPosition: Int) {
        when (adapterPosition) {
            0 -> saturday?.isExpanded = !saturday?.isExpanded!!
            1 -> sunday?.isExpanded = !sunday?.isExpanded!!
            2 -> monday?.isExpanded = !monday?.isExpanded!!
            3 -> tuesday?.isExpanded = !tuesday?.isExpanded!!
            4 -> wednesday?.isExpanded = !wednesday?.isExpanded!!
            5 -> thursday?.isExpanded = !thursday?.isExpanded!!
            6 -> friday?.isExpanded = !friday?.isExpanded!!
        }
    }

    fun getIsExpanded(adapterPosition: Int): Boolean? = when (adapterPosition) {
        0 -> saturday?.isExpanded
        1 -> sunday?.isExpanded
        2 -> monday?.isExpanded
        3 -> tuesday?.isExpanded
        4 -> wednesday?.isExpanded
        5 -> thursday?.isExpanded
        6 -> friday?.isExpanded
        else -> null
    }
}