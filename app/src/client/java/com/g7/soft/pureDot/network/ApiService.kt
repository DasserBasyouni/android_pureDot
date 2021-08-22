package com.g7.soft.pureDot.network

import com.g7.soft.pureDot.model.*
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface ApiService {

    // user account
    @FormUrlEncoded
    @POST("client/signUp")
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
    ): ApiResponseModel<ClientDataModel>?

    @FormUrlEncoded
    @POST("client/login")
    suspend fun login(
        @Field("fcmToken") fcmToken: String?,
        @Field("username") username: String?,
        @Field("password") password: String?,
    ): ApiResponseModel<ClientDataModel>? // api note: this method is used instead of checkAccount so fcmToken might be sent as null and api won't save it for user

    @FormUrlEncoded
    @POST("client/sendForgetPasswordCode")
    suspend fun sendForgetPasswordCode(
        @Field("emailOrPhoneNumber") emailOrPhoneNumber: String?,
    ): ApiResponseModel<*>?

    @FormUrlEncoded
    @POST("client/verify")
    suspend fun verify(
        @Field("emailOrPhoneNumber") emailOrPhoneNumber: String?,
        @Field("verificationCode") verificationCode: String?,
    ): ApiResponseModel<ClientDataModel>?

    @FormUrlEncoded
    @POST("client/resendVerification")
    suspend fun resendVerification(
        @Field("emailOrPhoneNumber") emailOrPhoneNumber: String?,
    ): ApiResponseModel<*>?

    @FormUrlEncoded
    @POST("client/resetPassword")
    suspend fun resetPassword(
        @Field("emailOrPhoneNumber") emailOrPhoneNumber: String?,
        @Field("password") password: String?,
    ): ApiResponseModel<ClientDataModel>?

    @FormUrlEncoded
    @POST("client/getAddresses")
    suspend fun getAddresses(
        @Field("tokenId") tokenId: String?,
    ): ApiResponseModel<List<AddressModel>>?

    @FormUrlEncoded
    @POST("client/addAddress")
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
    @POST("client/getUserData")
    suspend fun getUserData(
        @Field("tokenId") tokenId: String?,
    ): ApiResponseModel<ClientDataModel>?

    @FormUrlEncoded
    @POST("client/editUserData")
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
    @POST("client/changePassword")
    suspend fun changePassword(
        @Field("tokenId") tokenId: String?,
        @Field("password") password: String?,
    ): ApiResponseModel<ClientDataModel>?

    @FormUrlEncoded
    @POST("client/logout")
    suspend fun logout(
        @Field("fcmToken") fcmToken: String?,
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
    @POST("client/contactUs")
    suspend fun contactUs(
        @Field("tokenId") tokenId: String?,
        @Field("email") email: String?,
        @Field("message") message: String?,
    ): ApiResponseModel<*>?

    // product
    @FormUrlEncoded
    @POST("client/getProducts")
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
    @POST("client/getLatestOffers")
    suspend fun getLatestOffers(
        @Field("pageNumber") pageNumber: Int?,
        @Field("itemPerPage") itemPerPage: Int?,
        @Field("searchText") searchText: String?,
        @Field("shopId") shopId: Int?,
    ): ApiResponseModel<DataWithCountModel<List<ProductModel>>>?

    @FormUrlEncoded
    @POST("client/getLatestProducts")
    suspend fun getLatestProducts(
        @Field("pageNumber") pageNumber: Int?,
        @Field("itemPerPage") itemPerPage: Int?,
        @Field("searchText") searchText: String?,
        @Field("shopId") shopId: Int?,
    ): ApiResponseModel<DataWithCountModel<List<ProductModel>>>?

    @FormUrlEncoded
    @POST("client/getBestSelling")
    suspend fun getBestSelling(
        @Field("pageNumber") pageNumber: Int?,
        @Field("itemPerPage") itemPerPage: Int?,
        @Field("searchText") searchText: String?,
        @Field("shopId") shopId: Int?,
    ): ApiResponseModel<DataWithCountModel<List<ProductModel>>>?

    @FormUrlEncoded
    @POST("client/rateOrder")
    suspend fun rateOrder(
        @Field("tokenId") tokenId: String?,
        @Field("orderNumber") orderNumber: Int?,
        @Field("orderRating") orderRating: Float?,
        @Field("orderComment") orderComment: String?,
        @Field("deliveryRating") deliveryRating: Float?,
        @Field("deliveryComment") deliveryComment: String?,
    ): ApiResponseModel<*>?

    @FormUrlEncoded
    @POST("client/getProductDetails")
    suspend fun getProductDetails(
        @Field("productId") productId: Int?,
    ): ApiResponseModel<ProductDetailsModel>?

    // offers
    @FormUrlEncoded
    @POST("client/getSliderOffers")
    suspend fun getSliderOffers(
        @Field("categoryId") categoryId: Int?,
        @Field("shopId") shopId: Int?,
        @Field("type") type: Int?,
    ): ApiResponseModel<List<SliderOfferModel>>?

    // stores
    @FormUrlEncoded
    @POST("client/getShops")
    suspend fun getALlStores(
        @Field("pageNumber") pageNumber: Int?,
        @Field("itemPerPage") itemPerPage: Int?,
        @Field("searchText") searchText: String?,
        @Field("categoryId") categoryId: Int?,
    ): ApiResponseModel<DataWithCountModel<List<StoreModel>>>?

    // categories
    @FormUrlEncoded
    @POST("client/getCategories")
    suspend fun getCategories(
        @Field("pageNumber") pageNumber: Int?,
        @Field("itemPerPage") itemPerPage: Int?,
        @Field("searchText") searchText: String?,
        @Field("shopId") shopId: Int?,
    ): ApiResponseModel<DataWithCountModel<List<CategoryModel>>>?

    // cart
    /*@FormUrlEncoded
    @POST("client/editCartQuantity")
    suspend fun editCartQuantity(
        @Field("itemId") itemId: Int?,
        @Field("quantity") quantity: Int?,
        @Field("serviceDateTime") serviceDateTime: Long?,
    ): ApiResponseModel<TotalPriceInCartModel>?*/

    @FormUrlEncoded
    @POST("client/checkCartProducts")
    suspend fun checkCartProducts(
        @Field("tokenId") tokenId: String?,
        @Field("ids") ids: List<Int>?,
        @Field("quantities") quantities: List<Int>?,
    ): ApiResponseModel<List<ProductModel>>?

    @FormUrlEncoded
    @POST("client/getCartItems")
    suspend fun getCartItems(
        @Field("tokenId") tokenId: String?,
    ): ApiResponseModel<CartItemsModel>?

    @FormUrlEncoded
    @POST("client/getItemReviews")
    suspend fun getItemReviews(
        @Field("itemId") itemId: Int?,
        @Field("tokenId") tokenId: String?,
        @Field("pageNumber") pageNumber: Int?,
        @Field("itemPerPage") itemPerPage: Int?,
    ): ApiResponseModel<DataWithCountModel<List<ReviewModel>>>?

    /*@FormUrlEncoded
    @POST("review/mark")
    suspend fun markReview(
        @Field("tokenId") tokenId: String?,
        @Field("reviewId") reviewId: Int?,
        @Field("isHelpful") isHelpful: Boolean?,
    ): ApiResponseModel<*>?*/

    @FormUrlEncoded
    @POST("client/addProductReview")
    suspend fun addProductReview(
        @Field("tokenId") tokenId: String?,
        @Field("productId") productId: Int?,
        @Field("rating") rating: Float?,
        @Field("comment") comment: String?,
    ): ApiResponseModel<ReviewModel>?

    @FormUrlEncoded
    @POST("client/editWishList")
    suspend fun editWishList(
        @Field("tokenId") tokenId: String?,
        @Field("productId") productId: Int?,
        @Field("doAdd") doAdd: Boolean?,
    ): ApiResponseModel<*>?

    @FormUrlEncoded
    @POST("client/checkout")
    suspend fun checkout(
        @Field("tokenId") tokenId: String?,
        @Field("firstName") firstName: String?,
        @Field("lastName") lastName: String?,
        @Field("email") email: String?,
        @Field("phoneNumber") phoneNumber: String?,
        @Field("addressId") addressId: Int?,
    ): ApiResponseModel<ShippingCostModel>?

    @FormUrlEncoded
    @POST("client/checkCoupon")
    suspend fun checkCoupon(
        @Field("tokenId") tokenId: String?,
        @Field("coupon") coupon: String?,
    ): ApiResponseModel<DiscountPercentageModel>?

    @FormUrlEncoded
    @POST("client/checkoutIsPaid")
    suspend fun checkoutIsPaid(
        @Field("tokenId") tokenId: String?,
        @Field("paidAmount") paidAmount: Double?,
    ): ApiResponseModel<MasterOrderModel>?

    @FormUrlEncoded
    @POST("client/getWishList")
    suspend fun getWishList(
        @Field("tokenId") tokenId: String?,
    ): ApiResponseModel<List<ProductModel>>?

    // order
    @FormUrlEncoded
    @POST("client/getMyOrders")
    suspend fun getMyOrders(
        @Field("tokenId") tokenId: String?,
    ): ApiResponseModel<List<MasterOrderModel>>?

    @FormUrlEncoded
    @POST("client/trackOrder")
    suspend fun trackOrder(
        @Field("tokenId") tokenId: String?,
        @Field("orderNumber") orderNumber: Int?,
    ): ApiResponseModel<OrderTrackingModel>?

    @FormUrlEncoded
    @POST("client/cancelOrder")
    suspend fun cancelOrder(
        @Field("tokenId") tokenId: String?,
        @Field("orderNumber") orderNumber: Int?,
    ): ApiResponseModel<*>?

    @FormUrlEncoded
    @POST("client/calculateRefundShipping")
    suspend fun calculateRefundShipping(
        @Field("orderNumber") orderNumber: Int?,
        @Field("productId") productId: Int?,
        @Field("shippingMethod") shippingMethod: Int?,
    ): ApiResponseModel<ShippingCostModel>?

    @FormUrlEncoded
    @POST("client/refund")
    suspend fun refund(
        @Field("orderNumber") orderNumber: Int?,
        @Field("productId") productId: Int?,
        @Field("shippingMethod") shippingMethod: Int?,
    ): ApiResponseModel<*>?

    // service
    @FormUrlEncoded
    @POST("client/getServices")
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
    @POST("client/rateComplainService")
    suspend fun getComplainComments(
        @Field("tokenId") tokenId: String?,
        @Field("rating") rating: Float?,
        @Field("message") message: String?,
    ): ApiResponseModel<*>?

    @FormUrlEncoded
    @POST("client/getServiceDetails")
    suspend fun getServiceDetails(
        @Field("serviceId") serviceId: Int?,
    ): ApiResponseModel<ServiceDetailsModel>?

    @FormUrlEncoded
    @POST("client/addServiceReview")
    suspend fun addServiceReview(
        @Field("tokenId") tokenId: String?,
        @Field("productId") productId: Int?,
        @Field("rating") rating: Float?,
        @Field("comment") comment: String?,
    ): ApiResponseModel<ReviewModel>?

    // wallet
    @FormUrlEncoded
    @POST("client/getWalletData")
    suspend fun getWalletData(
        @Field("tokenId") tokenId: String?,
    ): ApiResponseModel<WalletDataModel>?

    @FormUrlEncoded
    @POST("client/getTransactions")
    suspend fun getTransactions(
        @Field("tokenId") tokenId: String?,
        @Field("pageNumber") pageNumber: Int?,
        @Field("itemPerPage") itemsPerPage: Int?,
    ): ApiResponseModel<List<TransactionModel>>?

    @FormUrlEncoded
    @POST("client/transferMoney")
    suspend fun transferMoney(
        @Field("tokenId") tokenId: String?,
        @Field("emailOrPhoneNumber") emailOrPhoneNumber: String?,
        @Field("amount") amount: Float?,
    ): ApiResponseModel<*>?

    @FormUrlEncoded
    @POST("client/suggestContact")
    suspend fun suggestContact(
        @Field("tokenId") tokenId: String?,
        @Field("emailOrPhoneNumber") emailOrPhoneNumber: String?,
    ): ApiResponseModel<MutableList<ContactModel>>?

    @FormUrlEncoded
    @POST("client/replacePoints")
    suspend fun replacePoints(
        @Field("tokenId") tokenId: String?,
    ): ApiResponseModel<*>?

    // notifications
    @FormUrlEncoded
    @POST("client/getNotifications")
    suspend fun getNotifications(
        @Field("tokenId") tokenId: String?,
    ): ApiResponseModel<List<NotificationModel>>?

    @FormUrlEncoded
    @POST("client/readNotifications")
    suspend fun readNotifications(
        @Field("tokenId") tokenId: String?,
        @Field("notificationsIds") notificationsIds: List<Int>?,
    ): ApiResponseModel<*>?

    @FormUrlEncoded
    @POST("client/doNotify")
    suspend fun doNotify(
        @Field("tokenId") tokenId: String?,
        @Field("doNotify") doNotify: Boolean?,
    ): ApiResponseModel<*>?

    // complain
    @FormUrlEncoded
    @POST("client/getComplains")
    suspend fun getComplains(
        @Field("tokenId") tokenId: String?,
    ): ApiResponseModel<List<ComplainModel>>?

    @FormUrlEncoded
    @POST("client/addComplain")
    suspend fun addComplain(
        @Field("tokenId") tokenId: String?,
        @Field("title") title: String?,
        @Field("description") description: String?,
        @Field("relatedOrderNumber") relatedOrderNumber: Int?,
        @Field("categoryId") categoryId: Int?,
    ): ApiResponseModel<*>?

    @FormUrlEncoded
    @POST("client/getComplainComments")
    suspend fun getComplainComments(
        @Field("tokenId") tokenId: String?,
        @Field("complainId") complainId: Int?,
    ): ApiResponseModel<List<CommentModel>>?

}