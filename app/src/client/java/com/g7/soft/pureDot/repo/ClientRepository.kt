package com.g7.soft.pureDot.repo

import androidx.lifecycle.liveData
import com.g7.soft.pureDot.network.Fetcher
import com.g7.soft.pureDot.network.NetworkRequestHandler

class ClientRepository(private val langTag: String) {

    /*suspend fun saveTokenId(context: Context, value: String) {
        Log.e("Z_", "saveTokenId 1")

        context.userDataStore.updateData { data -> data.toBuilder().setTokenId(value).build() }

        Log.e("Z_", "saveTokenId 2")

        val exampleCounterFlow: Flow<Unit> = context.userDataStore.data
            .map { settings ->
                Log.e("Z_", "entered")

                Log.e("Z_", "se: $settings")
                // The exampleCounter property is generated from the proto schema.
                Log.e("Z_", "tokenId: ${settings.tokenId}")
            }

        Log.e("Z_", "saveTokenId 3")
    }*/


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

    fun getUserData(
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
        countryId: String?,
        cityId: String?,
        dateOfBirth: Long?,
    ) = liveData(kotlinx.coroutines.Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.editUserData(
                tokenId = tokenId,
                firstName = firstName,
                lastName = lastName,
                phoneNumber = phoneNumber,
                isMale = isMale,
                imageUrl = imageUrl,
                email = email,
                countryCode = countryCode,
                countryId = countryId,
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