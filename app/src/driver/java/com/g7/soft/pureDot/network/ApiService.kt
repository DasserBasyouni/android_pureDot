package com.g7.soft.pureDot.network

import com.g7.soft.pureDot.model.*
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface ApiService {

    // user account
    @FormUrlEncoded
    @POST("driver/signUp")
    suspend fun signUp(
        @Field("firstName") firstName: String?,
        @Field("lastName") lastName: String?,
        @Field("email") email: String?,
        @Field("phoneNumber") phoneNumber: String?,
        @Field("password") password: String?,
        @Field("isMale") isMale: Boolean?,
        @Field("countryId") countryId: Int?,
        @Field("cityId") cityId: Int?,
        @Field("zipCodeId") zipCodeId: Int?,
        @Field("fcmToken") fcmToken: String?
    ): ApiResponseModel<DriverDataModel>?

    @FormUrlEncoded
    @POST("driver/login")
    suspend fun login(
        @Field("fcmToken") fcmToken: String?,
        @Field("username") username: String?,
        @Field("password") password: String?,
    ): ApiResponseModel<DriverDataModel>? // api note: this method is used instead of checkAccount so fcmToken might be sent as null and api won't save it for user

    @FormUrlEncoded
    @POST("driver/sendForgetPasswordCode")
    suspend fun sendForgetPasswordCode(
        @Field("emailOrPhoneNumber") emailOrPhoneNumber: String?,
    ): ApiResponseModel<*>?

    @FormUrlEncoded
    @POST("driver/verify")
    suspend fun verify(
        @Field("emailOrPhoneNumber") emailOrPhoneNumber: String?,
        @Field("verificationCode") verificationCode: String?,
    ): ApiResponseModel<DriverDataModel>?

    @FormUrlEncoded
    @POST("driver/resendVerification")
    suspend fun resendVerification(
        @Field("emailOrPhoneNumber") emailOrPhoneNumber: String?,
    ): ApiResponseModel<*>?

    @FormUrlEncoded
    @POST("driver/resetPassword")
    suspend fun resetPassword(
        @Field("emailOrPhoneNumber") emailOrPhoneNumber: String?,
        @Field("password") password: String?,
    ): ApiResponseModel<DriverDataModel>?

    @FormUrlEncoded
    @POST("driver/getAddresses")
    suspend fun getAddresses(
        @Field("tokenId") tokenId: String?,
    ): ApiResponseModel<List<AddressModel>>?

    @FormUrlEncoded
    @POST("driver/addAddress")
    suspend fun addAddress(
        @Field("tokenId") tokenId: String?,
        @Field("flat") flat: String?,
        @Field("floor") floor: String?,
        @Field("homeNumber") homeNumber: String?,
        @Field("streetName") streetName: String?,
        @Field("area") area: String?,
        @Field("isMainAddress") isMainAddress: Boolean?,
    ): ApiResponseModel<IdModel>?

    @FormUrlEncoded
    @POST("driver/getUserData")
    suspend fun getUserData(
        @Field("tokenId") tokenId: String?,
    ): ApiResponseModel<DriverDataModel>?

    @FormUrlEncoded
    @POST("driver/editUserData")
    suspend fun editUserData(
        @Field("tokenId") tokenId: String?,
        @Field("firstName") firstName: String?,
        @Field("lastName") lastName: String?,
        @Field("mobileNumber") phoneNumber: String?,
        @Field("isMale") isMale: Boolean?,
        @Field("imageUrl") imageUrl: String?,
        @Field("countryId") countryId: Int?,
        @Field("cityId") cityId: Int?,
        @Field("dateOfBirth") dateOfBirth: Long?,
        //@Field( "points") points: Int?, exclude this from api method too
        //@Field( "credit") credit: Double?, exclude this from api method too
        @Field("email") email: String?,
        @Field("countryCode") countryCode: String?,
    ): ApiResponseModel<*>?

    @FormUrlEncoded
    @POST("driver/changePassword")
    suspend fun changePassword(
        @Field("tokenId") tokenId: String?,
        @Field("password") password: String?,
    ): ApiResponseModel<DriverDataModel>?

    @FormUrlEncoded
    @POST("driver/logout")
    suspend fun logout(
        @Field("fcmToken") fcmToken: String?,
    ): ApiResponseModel<*>?

    @FormUrlEncoded
    @POST("driver/updateBankAccount")
    suspend fun updateBankAccount(
        @Field("tokenId") tokenId: String?,
        @Field("bankName") bankName: String?,
        @Field("iban") iban: String?,
    ): ApiResponseModel<*>?

    // general
    @POST("country/getAll")
    suspend fun getAllCounties(): ApiResponseModel<List<CountryModel>>?

    @FormUrlEncoded
    @POST("city/getAll")
    suspend fun getAllCities(@Field("countryId") countryId: Int?): ApiResponseModel<List<CityModel>>?

    @FormUrlEncoded
    @POST("zipCode/getAll")
    suspend fun getAllZipCodes(@Field("cityId") cityId: Int?): ApiResponseModel<List<ZipCodeModel>>?

    @FormUrlEncoded
    @POST("driver/contactUs")
    suspend fun contactUs(
        @Field("tokenId") tokenId: String?,
        @Field("email") email: String?,
        @Field("message") message: String?,
    ): ApiResponseModel<*>?

    // product
    @FormUrlEncoded
    @POST("driver/getProducts")
    suspend fun getProducts(
        @Field("categoriesIds") categoriesIds: List<Int>?,
        @Field("storesIds") storesIds: List<Int>?,
        @Field("minStarts") minStarts: List<Int>?,
        @Field("fromPrice") fromPrice: Int?,
        @Field("toPrice") toPrice: Int?,
        @Field("itemPerPage") itemPerPage: Int?,
        @Field("pageNumber") pageNumber: Int?,
        @Field("searchText") searchText: String?,
    ): ApiResponseModel<DataWithCountModel<List<ProductModel>>>?

    @FormUrlEncoded
    @POST("driver/getLatestOffers")
    suspend fun getLatestOffers(
        @Field("pageNumber") pageNumber: Int?,
        @Field("itemPerPage") itemPerPage: Int?,
        @Field("searchText") searchText: String?,
        @Field("shopId") shopId: Int?,
    ): ApiResponseModel<DataWithCountModel<List<ProductModel>>>?

    @FormUrlEncoded
    @POST("driver/getLatestProducts")
    suspend fun getLatestProducts(
        @Field("pageNumber") pageNumber: Int?,
        @Field("itemPerPage") itemPerPage: Int?,
        @Field("searchText") searchText: String?,
        @Field("shopId") shopId: Int?,
    ): ApiResponseModel<DataWithCountModel<List<ProductModel>>>?

    @FormUrlEncoded
    @POST("driver/getBestSelling")
    suspend fun getBestSelling(
        @Field("pageNumber") pageNumber: Int?,
        @Field("itemPerPage") itemPerPage: Int?,
        @Field("searchText") searchText: String?,
        @Field("shopId") shopId: Int?,
    ): ApiResponseModel<DataWithCountModel<List<ProductModel>>>?

    @FormUrlEncoded
    @POST("driver/rateItem")
    suspend fun rateItem(
        @Field("tokenId") tokenId: String?,
        @Field("rating") rating: Float?,
        @Field("itemId") itemId: Int?,
    ): ApiResponseModel<*>?

    // offers
    @FormUrlEncoded
    @POST("driver/getSliderOffers")
    suspend fun getSliderOffers(
        @Field("categoryId") categoryId: Int?,
        @Field("shopId") shopId: Int?,
        @Field("type") type: Int?,
    ): ApiResponseModel<List<SliderOfferModel>>?

    // stores
    @FormUrlEncoded
    @POST("driver/getShops")
    suspend fun getALlStores(
        @Field("pageNumber") pageNumber: Int?,
        @Field("itemPerPage") itemPerPage: Int?,
        @Field("searchText") searchText: String?,
        @Field("categoryId") categoryId: Int?,
    ): ApiResponseModel<DataWithCountModel<List<StoreModel>>>?

    // categories
    @FormUrlEncoded
    @POST("driver/getCategories")
    suspend fun getCategories(
        @Field("pageNumber") pageNumber: Int?,
        @Field("itemPerPage") itemPerPage: Int?,
        @Field("searchText") searchText: String?,
        @Field("shopId") shopId: Int?,
    ): ApiResponseModel<DataWithCountModel<List<CategoryModel>>>?

    // cart
    @FormUrlEncoded
    @POST("driver/editCartQuantity")
    suspend fun editCartQuantity(
        @Field("itemId") itemId: Int?,
        @Field("quantity") quantity: Int?,
        @Field("serviceDateTime") serviceDateTime: Long?,
    ): ApiResponseModel<TotalPriceInCartModel>?

    @FormUrlEncoded
    @POST("driver/getCartItems")
    suspend fun getCartItems(
        @Field("tokenId") tokenId: String?,
    ): ApiResponseModel<CartItemsModel>?

    @FormUrlEncoded
    @POST("driver/getItemDetails")
    suspend fun getItemDetails(
        @Field("itemId") itemId: Int?,
    ): ApiResponseModel<ProductDetailsModel>?

    @FormUrlEncoded
    @POST("driver/getItemReviews")
    suspend fun getItemReviews(
        @Field("itemId") itemId: Int?,
        @Field("tokenId") tokenId: String?,
        @Field("pageNumber") pageNumber: Int?,
        @Field("itemPerPage") itemPerPage: Int?,
    ): ApiResponseModel<DataWithCountModel<List<ReviewModel>>>?

    @FormUrlEncoded
    @POST("review/mark")
    suspend fun markReview(
        @Field("tokenId") tokenId: String?,
        @Field("reviewId") reviewId: Int?,
        @Field("isHelpful") isHelpful: Boolean?,
    ): ApiResponseModel<*>?

    @FormUrlEncoded
    @POST("driver/checkout")
    suspend fun checkout(
        @Field("tokenId") tokenId: String?,
        @Field("firstName") firstName: String?,
        @Field("lastName") lastName: String?,
        @Field("email") email: String?,
        @Field("phoneNumber") phoneNumber: String?,
        @Field("addressId") addressId: Int?,
    ): ApiResponseModel<ShippingCostModel>?

    @FormUrlEncoded
    @POST("driver/checkCoupon")
    suspend fun checkCoupon(
        @Field("tokenId") tokenId: String?,
        @Field("coupon") coupon: String?,
    ): ApiResponseModel<DiscountPercentageModel>?

    @FormUrlEncoded
    @POST("driver/checkoutIsPaid")
    suspend fun checkoutIsPaid(
        @Field("tokenId") tokenId: String?,
        @Field("paidAmount") paidAmount: Double?,
    ): ApiResponseModel<NumberModel>?

    @FormUrlEncoded
    @POST("driver/getWishList")
    suspend fun getWishList(
        @Field("tokenId") tokenId: String?,
    ): ApiResponseModel<List<ProductModel>>?

    // order
    @FormUrlEncoded
    @POST("driver/getMyOrders")
    suspend fun getMyOrders(
        @Field("tokenId") tokenId: String?,
    ): ApiResponseModel<List<OrderModel>>?

    @FormUrlEncoded
    @POST("driver/trackOrder")
    suspend fun trackOrder(
        @Field("tokenId") tokenId: String?,
        @Field("orderNumber") orderNumber: Int?,
    ): ApiResponseModel<OrderTrackingModel>?

    @FormUrlEncoded
    @POST("driver/cancelOrder")
    suspend fun cancelOrder(
        @Field("tokenId") tokenId: String?,
        @Field("orderNumber") orderNumber: Int?,
    ): ApiResponseModel<*>?

    // service
    @FormUrlEncoded
    @POST("driver/getServices")
    suspend fun getServices(
        @Field("tokenId") tokenId: String?,
        @Field("categoryId") categoryId: Int?,
        @Field("shopId") shopId: Int?,
        @Field("minStarts") minStarts: List<Int>?,
        @Field("fromPrice") fromPrice: Int?,
        @Field("toPrice") toPrice: Int?,
        @Field("pageNumber") pageNumber: Int?,
        @Field("itemPerPage") itemsPerPage: Int?,
    ): ApiResponseModel<List<ServiceModel>>?

    @FormUrlEncoded
    @POST("driver/rateComplainService")
    suspend fun getComplainComments(
        @Field("tokenId") tokenId: String?,
        @Field("rating") rating: Float?,
        @Field("message") message: String?,
    ): ApiResponseModel<*>?

    // wallet
    @FormUrlEncoded
    @POST("driver/getWalletData")
    suspend fun getWalletData(
        @Field("tokenId") tokenId: String?,
    ): ApiResponseModel<WalletDataModel>?

    @FormUrlEncoded
    @POST("driver/getTransactions")
    suspend fun getTransactions(
        @Field("tokenId") tokenId: String?,
        @Field("pageNumber") pageNumber: Int?,
        @Field("itemPerPage") itemsPerPage: Int?,
    ): ApiResponseModel<List<TransactionModel>>?

    @FormUrlEncoded
    @POST("driver/transferMoney")
    suspend fun transferMoney(
        @Field("tokenId") tokenId: String?,
        @Field("emailOrPhoneNumber") emailOrPhoneNumber: String?,
        @Field("amount") amount: Float?,
    ): ApiResponseModel<*>?

    @FormUrlEncoded
    @POST("driver/replacePoints")
    suspend fun replacePoints(
        @Field("tokenId") tokenId: String?,
    ): ApiResponseModel<*>?

    @FormUrlEncoded
    @POST("client/suggestContact")
    suspend fun suggestContact(
        @Field("tokenId") tokenId: String?,
        @Field("emailOrPhoneNumber") emailOrPhoneNumber: String?,
    ): ApiResponseModel<MutableList<*>>? // replace * with ContactModel in case of usage

    // notifications
    @FormUrlEncoded
    @POST("driver/getNotifications")
    suspend fun getNotifications(
        @Field("tokenId") tokenId: String?,
    ): ApiResponseModel<List<NotificationModel>>?

    @FormUrlEncoded
    @POST("driver/readNotifications")
    suspend fun readNotifications(
        @Field("tokenId") tokenId: String?,
        @Field("notificationsIds") notificationsIds: List<Int>?,
    ): ApiResponseModel<*>?

    @FormUrlEncoded
    @POST("driver/doNotify")
    suspend fun doNotify(
        @Field("tokenId") tokenId: String?,
        @Field("doNotify") doNotify: Boolean?,
    ): ApiResponseModel<*>?

    // complain
    @FormUrlEncoded
    @POST("driver/getComplains")
    suspend fun getComplains(
        @Field("tokenId") tokenId: String?,
    ): ApiResponseModel<List<ComplainModel>>?

    @FormUrlEncoded
    @POST("driver/addComplain")
    suspend fun addComplain(
        @Field("tokenId") tokenId: String?,
        @Field("title") title: String?,
        @Field("description") description: String?,
        @Field("relatedOrderNumber") relatedOrderNumber: Int?,
        @Field("categoryId") categoryId: Int?,
    ): ApiResponseModel<*>?

    @FormUrlEncoded
    @POST("driver/getComplainComments")
    suspend fun getComplainComments(
        @Field("tokenId") tokenId: String?,
        @Field("complainId") complainId: Int?,
    ): ApiResponseModel<List<CommentModel>>?

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

    @FormUrlEncoded
    @POST("driver/getWorkingHours")
    suspend fun getWorkingHours(
        @Field("tokenId") tokenId: String?,
    ): ApiResponseModel<WorkingTimesModel>?

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

    // orders
    @FormUrlEncoded
    @POST("driver/getPendingOrders")
    suspend fun getPendingOrders(
        @Field("tokenId") tokenId: String?,
        @Field("lat") lat: Double?,
        @Field("lng") lng: Double?,
    ): ApiResponseModel<MutableList<OrderModel>>?

    @FormUrlEncoded
    @POST("driver/changeOrderStatus")
    suspend fun changeOrderStatus(
        @Field("tokenId") tokenId: String?,
        @Field("orderNumber") orderNumber: Int?,
        @Field("status ") status: Int?,
    ): ApiResponseModel<*>?
}