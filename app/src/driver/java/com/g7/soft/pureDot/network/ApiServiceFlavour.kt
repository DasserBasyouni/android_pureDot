package com.g7.soft.pureDot.network

import com.g7.soft.pureDot.model.ApiResponseModel
import com.g7.soft.pureDot.model.DriverAvailabilityModel
import com.g7.soft.pureDot.model.MasterOrderModel
import com.g7.soft.pureDot.model.WorkingHourModel
import okhttp3.MultipartBody
import retrofit2.http.*


interface ApiServiceFlavour {

    // user account
    @Multipart
    @POST("driver/signUp")
    suspend fun signUp(
        @Part("name") name: String?,
        @Part("email") email: String?,
        @Part("phoneNumber") phoneNumber: String?,
        @Part("cityId") cityId: String?,
        @Part("isMale") isMale: Boolean?,
        @Part("birthDate") birthDate: Long?,
        @Part("carBrand") carBrand: String?,
        @Part("fcmToken") fcmToken: String?,
        //@Part("profileImage") profileImage: MultipartBody.Part?,
        @Part licenceImage: MultipartBody.Part?,
        @Part carFrontImage: MultipartBody.Part?,
        @Part carBackImage: MultipartBody.Part?,
        @Part nationalIdImage: MultipartBody.Part?,
    ): ApiResponseModel<String>?

    @FormUrlEncoded
    @POST("driver/editUserData")
    suspend fun editUserData(
        @Field("tokenId") tokenId: String?,
        @Field("name") name: String?,
        @Field("phoneNumber") phoneNumber: String?,
        @Field("isMale") isMale: Boolean?,
        @Field("imageUrl") imageUrl: String?,
        @Field("cityId") cityId: String?,
        @Field("dateOfBirth") dateOfBirth: Long?,
        //@Field( "points") points: Int?, exclude this from api method too
        //@Field( "credit") credit: Double?, exclude this from api method too
        @Field("email") email: String?,
    ): ApiResponseModel<*>?

    @FormUrlEncoded
    @POST("driver/updateBankAccount")
    suspend fun updateBankAccount(
        @Field("tokenId") tokenId: String?,
        @Field("bankName") bankName: String?,
        @Field("iban") iban: String?,
    ): ApiResponseModel<*>?

    // order
    @FormUrlEncoded
    @POST("driver/getMyOrders")
    suspend fun getMyOrders(
        @Field("tokenId") tokenId: String?,
        @Field("itemsPerPage") itemsPerPage: Int?,
        @Field("pageNumber") pageNumber: Int?,
    ): ApiResponseModel<List<MasterOrderModel>>?

    @FormUrlEncoded
    @POST("driver/getPendingOrders")
    suspend fun getPendingOrders(
        @Field("tokenId") tokenId: String?,
        @Field("itemsPerPage") itemsPerPage: Int?,
        @Field("pageNumber") pageNumber: Int?,
    ): ApiResponseModel<MutableList<MasterOrderModel>>?

    // wallet
    @FormUrlEncoded // todo add to mock api
    @POST("driver/addMoney")
    suspend fun addMoney(
        @Field("tokenId") tokenId: String?,
        @Field("amount") amount: Int?,
    ): ApiResponseModel<*>?

    // Availability
    @FormUrlEncoded
    @POST("driver/checkAvailability")
    suspend fun checkAvailability(
        @Field("tokenId") tokenId: String?,
    ): ApiResponseModel<DriverAvailabilityModel>?

    @FormUrlEncoded
    @POST("driver/changeAvailability")
    suspend fun changeAvailability(
        @Field("tokenId") tokenId: String?,
        @Field("isAvailable") isAvailable: Boolean?,
    ): ApiResponseModel<*>?

    @FormUrlEncoded // todo update output in mock api
    @POST("driver/getWorkingHours")
    suspend fun getWorkingHours(
        @Field("tokenId") tokenId: String?,
    ): ApiResponseModel<List<WorkingHourModel>>?

    @FormUrlEncoded
    @POST("driver/addEditWorkingHours")
    suspend fun addEditWorkingHours(
        @Field("tokenId") tokenId: String?,
        @Field("saturday") saturday: List<WorkingHourModel>?,
        @Field("sunday") sunday: List<WorkingHourModel>?,
        @Field("monday") monday: List<WorkingHourModel>?,
        @Field("tuesday") tuesday: List<WorkingHourModel>?,
        @Field("wednesday") wednesday: List<WorkingHourModel>?,
        @Field("thursday") thursday: List<WorkingHourModel>?,
    ): ApiResponseModel<*>?
}