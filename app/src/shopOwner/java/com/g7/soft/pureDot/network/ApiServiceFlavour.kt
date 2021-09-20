package com.g7.soft.pureDot.network

import com.g7.soft.pureDot.model.ApiResponseModel
import com.g7.soft.pureDot.model.MasterOrderModel
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface ApiServiceFlavour {

    // user account
    @FormUrlEncoded
    @POST("shopOwner/editUserData")
    suspend fun editUserData(
        @Field("tokenId") tokenId: String?,
        @Field("name") name: String?,
        @Field("phoneNumber") phoneNumber: String?,
        @Field("isMale") isMale: Boolean?,
        @Field("imageUrl") imageUrl: String?,
        @Field("cityId") cityId: String?,
        @Field("dateOfBirth") dateOfBirth: Long?,
        @Field("email") email: String?,
    ): ApiResponseModel<*>?

    @FormUrlEncoded
    @POST("shopOwner/getMyOrders")
    suspend fun getMyOrders(
        @Field("tokenId") tokenId: String?,
        @Field("pageNumber") pageNumber: Int?,
        @Field("itemsPerPage") itemsPerPage: Int?,
    ): ApiResponseModel<List<MasterOrderModel>>?

    @FormUrlEncoded
    @POST("shopOwner/getNewOrders")
    suspend fun getNewOrders(
        @Field("tokenId") tokenId: String?,
        @Field("pageNumber") pageNumber: Int?,
        @Field("itemsPerPage") itemsPerPage: Int?,
    ): ApiResponseModel<List<MasterOrderModel>>?

    @FormUrlEncoded
    @POST("shopOwner/getPendingOrders")
    suspend fun getPendingOrders(
        @Field("tokenId") tokenId: String?,
        @Field("pageNumber") pageNumber: Int?,
        @Field("itemsPerPage") itemsPerPage: Int?,
    ): ApiResponseModel<List<MasterOrderModel>>?
}