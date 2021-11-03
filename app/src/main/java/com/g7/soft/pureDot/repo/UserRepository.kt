package com.g7.soft.pureDot.repo

import android.content.Context
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.liveData
import com.g7.soft.pureDot.data.database.userData.UserDataDatabase
import com.g7.soft.pureDot.model.UserDataModel
import com.g7.soft.pureDot.network.Fetcher
import com.g7.soft.pureDot.network.NetworkRequestHandler
import kotlinx.coroutines.flow.first

class UserRepository(private val langTag: String): UserRepositoryFlavour(langTag) {

    /*suspend fun getLocalUserData(context: Context) =
        UserDataDatabase.getInstance(context).userDataFlow.first()*/

    suspend fun getTokenId(context: Context): String? {
        val tokenId = UserDataDatabase.getInstance(context).userDataFlow.first().tokenId
        return if (tokenId.isEmpty()) null else tokenId
    }

    suspend fun getEmailOrPhoneNumber(context: Context): String? {
        val emailOrPhoneNumber = UserDataDatabase.getInstance(context).userDataFlow.first().emailOrPhoneNumber
        return if (emailOrPhoneNumber.isEmpty()) null else emailOrPhoneNumber
    }

    suspend fun getPassword(context: Context): String? {
        val password = UserDataDatabase.getInstance(context).userDataFlow.first().password
        return if (password.isEmpty()) null else password
    }

    suspend fun getCurrencySymbol(context: Context): String =
        UserDataDatabase.getInstance(context).userDataFlow.first().currencySymbol

    suspend fun updateTokenId(context: Context, value: String?) =
        UserDataDatabase.getInstance(context).updateTokenId(value)

    suspend fun updateEmailOrPhoneNumber(context: Context, value: String?) =
        UserDataDatabase.getInstance(context).updateEmailOrPhoneNumber(value)

    suspend fun updatePassword(context: Context, value: String?) =
        UserDataDatabase.getInstance(context).updatePassword(value)

    suspend fun updateCurrencySymbol(context: Context, value: String?) =
        UserDataDatabase.getInstance(context).updateCurrencySymbol(value)

    suspend fun saveUserData(context: Context, userData: UserDataModel?, password: String?) =
        UserDataDatabase.getInstance(context).also {
            it.updateTokenId(userData?.tokenId)
            it.updateEmailOrPhoneNumber(userData?.phoneNumber)
            it.updatePassword(password)
            it.updateIsGuestAccount(false)
            saveUserDataFlavour(context, userData, password)
        }

    suspend fun clearUserData(lifecycleScope: LifecycleCoroutineScope, context: Context) =
        UserDataDatabase.getInstance(context).also {
            it.updateTokenId(null)
            it.updateEmailOrPhoneNumber(null)
            it.updatePassword(null)
            it.updateIsGuestAccount(null)
            clearUserDataFlavour(lifecycleScope, context)
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

    fun getUserData(
        tokenId: String?,
    ) = liveData(kotlinx.coroutines.Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.getUserData(
                tokenId = tokenId,
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