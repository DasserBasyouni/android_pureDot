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
                        uri.contains("driver/signUp") -> OfflineDemoRepository.userSignUp
                        uri.contains("driver/login") -> OfflineDemoRepository.userLogin
                        uri.contains("driver/sendForgetPasswordCode") -> OfflineDemoRepository.getSuccessfulStatus
                        uri.contains("driver/verify") -> OfflineDemoRepository.userLogin
                        uri.contains("driver/resendVerification") -> OfflineDemoRepository.getSuccessfulStatus
                        uri.contains("driver/resetPassword") -> OfflineDemoRepository.userLogin
                        uri.contains("driver/getAddresses") -> OfflineDemoRepository.getAddresses
                        uri.contains("driver/addAddress") -> OfflineDemoRepository.getSuccessfulStatusWithId
                        uri.contains("driver/getUserData") -> OfflineDemoRepository.userLogin
                        uri.contains("driver/editUserData") -> OfflineDemoRepository.getSuccessfulStatus
                        uri.contains("driver/changePassword") -> OfflineDemoRepository.userLogin
                        uri.contains("driver/logout") -> OfflineDemoRepository.getSuccessfulStatus
                        uri.contains("driver/updateBankAccount") -> OfflineDemoRepository.getSuccessfulStatus

                        // Availability
                        uri.contains("driver/checkAvailability") -> OfflineDemoRepository.checkAvailability
                        uri.contains("driver/changeAvailability") -> OfflineDemoRepository.getSuccessfulStatus
                        uri.contains("driver/getWorkingHours") -> OfflineDemoRepository.getWorkingHours
                        uri.contains("driver/addEditWorkingHours") -> OfflineDemoRepository.getSuccessfulStatus

                        // general
                        uri.contains("country/getAll") -> OfflineDemoRepository.getAllCounties
                        uri.contains("city/getAll") -> OfflineDemoRepository.getAllCities
                        uri.contains("zipCode/getAll") -> OfflineDemoRepository.getAllZipCodes
                        uri.contains("driver/contactUs") -> OfflineDemoRepository.getSuccessfulStatus

                        // product
                        uri.contains("driver/getProducts") -> OfflineDemoRepository.get10Products
                        uri.contains("driver/getLatestOffers") -> OfflineDemoRepository.get4Products
                        uri.contains("driver/getLatestProducts") -> OfflineDemoRepository.get9Products
                        uri.contains("driver/getBestSelling") -> OfflineDemoRepository.get10Products
                        uri.contains("driver/rateItem") -> OfflineDemoRepository.getSuccessfulStatus
                        uri.contains("driver/getItemReviews") -> OfflineDemoRepository.getItemReviews
                        uri.contains("review/mark") -> OfflineDemoRepository.getSuccessfulStatus

                        // offers
                        uri.contains("driver/getSliderOffers") -> OfflineDemoRepository.getSliderOffers

                        // store
                        uri.contains("driver/getShops") -> OfflineDemoRepository.getStores

                        // categories
                        uri.contains("driver/getCategories") -> OfflineDemoRepository.getCategories

                        // cart
                        uri.contains("driver/editCartQuantity") -> OfflineDemoRepository.editCartQuantity
                        uri.contains("driver/getCartItems") -> OfflineDemoRepository.getCartItems
                        uri.contains("driver/getItemDetails") -> OfflineDemoRepository.getItemDetails
                        uri.contains("driver/checkout") -> OfflineDemoRepository.checkout
                        uri.contains("driver/checkCoupon") -> OfflineDemoRepository.checkCoupon
                        uri.contains("driver/checkoutIsPaid") -> OfflineDemoRepository.getSuccessfulStatusWithNumber
                        uri.contains("driver/getWishList") -> OfflineDemoRepository.getWishList

                        // order
                        uri.contains("driver/getPendingOrders") -> OfflineDemoRepository.getPendingOrders
                        uri.contains("driver/getMyOrders") -> OfflineDemoRepository.getMyOrders
                        uri.contains("driver/cancelOrder") -> OfflineDemoRepository.getSuccessfulStatus
                        uri.contains("driver/changeOrderStatus") -> OfflineDemoRepository.getSuccessfulStatus

                        // service
                        uri.contains("driver/getServices") -> OfflineDemoRepository.getServices
                        uri.contains("driver/rateComplainService") -> OfflineDemoRepository.getSuccessfulStatus

                        // wallet
                        uri.contains("driver/getWalletData") -> OfflineDemoRepository.getWalletData
                        uri.contains("driver/getTransactions") -> OfflineDemoRepository.getTransactions
                        uri.contains("driver/transferMoney") -> OfflineDemoRepository.getSuccessfulStatus
                        uri.contains("driver/replacePoints") -> OfflineDemoRepository.getSuccessfulStatus

                        // notifications
                        uri.contains("driver/getNotifications") -> OfflineDemoRepository.getNotifications
                        uri.contains("driver/readNotifications") -> OfflineDemoRepository.getSuccessfulStatus
                        uri.contains("driver/doNotify") -> OfflineDemoRepository.getSuccessfulStatus

                        // complain
                        uri.contains("driver/getComplains") -> OfflineDemoRepository.getComplains
                        uri.contains("driver/addComplain") -> OfflineDemoRepository.getSuccessfulStatus
                        uri.contains("driver/getComplainComments") -> OfflineDemoRepository.getComplainComments

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