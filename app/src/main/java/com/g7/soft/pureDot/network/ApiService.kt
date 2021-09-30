package com.g7.soft.pureDot.network

import com.g7.soft.pureDot.BuildConfig
import com.g7.soft.pureDot.model.*
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Path


interface ApiService : ApiServiceFlavour {

    // user account
    @FormUrlEncoded
    @POST("{flavour}/login")
    suspend fun login(
        @Path("flavour") flavour: String = BuildConfig.FLAVOR,
        @Field("fcmToken") fcmToken: String?,
        @Field("emailOrPhoneNumber") emailOrPhoneNumber: String?,
        @Field("password") password: String?,
    ): ApiResponseModel<UserDataModel>? // api note: this method is used instead of checkAccount so fcmToken might be sent as null and api won't save it for user

    @FormUrlEncoded
    @POST("{flavour}/forgetPassword")
    suspend fun sendForgetPasswordCode(
        @Path("flavour") flavour: String = BuildConfig.FLAVOR,
        @Field("emailOrPhoneNumber") emailOrPhoneNumber: String?,
    ): ApiResponseModel<*>?

    @FormUrlEncoded
    @POST("{flavour}/verify")
    suspend fun verify(
        @Path("flavour") flavour: String = BuildConfig.FLAVOR,
        @Field("emailOrPhoneNumber") emailOrPhoneNumber: String?,
        @Field("verificationCode") verificationCode: String?,
    ): ApiResponseModel<UserDataModel>?

    @FormUrlEncoded
    @POST("{flavour}/resendVerification")
    suspend fun resendVerification(
        @Path("flavour") flavour: String = BuildConfig.FLAVOR,
        @Field("emailOrPhoneNumber") emailOrPhoneNumber: String?,
    ): ApiResponseModel<*>?

    @FormUrlEncoded
    @POST("{flavour}/getUserData")
    suspend fun getUserData(
        @Path("flavour") flavour: String = BuildConfig.FLAVOR,
        @Field("tokenId") tokenId: String?,
    ): ApiResponseModel<UserDataModel>?

    @FormUrlEncoded
    @POST("client/resetPassword")
    suspend fun resetPassword(
        @Field("emailOrPhoneNumber") emailOrPhoneNumber: String?,
        @Field("password") password: String?,
    ): ApiResponseModel<UserDataModel>?

    @FormUrlEncoded
    @POST("{flavour}/changePassword")
    suspend fun changePassword(
        @Path("flavour") flavour: String = BuildConfig.FLAVOR,
        @Field("tokenId") tokenId: String?,
        @Field("password") password: String?,
        @Field("oldPassword") oldPassword: String?,
    ): ApiResponseModel<UserDataModel>?

    @FormUrlEncoded
    @POST("{flavour}/logout")
    suspend fun logout(
        @Path("flavour") flavour: String = BuildConfig.FLAVOR,
        @Field("fcmToken") fcmToken: String?,
    ): ApiResponseModel<*>?

    // general
    @POST("client/getAppCurrency")
    suspend fun getAppCurrency(): ApiResponseModel<CurrencyModel>

    @FormUrlEncoded
    @POST("client/changeLang")
    suspend fun changeLanguage(
        @Field("fcmToken") fcmToken: String?,
        @Field("language") language: Int?
    ): ApiResponseModel<*>?

    @POST("country/getAll")
    suspend fun getAllCounties(): ApiResponseModel<List<CountryModel>>?

    @FormUrlEncoded
    @POST("city/getAll")
    suspend fun getAllCities(@Field("countryId") countryId: String?): ApiResponseModel<List<CityModel>>?

    @FormUrlEncoded
    @POST("zipCode/getAll")
    suspend fun getAllZipCodes(@Field("cityId") cityId: String?): ApiResponseModel<List<ZipCodeModel>>?

    @FormUrlEncoded
    @POST("{flavour}/contactUs")
    suspend fun contactUs(
        @Path("flavour") flavour: String = BuildConfig.FLAVOR,
        @Field("name") name: String?,
        @Field("email") email: String?,
        @Field("message") message: String?,
    ): ApiResponseModel<*>?

    // order
    @FormUrlEncoded // todo in api mock
    @POST("{flavour}/getComplaintOrder")
    suspend fun getComplaintOrder(
        @Path("flavour") flavour: String = BuildConfig.FLAVOR,
        @Field("tokenId") tokenId: String?,
    ): ApiResponseModel<List<ComplaintOrderModel>>?

    @FormUrlEncoded
    @POST("{flavour}/changeOrderStatus")
    suspend fun changeOrderStatus(
        @Path("flavour") flavour: String = BuildConfig.FLAVOR,
        @Field("tokenId") tokenId: String?,
        @Field("orderId") orderId: String?,
        @Field("status") status: Int?,
        @Field("isReturn") isReturn: Boolean?,
    ): ApiResponseModel<*>?

