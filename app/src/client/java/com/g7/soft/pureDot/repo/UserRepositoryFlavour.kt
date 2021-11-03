package com.g7.soft.pureDot.repo

import android.content.Context
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.liveData
import com.g7.soft.pureDot.data.database.userData.UserDataDatabase
import com.g7.soft.pureDot.model.UserDataModel
import com.g7.soft.pureDot.network.Fetcher
import com.g7.soft.pureDot.network.NetworkRequestHandler
import kotlinx.coroutines.flow.first

open class UserRepositoryFlavour(private val langTag: String) {

    /*suspend fun getLocalUserData(context: Context) =
        UserDataDatabase.getInstance(context).userDataFlow.first()*/

    suspend fun getIsGuestAccount(context: Context): Boolean =
        UserDataDatabase.getInstance(context).userDataFlow.first().isGuestAccount

    suspend fun getFirstName(context: Context): String =
        UserDataDatabase.getInstance(context).userDataFlow.first().firstName

    suspend fun updateIsGuestAccount(context: Context, value: Boolean?) =
        UserDataDatabase.getInstance(context).updateIsGuestAccount(value)

    suspend fun saveUserDataFlavour(context: Context, userData: UserDataModel?, password: String?) =
        UserDataDatabase.getInstance(context).also {
            it.updateFirstName(userData?.firstName)
            it.updateIsGuestAccount(false)
        }

    suspend fun clearUserDataFlavour(lifecycleScope: LifecycleCoroutineScope, context: Context): UserDataDatabase {
        CartRepository("").clearCart(lifecycleScope, context)
        return UserDataDatabase.getInstance(context).also {
            it.updateFirstName(null)
            it.updateIsGuestAccount(null)
        }
    }

    fun signUp(
        fcmToken: String?,
        firstName: String?,
        lastName: String?,
        phoneNumber: String?,
        email: String?,
        cityId: String?,
        zipCodeId: String?,
        password: String?,
        isMale: Boolean?,
    ) = liveData(kotlinx.coroutines.Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.signUp(
                fcmToken = fcmToken,
                firstName = firstName,
                lastName = lastName,
                phoneNumber = phoneNumber,
                email = email,
                cityId = cityId,
                zipCodeId = zipCodeId,
                password = password,
                isMale = isMale,
            )
        }))
    }

    fun getAddresses(
        tokenId: String?,
    ) = liveData(kotlinx.coroutines.Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.getAddresses(
                tokenId = tokenId,
            )
        }))
    }

    fun addAddress(
        tokenId: String?,
        flat: String?,
        floor: String?,
        buildingNumber: String?,
        streetName: String?,
        area: String?,
        cityId: String?,
        latitude: Double?,
        longitude: Double?,
        zipCodeId: String?,
        isMainAddress: Boolean?,
    ) = liveData(kotlinx.coroutines.Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.addAddress(
                tokenId = tokenId,
                flat = flat,
                floor = floor,
                buildingNumber = buildingNumber,
                streetName = streetName,
                areaName = area,
                isMainAddress = isMainAddress,
                cityId = cityId,
                latitude = latitude,
                longitude = longitude,
                zipCodeId = zipCodeId
            )
        }))
    }

    fun editUserData(
        tokenId: String?,
        firstName: String?,
        lastName: String?,
        phoneNumber: String?,
        isMale: Boolean?,
        imageUrl: String?,
        email: String?,
        cityId: String?,
        zipCodeId: String?,
        dateOfBirth: Long?,
    ) = liveData(kotlinx.coroutines.Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.editUserData(
                tokenId = tokenId,
                firstName = firstName,
                lastName = lastName,
                phoneNumber = phoneNumber,
                isMale = isMale,
                zipCodeId = zipCodeId,
                imageUrl = imageUrl,
                email = email,
                cityId = cityId,
                dateOfBirth = dateOfBirth
            )
        }))
    }
}