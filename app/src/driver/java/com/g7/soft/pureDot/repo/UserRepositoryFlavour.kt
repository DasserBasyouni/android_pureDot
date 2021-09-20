package com.g7.soft.pureDot.repo

import android.content.Context
import androidx.lifecycle.liveData
import com.g7.soft.pureDot.data.database.userData.UserDataDatabase
import com.g7.soft.pureDot.model.UserDataModel
import com.g7.soft.pureDot.network.Fetcher
import com.g7.soft.pureDot.network.NetworkRequestHandler
import kotlinx.coroutines.Dispatchers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

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

    fun signUp(
        fcmToken: String?,
        name: String?,
        phoneNumber: String?,
        email: String?,
        cityId: String?,
        dateOfBirth: Long?,
        carBrand: String?,
        isMale: Boolean?,
        licenceImagePath: String?,
        carFrontImagePath: String?,
        carBackImagePath: String?,
        nationalIdImagePath: String?,
    ) = liveData(Dispatchers.IO) {

        val licenceImageFile = if (licenceImagePath == null) null else File(licenceImagePath)
        val licenceImage: MultipartBody.Part? =
            if (licenceImageFile == null) null else MultipartBody.Part.createFormData(
                "licenceImage",
                licenceImageFile.name,
                licenceImageFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            )

        val carFrontImageFile = if (carFrontImagePath == null) null else File(carFrontImagePath)
        val carFrontImage: MultipartBody.Part? =
            if (carFrontImageFile == null) null else MultipartBody.Part.createFormData(
                "carFrontImage",
                carFrontImageFile.name,
                carFrontImageFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            )

        val carBackImageFile = if (carBackImagePath == null) null else File(carBackImagePath)
        val carBackImage: MultipartBody.Part? =
            if (carBackImageFile == null) null else MultipartBody.Part.createFormData(
                "carBackImage",
                carBackImageFile.name,
                carBackImageFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            )

        val nationalIdImageFile = if (nationalIdImagePath == null) null else File(nationalIdImagePath)
        val nationalIdImage: MultipartBody.Part? =
            if (nationalIdImageFile == null) null else MultipartBody.Part.createFormData(
                "nationalIdImage",
                nationalIdImageFile.name,
                nationalIdImageFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            )

        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.signUp(
                fcmToken = fcmToken,
                name = name,
                email = email,
                phoneNumber = phoneNumber,
                cityId = cityId,
                isMale = isMale,
                birthDate = dateOfBirth,
                carBrand = carBrand,
                licenceImage = licenceImage,
                carFrontImage = carFrontImage,
                carBackImage = carBackImage,
                nationalIdImage = nationalIdImage,
            )
        }))
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

    fun updateBankAccount(
        tokenId: String?,
        bankName: String?,
        iban: String?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.updateBankAccount(
                tokenId = tokenId,
                bankName = bankName,
                iban = iban
            )
        }))
    }
}