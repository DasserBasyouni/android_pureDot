package com.g7.soft.pureDot.ui.screen.splash
    
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.g7.soft.pureDot.model.DriverDataModel
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.DriverRepository

class SplashViewModel : ViewModel(){

    val userDataResponse = MediatorLiveData<NetworkRequestResponse<DriverDataModel>>()


    fun signUpAsGuest(langTag: String){
        val fcmToken: String? = null // todo

        userDataResponse.value = NetworkRequestResponse.loading()

        // fetch request
        userDataResponse.apply {
            addSource(
                DriverRepository(langTag).signUpAsGuest(
                    fcmToken = fcmToken,
                )
            ) { userDataResponse.value = it }
        }
    }

}