package com.g7.soft.pureDot.model.project.timeline

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class TimeLineModel(
        var activatedImageResId: Int,
        var deactivatedImageResId: Int,
        var title: String,
        var subTitle: String
) : Parcelable
