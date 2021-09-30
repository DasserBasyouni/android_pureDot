package com.g7.soft.pureDot.ui.screen.home

import android.location.Location
import androidx.lifecycle.*
import androidx.paging.PagedList
import com.g7.soft.pureDot.model.DriverAvailabilityModel
import com.g7.soft.pureDot.model.MasterOrderModel
import com.g7.soft.pureDot.model.UserDataModel
import com.g7.soft.pureDot.model.project.LceeModel
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.AvailabilityRepository
import com.g7.soft.pureDot.repo.OrderRepository
import com.g7.soft.pureDot.repo.UserRepository
import kotlinx.coroutines.Dispatchers

class HomeViewModel(internal val tokenId: String?) : ViewModel() {

    val isSideNavMenuOpened = MediatorLiveData<Boolean>().apply { this.value = false }
    val location = MutableLiveData<Location?>()

    var isFirstFetchData = false
    val isAvailable = MutableLiveData<Boolean?>()

    val availabilityResponse = MediatorLiveData<NetworkRequestResponse<DriverAvailabilityModel>>()
    val availabilityLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }

    val ordersLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }
    var ordersPagedList: LiveData<PagedList<MasterOrderModel>>? = null

    val selectedOrder = MutableLiveData<MasterOrderModel?>()

    val userDataResponse = MediatorLiveData<NetworkRequestResponse<UserDataModel>>()
    val userDataLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }

    val isNavigationView = MediatorLiveData<Boolean>().apply { this.value = false }


    init {
        availabilityResponse.value = null
        isFirstFetchData = false
    }

    fun fetchData(langTag: String, tokenId: String?) {
        checkAvailability(langTag, tokenId)
        getUserData(langTag, tokenId)
    }

    fun checkAvailability(langTag: String, tokenId: String?) {
        availabilityResponse.value = NetworkRequestResponse.loading()
        availabilityResponse.apply {
            this.addSource(
                AvailabilityRepository(langTag).checkAvailability(
                    tokenId = tokenId,
                )
            ) {
                availabilityResponse.value = it
            }
        }
    }

    fun changeAvailability(langTag: String, tokenId: String?) = liveData(Dispatchers.IO) {
        emit(NetworkRequestResponse.loading())

        // fetch request
        emitSource(
            AvailabilityRepository(langTag).changeAvailability(
                tokenId = tokenId,
                isAvailable = isAvailable.value,
            )
        )
    }

    fun getUserData(langTag: String, tokenId: String?) {
        userDataResponse.value = NetworkRequestResponse.loading()
        userDataResponse.apply {
            this.addSource(UserRepository(langTag).getUserData(tokenId = tokenId)) {
                userDataResponse.value = it
            }
        }
    }

    fun changeOrderStatus(
        langTag: String,
        tokenId: String?,
        orderId: String?,
        status: Int?,
        isReturn: Boolean?
    ) =
        liveData(Dispatchers.IO) {
            emit(NetworkRequestResponse.loading())

            // fetch request
            emitSource(
                OrderRepository(langTag).changeOrderStatus(
                    tokenId = tokenId,
                    orderId = orderId,
                    status = status,
                    isReturn = isReturn,
                )
            )
        }

}