    // wallet
    @FormUrlEncoded
    @POST("{flavour}/getWalletData")
    suspend fun getWalletData(
        @Path("flavour") flavour: String = BuildConfig.FLAVOR,
        @Field("tokenId") tokenId: String?,
    ): ApiResponseModel<WalletDataModel>?

    @FormUrlEncoded
    @POST("{flavour}/getTransactions")
    suspend fun getTransactions(
        @Path("flavour") flavour: String = BuildConfig.FLAVOR,
        @Field("tokenId") tokenId: String?,
        @Field("pageNumber") pageNumber: Int?,
        @Field("itemsPerPage") itemsPerPage: Int?,
    ): ApiResponseModel<List<TransactionModel>>?

    // notifications
    @FormUrlEncoded
    @POST("{flavour}/getNotifications")
    suspend fun getNotifications(
        @Path("flavour") flavour: String = BuildConfig.FLAVOR,
        @Field("tokenId") tokenId: String?,
    ): ApiResponseModel<List<NotificationModel>>?

    @FormUrlEncoded
    @POST("{flavour}/doNotify")
    suspend fun doNotify(
        @Path("flavour") flavour: String = BuildConfig.FLAVOR,
        @Field("tokenId") tokenId: String?,
        @Field("doNotify") doNotify: Boolean?,
    ): ApiResponseModel<*>?

    // complain
    @FormUrlEncoded
    @POST("{flavour}/getComplains")
    suspend fun getComplains(
        @Path("flavour") flavour: String = BuildConfig.FLAVOR,
        @Field("tokenId") tokenId: String?,
    ): ApiResponseModel<List<ComplainModel>>?

    @FormUrlEncoded
    @POST("{flavour}/addComplain")
    suspend fun addComplain(
        @Path("flavour") flavour: String = BuildConfig.FLAVOR,
        @Field("tokenId") tokenId: String?,
        @Field("title") title: String?,
        @Field("description") description: String?,
        @Field("relatedOrderNumber") orderId: String?,
        @Field("reasonType") reasonType: String?,
    ): ApiResponseModel<*>?

    @FormUrlEncoded
    @POST("{flavour}/getComplainComments")
    suspend fun getComplainComments(
        @Path("flavour") flavour: String = BuildConfig.FLAVOR,
        @Field("tokenId") tokenId: String?,
        @Field("complainId") complainId: String?,
    ): ApiResponseModel<MutableList<CommentModel>>?

    @POST("client/getComplainReasons")
    suspend fun getComplaintReasons(): ApiResponseModel<List<IdNameModel>>?

    @FormUrlEncoded // todo add to mock api
    @POST("{flavour}/addComplaintReply")
    suspend fun addComplaintReply(
        @Path("flavour") flavour: String = BuildConfig.FLAVOR,
        @Field("message") message: String?,
        @Field("complainId") complainId: String?,
        @Field("tokenId") tokenId: String?,
    ): ApiResponseModel<CommentModel?>?

    @FormUrlEncoded // todo add to mock api
    @POST("{flavour}/rateComplainService")
    suspend fun rateComplainService(
        @Path("flavour") flavour: String = BuildConfig.FLAVOR,
        @Field("message") message: String?,
        @Field("complainId") complainId: String?,
        @Field("tokenId") tokenId: String?,
        @Field("rating") rating: Int?,
    ): ApiResponseModel<String?>?

    // payment
    @FormUrlEncoded
    @POST("payment/StcPayAuth")
    suspend fun authenticateStcPay( // todo add to mock api
        @Field("mobile") mobile: String?,
        @Field("orderNumber") masterOrderNumber: Int?,
        @Field("orderId") masterOrderId: String?,
        @Field("orderAmount") orderAmount: Double?,
        @Field("description") description: String?
    ): ApiResponseModel<StcPayAuthModel>?

    @FormUrlEncoded
    @POST("payment/stcPayConfirm")
    suspend fun confirmStcPay( // todo add to mock api
        @Field("stcpayPmtReference") stcPayPmtReference: String?,
        @Field("otpValue") otpValue: String?,
        @Field("otpReference") otpReference: String?,
        @Field("payType") payType: Int?
    ): ApiResponseModel<*>?

    @FormUrlEncoded
    @POST("payment/createMasterCardSession")
    suspend fun createMasterCardSession( // todo add to mock api
        @Field("orderId") masterOrderId: String?,
        @Field("orderAmount") orderAmount: Double?,
        @Field("description") description: String?
    ): ApiResponseModel<SessionModel>?
}