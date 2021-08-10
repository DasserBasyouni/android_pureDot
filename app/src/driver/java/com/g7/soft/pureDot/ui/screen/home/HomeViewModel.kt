package com.g7.soft.pureDot.ui.screen.home

import android.location.Location
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.g7.soft.pureDot.model.DriverAvailabilityModel
import com.g7.soft.pureDot.model.DriverDataModel
import com.g7.soft.pureDot.model.OrderModel
import com.g7.soft.pureDot.model.project.LceeModel
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.AvailabilityRepository
import com.g7.soft.pureDot.repo.DriverRepository
import com.g7.soft.pureDot.repo.OrderRepository
import kotlinx.coroutines.Dispatchers

class HomeViewModel : ViewModel() {

    val isSideNavMenuOpened = MediatorLiveData<Boolean>().apply { this.value = false }
    val location = MutableLiveData<Location?>()

    var isFirstFetchData = false
    val isAvailable = MutableLiveData<Boolean?>()

    val availabilityResponse = MediatorLiveData<NetworkRequestResponse<DriverAvailabilityModel>>()
    val availabilityLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }

    val ordersResponse = MediatorLiveData<NetworkRequestResponse<MutableList<OrderModel>>>()
    val ordersLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }

    val selectedOrder = MutableLiveData<OrderModel?>()

    val userDataResponse = MediatorLiveData<NetworkRequestResponse<DriverDataModel>>()
    val userDataLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }

    init {
        availabilityResponse.value = null
        isFirstFetchData = false
    }

    fun fetchData(langTag: String, tokenId: String){
        checkAvailability(langTag, tokenId)
        getUserData(langTag, tokenId)
    }

    fun checkAvailability(langTag: String, tokenId: String) {
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

    fun changeAvailability(langTag: String, tokenId: String) = liveData(Dispatchers.IO) {
        emit(NetworkRequestResponse.loading())

        // fetch request
        emitSource(
            AvailabilityRepository(langTag).changeAvailability(
                tokenId = tokenId,
                isAvailable = isAvailable.value,
            )
        )
    }


    fun getPendingOrders(langTag: String, tokenId: String, lat: Double?, lng: Double?) {
        ordersResponse.value = NetworkRequestResponse.loading()
        ordersResponse.apply {
            this.addSource(
                OrderRepository(langTag).getPendingOrders(
                    tokenId = tokenId,
                    lat = lat,
                    lng = lng
                )
            ) {
                ordersResponse.value = it
            }
        }
    }

    fun getUserData(langTag: String, tokenId: String) {
        userDataResponse.value = NetworkRequestResponse.loading()
        userDataResponse.apply {
            this.addSource(DriverRepository(langTag).getUserData(tokenId = tokenId)) {
                userDataResponse.value = it
            }
        }
    }

    fun changeOrderStatus(langTag: String, tokenId: String, orderNumber: Int?, status: Int?) = liveData(Dispatchers.IO) {
        emit(NetworkRequestResponse.loading())

        // fetch request
        emitSource(
            OrderRepository(langTag).changeOrderStatus(
                tokenId = tokenId,
                orderNumber = orderNumber,
                status = status,
            )
        )
    }

}