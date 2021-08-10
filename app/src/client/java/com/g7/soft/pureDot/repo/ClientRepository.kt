package com.g7.soft.pureDot.repo

import com.g7.soft.pureDot.network.Fetcher
import com.g7.soft.pureDot.network.NetworkRequestHandler

class ClientRepository(private val langTag: String) {

    fun signUpAsGuest(
        fcmToken: String?,
    ) = androidx.lifecycle.liveData(kotlinx.coroutines.Dispatchers.IO) {
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
    }

    fun signUp(
        fcmToken: String?,
        firstName: String?,
        lastName: String?,
        phoneNumber: String?,
        email: String?,
        countryId: Int?,
        cityId: Int?,
        zipCodeId: Int?,
        password: String?,
        isMale: Boolean?,
    ) = androidx.lifecycle.liveData(kotlinx.coroutines.Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.signUp(
                fcmToken = fcmToken,
                firstName = firstName,
                lastName = lastName,
                phoneNumber = phoneNumber,
                email = email,
                countryId = countryId,
                cityId = cityId,
                zipCodeId = zipCodeId,
                password = password,
                isMale = isMale,
            )
        }))
    }

    fun login(
        fcmToken: String?,
        username: String?,
        password: String?,
    ) = androidx.lifecycle.liveData(kotlinx.coroutines.Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.login(
                fcmToken = fcmToken,
                username = username,
                password = password
            )
        }))
    }

    fun sendForgetPasswordCode(
        emailOrPhoneNumber: String?,
    ) = androidx.lifecycle.liveData(kotlinx.coroutines.Dispatchers.IO) {
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
    ) = androidx.lifecycle.liveData(kotlinx.coroutines.Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.verify(
                emailOrPhoneNumber = emailOrPhoneNumber,
                verificationCode = verificationCode
            )
        }))
    }

    fun resendVerification(
        emailOrPhoneNumber: String?,
    ) = androidx.lifecycle.liveData(kotlinx.coroutines.Dispatchers.IO) {
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
    ) = androidx.lifecycle.liveData(kotlinx.coroutines.Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.resetPassword(
                emailOrPhoneNumber = emailOrPhoneNumber,
                password = password
            )
        }))
    }

    fun getAddresses(
        tokenId: String?,
    ) = androidx.lifecycle.liveData(kotlinx.coroutines.Dispatchers.IO) {
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
    ) = androidx.lifecycle.liveData(kotlinx.coroutines.Dispatchers.IO) {
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
    ) = androidx.lifecycle.liveData(kotlinx.coroutines.Dispatchers.IO) {
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
        countryId: Int?,
        cityId: Int?,
        dateOfBirth: Long?,
    ) = androidx.lifecycle.liveData(kotlinx.coroutines.Dispatchers.IO) {
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
    ) = androidx.lifecycle.liveData(kotlinx.coroutines.Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)
                ?.changePassword(
                    tokenId = tokenId,
                    password = password,
                )
        }))
    }

    fun logout(
        fcmToken: String?,
    ) = androidx.lifecycle.liveData(kotlinx.coroutines.Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.logout(
                fcmToken = fcmToken,
            )
        }))
    }
}