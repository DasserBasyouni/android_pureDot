package com.g7.soft.pureDot.model.project

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import com.g7.soft.pureDot.constant.ProjectConstant
import com.g7.soft.pureDot.model.DataWithCountModel
import com.g7.soft.pureDot.network.response.NetworkRequestResponse


class LceeModel(private val allowEmpty: Boolean? = false, private val allowNull: Boolean? = false) {
    val response = MediatorLiveData<NetworkRequestResponse<*>?>()

    init {
        response.value = NetworkRequestResponse<Any>(status = ProjectConstant.Companion.Status.IDLE)
    }

    private val haveNetworkError
        get() = Transformations.map(response) {
            it?.status == ProjectConstant.Companion.Status.NETWORK_ERROR
        }

    val loadingVisibility
        get() = Transformations.map(response) {
            if (it?.status == ProjectConstant.Companion.Status.LOADING) View.VISIBLE else View.GONE
        }

    val contentVisibility
        get() = Transformations.map(response) {
            if (it?.status == ProjectConstant.Companion.Status.SUCCESS
                && (it.data is ArrayList<*> && ((allowEmpty == false && !it.data.isNullOrEmpty())
                        || (allowEmpty == true)) || it.data !is ArrayList<*> && (allowNull == true || it.data != null))
            )
                View.VISIBLE
            else
                View.GONE
        }

    val emptyVisibility: LiveData<Int>
        get() = Transformations.map(response) {
            if ((it?.status != ProjectConstant.Companion.Status.IDLE && it?.status != ProjectConstant.Companion.Status.LOADING)
                && (it?.data is ArrayList<*> && (allowEmpty == false && it.data.isNullOrEmpty()) // case list
                        || it?.data is DataWithCountModel<*> && it.data.data is ArrayList<*> && (allowEmpty == false && it.data.data.isNullOrEmpty()) // case list with count
                        || it?.data !is ArrayList<*> && (allowNull == false && it?.data == null)) // case object
            ) View.VISIBLE else View.GONE
        }

    /* todo
    val noInternetVisibility
        get() =
            if (haveNetworkError && dataResponse.status == ProjectConstant.Companion.Status.NETWORK_ERROR) View.VISIBLE else View.GONE

    val somethingWrongVisibility
        get() =
            if (haveNetworkError && dataResponse.status == ProjectConstant.Companion.Status.NETWORK_ERROR) View.VISIBLE else View.GONE
            */
}