package com.g7.soft.pureDot.network

import com.g7.soft.pureDot.BuildConfig
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okio.Buffer
import org.json.JSONObject
import java.io.IOException


class MockInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (BuildConfig.DEBUG) {

            val uri = chain.request().url.toUri().toString()

            // todo find a way to link names with ApiService class for less hard-coded Errors
            val responseString =
                JSONObject(
                    when {
                        // user
                        uri.contains("client/signUp") -> OfflineDemoRepository.userSignUp
                        uri.contains("client/login") -> OfflineDemoRepository.userLogin
                        uri.contains("client/sendForgetPasswordCode") -> OfflineDemoRepository.getSuccessfulStatus
                        uri.contains("client/verify") -> OfflineDemoRepository.userLogin
                        uri.contains("client/resendVerification") -> OfflineDemoRepository.getSuccessfulStatus
                        uri.contains("client/resetPassword") -> OfflineDemoRepository.userLogin
                        uri.contains("client/getAddresses") -> OfflineDemoRepository.getAddresses
                        uri.contains("client/addAddress") -> OfflineDemoRepository.getSuccessfulStatusWithId
                        uri.contains("client/getUserData") -> OfflineDemoRepository.userLogin
                        uri.contains("client/editUserData") -> OfflineDemoRepository.getSuccessfulStatus
                        uri.contains("client/changePassword") -> OfflineDemoRepository.userLogin
                        uri.contains("client/logout") -> OfflineDemoRepository.getSuccessfulStatus

                        // general
                        uri.contains("country/getAll") -> OfflineDemoRepository.getAllCounties
                        uri.contains("city/getAll") -> OfflineDemoRepository.getAllCities
                        uri.contains("zipCode/getAll") -> OfflineDemoRepository.getAllZipCodes
                        uri.contains("client/contactUs") -> OfflineDemoRepository.getSuccessfulStatus

                        // product
                        uri.contains("client/getProducts") -> OfflineDemoRepository.get10Products
                        uri.contains("client/getLatestOffers") -> OfflineDemoRepository.get4Products
                        uri.contains("client/getLatestProducts") -> OfflineDemoRepository.get9Products
                        uri.contains("client/getBestSelling") -> OfflineDemoRepository.get10Products
                        uri.contains("client/rateItem") -> OfflineDemoRepository.getSuccessfulStatus
                        uri.contains("client/getItemReviews") -> OfflineDemoRepository.getItemReviews
                        uri.contains("review/mark") -> OfflineDemoRepository.getSuccessfulStatus
                        uri.contains("client/getProductDetails") -> OfflineDemoRepository.getProductDetails

                        // offers
                        uri.contains("client/getSliderOffers") -> OfflineDemoRepository.getSliderOffers

                        // store
                        uri.contains("client/getShops") -> OfflineDemoRepository.getStores

                        // categories
                        uri.contains("client/getCategories") -> OfflineDemoRepository.getCategories

                        // cart
                        uri.contains("client/editCartQuantity") -> OfflineDemoRepository.editCartQuantity
                        uri.contains("client/getCartItems") -> OfflineDemoRepository.getCartItems
                        uri.contains("client/checkoutIsPaid") -> OfflineDemoRepository.checkoutIsPaid
                        uri.contains("client/checkout") -> OfflineDemoRepository.checkout
                        uri.contains("client/checkCoupon") -> OfflineDemoRepository.checkCoupon
                        uri.contains("client/getWishList") -> OfflineDemoRepository.getWishList

                        // order
                        uri.contains("client/getMyOrders") -> OfflineDemoRepository.getMyOrders
                        uri.contains("client/trackOrder") -> OfflineDemoRepository.trackOrder
                        uri.contains("client/cancelOrder") -> OfflineDemoRepository.getSuccessfulStatus
                        uri.contains("client/rateOrder") -> OfflineDemoRepository.getSuccessfulStatus

                        // service
                        uri.contains("client/getServices") -> OfflineDemoRepository.getServices
                        uri.contains("client/rateComplainService") -> OfflineDemoRepository.getSuccessfulStatus
                        uri.contains("client/getServiceDetails") -> OfflineDemoRepository.getServiceDetails

                        // wallet
                        uri.contains("client/getWalletData") -> OfflineDemoRepository.getWalletData
                        uri.contains("client/getTransactions") -> OfflineDemoRepository.getTransactions
                        uri.contains("client/transferMoney") -> OfflineDemoRepository.getSuccessfulStatus
                        uri.contains("client/suggestContact") -> OfflineDemoRepository.suggestContact
                        uri.contains("client/replacePoints") -> OfflineDemoRepository.getSuccessfulStatus

                        // notifications
                        uri.contains("client/getNotifications") -> OfflineDemoRepository.getNotifications
                        uri.contains("client/readNotifications") -> OfflineDemoRepository.getSuccessfulStatus
                        uri.contains("client/doNotify") -> OfflineDemoRepository.getSuccessfulStatus

                        // complain
                        uri.contains("client/getComplains") -> OfflineDemoRepository.getComplains
                        uri.contains("client/addComplain") -> OfflineDemoRepository.getSuccessfulStatus
                        uri.contains("client/getComplainComments") -> OfflineDemoRepository.getComplainComments

                        else -> hashMapOf()
                    }
                ).toString()

            return chain.proceed(chain.request())
                .newBuilder()
                .code(200)
                .protocol(Protocol.HTTP_2)
                .message(responseString)
                .body(
                    ResponseBody.create(
                        "application/json".toMediaTypeOrNull(),
                        responseString.toByteArray()
                    )
                )
                .addHeader("content-type", "application/json")
                .build()
        } else {
            //just to be on safe side.
            throw IllegalAccessError(
                "MockInterceptor is only meant for Testing Purposes and " +
                        "bound to be used only with DEBUG mode"
            )
        }
    }

    private fun bodyToString(request: RequestBody?): String? {
        return try {
            val buffer = Buffer()
            request?.writeTo(buffer)
            buffer.readUtf8()
        } catch (e: IOException) {
            "did not work"
        }
    }

}