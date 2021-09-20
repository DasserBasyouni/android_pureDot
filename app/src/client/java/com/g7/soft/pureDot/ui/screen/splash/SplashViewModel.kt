package com.g7.soft.pureDot.ui.screen.splash
    
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.GeneralRepository
import com.g7.soft.pureDot.repo.UserRepository
import kotlinx.coroutines.Dispatchers

class SplashViewModel : ViewModel(){

    //val userDataResponse = MediatorLiveData<NetworkRequestResponse<ClientDataModel>>()


    /*fun signUpAsGuest(langTag: String){
        val fcmToken: String? = null // todo

        userDataResponse.value = NetworkRequestResponse.loading()

        // fetch request
        userDataResponse.apply {
            addSource(
                com.g7.soft.pureDot.repo.ClientRepository(langTag).signUpAsGuest(
                    fcmToken = fcmToken,
                )
            ) { userDataResponse.value = it }
        }
    }*/

    fun getAppCurrency(langTag: String) = liveData(Dispatchers.IO) {
        emit(NetworkRequestResponse.loading())
        emitSource(GeneralRepository(langTag).getAppCurrency())
    }


    fun login(langTag: String, emailOrPhoneNumber: String?, password: String?) = liveData(Dispatchers.IO) {
        val fcmToken: String? = null // todo

        emit(NetworkRequestResponse.loading())
        emitSource(
            UserRepository(langTag).login(
                fcmToken = fcmToken,
                emailOrPhoneNumber = emailOrPhoneNumber,
                password = password
            )
        )
    }

}