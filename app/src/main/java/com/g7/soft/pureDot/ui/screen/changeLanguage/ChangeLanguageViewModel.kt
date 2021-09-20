package com.g7.soft.pureDot.ui.screen.changeLanguage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.GeneralRepository
import kotlinx.coroutines.Dispatchers

class ChangeLanguageViewModel : ViewModel() {

    fun changeLanguage(langTag: String, fcmToken: String, language: Int) = liveData(Dispatchers.IO) {
        emit(NetworkRequestResponse.loading())
        emitSource(
            GeneralRepository(langTag).changeLanguage(
                fcmToken = fcmToken,
                language = language,
            )
        )
    }
}