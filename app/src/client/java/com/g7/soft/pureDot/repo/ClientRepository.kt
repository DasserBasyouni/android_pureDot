package com.g7.soft.pureDot.repo

import android.content.Context
import androidx.lifecycle.liveData
import com.g7.soft.pureDot.data.database.userData.UserDataDatabase
import com.g7.soft.pureDot.model.ClientDataModel
import com.g7.soft.pureDot.network.Fetcher
import com.g7.soft.pureDot.network.NetworkRequestHandler
import kotlinx.coroutines.flow.first

class ClientRepository(private val langTag: String) {

    suspend fun getLocalUserData(context: Context) =
        UserDataDatabase.getInstance(context).userDataFlow.first()

    suspend fun updateTokenId(context: Context, value: String) =
        UserDataDatabase.getInstance(context).updateTokenId(value)

    suspend fun updateEmailOrPhoneNumber(context: Context, value: String) =
        UserDataDatabase.getInstance(context).updateEmailOrPhoneNumber(value)

    suspend fun updatePassword(context: Context, value: String) =
        UserDataDatabase.getInstance(context).updatePassword(value)

    suspend fun updateCurrencySymbol(context: Context, value: String) =
        UserDataDatabase.getInstance(context).updateCurrencySymbol(value)

    suspend fun updateIsGuestAccount(context: Context, value: Boolean) =
        UserDataDatabase.getInstance(context).updateIsGuestAccount(value)

    suspend fun saveUserData(context: Context, clientData: ClientDataModel, password: String?) =
        UserDataDatabase.getInstance(context).also {
            it.updateTokenId(clientData.tokenId)
            it.updateEmailOrPhoneNumber(clientData.phoneNumber)
            it.updatePassword(password)
            it.updateCurrencySymbol(clientData.currency?.name)
            it.updateIsGuestAccount(false)
        }

    suspend fun clearUserData(context: Context) =
        UserDataDatabase.getInstance(context).also {
            it.updateTokenId(null)
            it.updateEmailOrPhoneNumber(null)
            it.updatePassword(null)
            it.updateCurrencySymbol(null)
            it.updateIsGuestAccount(null)
        }


/*fun signUpAsGuest(
    fcmToken: String?,
) = liveData(kotlinx.coroutines.Dispatchers.IO) {
    emitSource(NetworkRequestHandler().handle(request = {
        return@handle Fetcher().getInstance(langTag)?.signUp(
            fcmToken = fcmToken,
            firstName = null,
            lastName = null,
            email = null,
            phoneNumber = null,
            password = null,
            isMale = null,
            countryId = null,
            cityId = null,
            zipCodeId = null,
        )
    }))
}*/

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

    fun login(
        fcmToken: String?,
        emailOrPhoneNumber: String?,
        password: String?,
    ) = liveData(kotlinx.coroutines.Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.login(
                fcmToken = fcmToken,
                emailOrPhoneNumber = emailOrPhoneNumber,
                password = password
            )
        }))
    }

    fun sendForgetPasswordCode(
        emailOrPhoneNumber: String?,
    ) = liveData(kotlinx.coroutines.Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)
                ?.sendForgetPasswordCode(
                    emailOrPhoneNumber = emailOrPhoneNumber
                )
        }))
    }

    fun verify(
        emailOrPhoneNumber: String?,
        verificationCode: String?,
    ) = liveData(kotlinx.coroutines.Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.verify(
                emailOrPhoneNumber = emailOrPhoneNumber,
                verificationCode = verificationCode
            )
        }))
    }

    fun resendVerification(
        emailOrPhoneNumber: String?,
    ) = liveData(kotlinx.coroutines.Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)
                ?.resendVerification(
                    emailOrPhoneNumber = emailOrPhoneNumber
                )
        }))
    }

    fun resetPassword(
        emailOrPhoneNumber: String?,
        password: String?,
    ) = liveData(kotlinx.coroutines.Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.resetPassword(
                emailOrPhoneNumber = emailOrPhoneNumber,
                password = password
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
        homeNumber: String?,
        streetName: String?,
        area: String?,
        isMainAddress: Boolean?,
    ) = liveData(kotlinx.coroutines.Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.addAddress(
                tokenId = tokenId,
                flat = flat,
                floor = floor,
                homeNumber = homeNumber,
                streetName = streetName,
                area = area,
                isMainAddress = isMainAddress,
            )
        }))
    }

    fun getLocalUserData(
        tokenId: String?,
    ) = liveData(kotlinx.coroutines.Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.getUserData(
                tokenId = tokenId,
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
        countryCode: String?,
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
                countryCode = countryCode,
                cityId = cityId,
                dateOfBirth = dateOfBirth
            )
        }))
    }

    fun changePassword(
        tokenId: String?,
        password: String?,
        oldPassword: String?
    ) = liveData(kotlinx.coroutines.Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)
                ?.changePassword(
                    tokenId = tokenId,
                    password = password,
                    oldPassword = oldPassword
                )
        }))
    }

    fun logout(
        fcmToken: String?,
    ) = liveData(kotlinx.coroutines.Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.logout(
                fcmToken = fcmToken,
            )
        }))
    }
}