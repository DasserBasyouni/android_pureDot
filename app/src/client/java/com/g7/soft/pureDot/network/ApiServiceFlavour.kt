package com.g7.soft.pureDot.network

import com.g7.soft.pureDot.model.*
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface ApiServiceFlavour {

    // general
    @POST("client/getSignUpFields")
    suspend fun getSignUpFields(): ApiResponseModel<SignUpFieldsModel>?

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
        @Field("cityId") cityId: String?,
        @Field("zipCodeId") zipCodeId: String?,
        @Field("fcmToken") fcmToken: String?
    ): ApiResponseModel<String?>?

    @FormUrlEncoded
    @POST("client/getAddresses")
    suspend fun getAddresses(
        @Field("tokenId") tokenId: String?,
    ): ApiResponseModel<MutableList<AddressModel>>?

    @FormUrlEncoded
    @POST("client/addAddress")
    suspend fun addAddress(
        @Field("tokenId") tokenId: String?,
        @Field("flat") flat: String?,
        @Field("floor") floor: String?,
        @Field("streetName") streetName: String?,
        @Field("isMainAddress") isMainAddress: Boolean?,
        @Field("latitude") latitude: Double?,
        @Field("longitude") longitude: Double?,
        @Field("areaName") areaName: String?,
        @Field("cityId") cityId: String?,
        @Field("zipCodeId") zipCodeId: String?,
        @Field("buildingNumber") buildingNumber: String?,
    ): ApiResponseModel<String>?

    @FormUrlEncoded
    @POST("client/editUserData")
    suspend fun editUserData(
        @Field("tokenId") tokenId: String?,
        @Field("firstName") firstName: String?,
        @Field("lastName") lastName: String?,
        @Field("phoneNumber") phoneNumber: String?,
        @Field("isMale") isMale: Boolean?,
        @Field("imageUrl") imageUrl: String?,
        @Field("cityId") cityId: String?,
        @Field("zipCodeId") zipCodeId: String?,
        @Field("dateOfBirth") dateOfBirth: Long?,
        //@Field( "points") points: Int?, exclude this from api method too
        //@Field( "credit") credit: Double?, exclude this from api method too
        @Field("email") email: String?,
    ): ApiResponseModel<*>?

    // product
    @FormUrlEncoded
    @POST("product/getProducts")
    suspend fun getProducts(
        @Field("categoriesIds") categoriesIds: List<String>?,
        @Field("storesIds") storesIds: List<String>?,
        @Field("minStars") minStars: List<Int>?,
        @Field("fromPrice") fromPrice: Int?,
        @Field("toPrice") toPrice: Int?,
        @Field("itemsPerPage") itemsPerPage: Int?,
        @Field("pageNumber") pageNumber: Int?,
        @Field("searchText") searchText: String?,
    ): ApiResponseModel<List<ProductModel>>?

    @FormUrlEncoded
    @POST("product/getLatestOffers")
    suspend fun getLatestOffers(
        @Field("pageNumber") pageNumber: Int?,
        @Field("itemsPerPage") itemsPerPage: Int?,
        @Field("shopId") shopId: String?,
    ): ApiResponseModel<DataWithCountModel<List<ProductModel>>>?

    @FormUrlEncoded
    @POST("product/getLatestProducts")
    suspend fun getLatestProducts(
        @Field("pageNumber") pageNumber: Int?,
        @Field("itemsPerPage") itemsPerPage: Int?,
        @Field("shopId") shopId: String?,
    ): ApiResponseModel<DataWithCountModel<List<ProductModel>>>?

    @FormUrlEncoded
    @POST("product/getBestSelling")
    suspend fun getBestSelling(
        @Field("pageNumber") pageNumber: Int?,
        @Field("itemsPerPage") itemsPerPage: Int?,
        @Field("shopId") shopId: String?,
    ): ApiResponseModel<DataWithCountModel<List<ProductModel>>>?

    @FormUrlEncoded
    @POST("client/rateOrder")
    suspend fun rateOrder(
        @Field("tokenId") tokenId: String?,
        @Field("orderId") orderId: String?,
        @Field("orderRating") orderRating: Int?,
        @Field("orderComment") orderComment: String?,
        @Field("deliveryRating") deliveryRating: Int?,
        @Field("deliveryComment") deliveryComment: String?,
    ): ApiResponseModel<*>?

    @FormUrlEncoded
    @POST("product/getProductDetails")
    suspend fun getProductDetails(
        @Field("tokenId") tokenId: String?,
        @Field("productId") productId: String?,
    ): ApiResponseModel<ProductDetailsModel>?

    @FormUrlEncoded
    @POST("client/getProductReviews")
    suspend fun getProductReviews(
        @Field("productId") productId: String?,
        @Field("pageNumber") pageNumber: Int?,
        @Field("itemsPerPage") itemsPerPage: Int?,
    ): ApiResponseModel<DataWithCountModel<List<ReviewModel>>>?

    @FormUrlEncoded
    @POST("product/getProductCost") // todo in mock api
    suspend fun getProductCost(
        @Field("productId") productId: String?,
        @Field("variations[]") variations: ArrayList<String>?
    ): ApiResponseModel<Double>?

    // offers
    @FormUrlEncoded
    @POST("client/getSliderOffers")
    suspend fun getSliderOffers(
        @Field("tokenId") tokenId: String?,
        @Field("categoryId") categoryId: String?,
        @Field("shopId") shopId: String?,
        @Field("type") type: Int?,
    ): ApiResponseModel<List<SliderOfferModel>>?

    // stores
    @FormUrlEncoded
    @POST("shop/getAll")
    suspend fun getALlStores(
        @Field("pageNumber") pageNumber: Int?,
        @Field("itemsPerPage") itemsPerPage: Int?,
        @Field("searchText") searchText: String?,
        @Field("categoryId") categoryId: String?,
    ): ApiResponseModel<DataWithCountModel<List<StoreModel>>>?

    // categories
    @FormUrlEncoded
    @POST("category/getAll")
    suspend fun getCategories(
        @Field("pageNumber") pageNumber: Int?,
        @Field("itemsPerPage") itemsPerPage: Int?,
        @Field("searchText") searchText: String?,
        @Field("shopId") shopId: String?,
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
    @POST("client/addProductReview")
    suspend fun addProductReview(
        @Field("tokenId") tokenId: String?,
        @Field("productId") productId: String?,
        @Field("rating") rating: Int?,
        @Field("review") comment: String?,
    ): ApiResponseModel<ReviewModel>?

    @FormUrlEncoded
    @POST("client/AddItemToWishList")
    suspend fun editWishList(
        @Field("tokenId") tokenId: String?,
        @Field("itemId") productId: String?,
        @Field("isFavorite") doAdd: Boolean?,
    ): ApiResponseModel<*>?

    @FormUrlEncoded
    @POST("orders/checkoutIsPaid")
    suspend fun checkoutIsPaid(
        @Field("tokenId") tokenId: String?,
        @Field("orderId") orderId: String?,
        @Field("isPaid") isPaid: Boolean?,
    ): ApiResponseModel<*>?

    @FormUrlEncoded // todo add to mock api
    @POST("orders/cancelOrder")
    suspend fun cancelMasterOrder(
        @Field("tokenId") tokenId: String?,
        @Field("masterOrderId") masterOrderId: String?,
    ): ApiResponseModel<*>?

    @FormUrlEncoded
    @POST("client/getWishList")
    suspend fun getWishList(
        @Field("tokenId") tokenId: String?,
        @Field("pageNumber") pageNumber: Int?,
        @Field("itemsPerPage") itemsPerPage: Int?,
    ): ApiResponseModel<List<ProductModel>>?

    // order
    @POST("orders/checkCartItems")
    suspend fun checkCartItems(@Body body: RequestBody): ApiResponseModel<MasterOrderModel>?

    @POST("orders/checkout")
    suspend fun checkout(@Body body: RequestBody): ApiResponseModel<CheckoutResponseModel>?

    @FormUrlEncoded
    @POST("client/getMasterOrder")
    suspend fun getMasterOrder(
        @Field("tokenId") tokenId: String?,
        @Field("masterOrderId") id: String?
    ): ApiResponseModel<MasterOrderModel>?

    @FormUrlEncoded
    @POST("client/getMyOrders")
    suspend fun getMyOrders(
        @Field("tokenId") tokenId: String?,
        @Field("pageNumber") pageNumber: Int?,
        @Field("itemsPerPage") itemsPerPage: Int?,
    ): ApiResponseModel<List<MasterOrderModel>>?

    @FormUrlEncoded // todo update in mock api (output & url)
    @POST("orders/trackOrder")
    suspend fun trackOrder(
        @Field("tokenId") tokenId: String?,
        @Field("orderId") orderId: String?,
    ): ApiResponseModel<List<OrderTrackingModel>>?

    @POST("client/getShippingMethods") // todo add to mock api
    suspend fun getShippingMethods(@Body body: RequestBody): ApiResponseModel<List<ShippingMethodModel>>?

    @FormUrlEncoded
    @POST("orders/calculateRefundShipping")
    suspend fun calculateRefundShipping(
        @Field("detailsId") detailsId: String?,
        @Field("shippingMethod") shippingMethod: String?,
    ): ApiResponseModel<ShippingCostModel>?

    @FormUrlEncoded
    @POST("orders/refund")
    suspend fun returnItem(
        @Field("shippingMethod") shippingMethod: String?,
        @Field("detailsId") detailsId: String?,
        @Field("itemTotalPrice") itemTotalPrice: Double?,
        @Field("refundAmount") refundAmount: Double?,
        @Field("shippingCost") shippingCost: Double?,
        @Field("commission") commission: Double?,
        @Field("vat") vat: Double?,
        @Field("driverEarning") driverEarning: Double?,
        @Field("driverVat") driverVat: Double?,
        @Field("deliveryCommissionVat") deliveryCommissionVat: Double?,
    ): ApiResponseModel<*>?

    // service
    @FormUrlEncoded
    @POST("product/getServices")
    suspend fun getServices(
        @Field("tokenId") tokenId: String?,
        @Field("categoryId") categoryId: String?,
        @Field("minStars") minStars: List<Int>?,
        @Field("pageNumber") pageNumber: Int?,
        @Field("itemsPerPage") itemsPerPage: Int?,
    ): ApiResponseModel<List<ServiceModel>>?

    @FormUrlEncoded
    @POST("client/rateComplainService")
    suspend fun getComplainComments(
        @Field("tokenId") tokenId: String?,
        @Field("rating") rating: Int?,
        @Field("message") message: String?,
    ): ApiResponseModel<*>?

    @FormUrlEncoded
    @POST("product/getServiceDetails")
    suspend fun getServiceDetails(
        @Field("tokenId") tokenId: String?,
        @Field("serviceId") serviceId: String?,
    ): ApiResponseModel<ServiceDetailsModel>?

    @FormUrlEncoded
    @POST("client/addServiceReview")
    suspend fun addServiceReview(
        @Field("tokenId") tokenId: String?,
        @Field("productId") productId: String?,
        @Field("rating") rating: Int?,
        @Field("review") comment: String?,
    ): ApiResponseModel<ReviewModel>?

    @FormUrlEncoded
    @POST("client/getServiceReviews")
    suspend fun getServiceReviews(
        @Field("serviceId") productId: String?,
        @Field("pageNumber") pageNumber: Int?,
        @Field("itemsPerPage") itemsPerPage: Int?,
    ): ApiResponseModel<DataWithCountModel<List<ReviewModel>>>?

    @POST("product/getServiceCost") // todo in mock api
    suspend fun getServiceCost(
        @Body body: RequestBody?
    ): ApiResponseModel<Double>?

    @FormUrlEncoded
    @POST("client/transferMoney")
    suspend fun transferMoney(
        @Field("tokenId") tokenId: String?,
        @Field("emailOrPhoneNumber") emailOrPhoneNumber: String?,
        @Field("amount") amount: Float?,
    ): ApiResponseModel<*>?

    @FormUrlEncoded
    @POST("client/GetTransferCustomer")
    suspend fun suggestContact(
        @Field("tokenId") tokenId: String?,
        @Field("emailOrPhoneNumber") emailOrPhoneNumber: String?,
    ): ApiResponseModel<ContactModel?>?

    @FormUrlEncoded
    @POST("client/replacePoints")
    suspend fun replacePoints(
        @Field("tokenId") tokenId: String?,
        @Field("amount") amount: Int?,
    ): ApiResponseModel<*>?
}