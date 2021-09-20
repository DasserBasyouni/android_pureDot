package com.g7.soft.pureDot.repo

import android.content.Context
import androidx.lifecycle.liveData
import com.g7.soft.pureDot.data.database.userData.UserDataDatabase
import com.g7.soft.pureDot.model.UserDataModel
import com.g7.soft.pureDot.network.Fetcher
import com.g7.soft.pureDot.network.NetworkRequestHandler
import kotlinx.coroutines.Dispatchers

open class UserRepositoryFlavour(private val langTag: String) {

    fun getIsGuestAccount(context: Context) = false

    suspend fun updateIsGuestAccount(context: Context, value: Boolean) =
        UserDataDatabase.getInstance(context).updateIsGuestAccount(value)

    suspend fun saveUserDataFlavour(
        context: Context,
        clientData: UserDataModel?,
        password: String?
    ) =
        UserDataDatabase.getInstance(context).also {

        }

    suspend fun clearUserDataFlavour(context: Context) =
        UserDataDatabase.getInstance(context).also {

        }

    fun editUserData(
        tokenId: String?,
        name: String?,
        phoneNumber: String?,
        isMale: Boolean?,
        imageUrl: String?,
        email: String?,
        cityId: String?,
        dateOfBirth: Long?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.editUserData(
                tokenId = tokenId,
                name = name,
                phoneNumber = phoneNumber,
                isMale = isMale,
                imageUrl = imageUrl,
                email = email,
                cityId = cityId,
                dateOfBirth = dateOfBirth
            )
        }))
    }

}