package com.g7.soft.pureDot.ui.screen.checkout

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.g7.soft.pureDot.model.*
import com.g7.soft.pureDot.model.project.LceeModel
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.CartRepository
import com.g7.soft.pureDot.repo.OrderRepository
import com.g7.soft.pureDot.repo.UserRepository
import com.kofigyan.stateprogressbar.StateProgressBar
import kotlinx.coroutines.Dispatchers

/*val cartItemsResponse = MediatorLiveData<NetworkRequestResponse<CartItemsModel>>()
val cartItemsLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }

val totalPrice // add delivry
    get() = Transformations.map(cartItemsResponse) {
        it.data?.products?.sumOf { product ->
            product.quantityInCart!! * (product.priceWithDiscount ?: 0.00)
        }?.plus(it.data.products.sumOf { product ->
            product.quantityInCart!! * (product.vat ?: 0.00)
        })
    }*/

class CheckoutViewModel(
    var masterOrder: MasterOrderModel?,
    var productApiShopOrder: List<ApiShopOrderModel>?,
    var serviceId: String?,
    var serviceBranchId: String?,
    var serviceShopId: String?,
    var serviceVariations: List<ServiceVariationValueModel>?,
    var serviceQuantity: Int,
) : ViewModel() {

    private val isProductCheckout = serviceId == null


    // checkout 1
    val currentStateNumber = MediatorLiveData<StateProgressBar.StateNumber>().apply {
        this.value = StateProgressBar.StateNumber.ONE
    }

    val addressesResponse = MediatorLiveData<NetworkRequestResponse<MutableList<AddressModel>?>?>()
    val selectedAddressPosition = MutableLiveData<Int?>().apply { this.value = 0 }

    val userDataResponse = MediatorLiveData<NetworkRequestResponse<UserDataModel>>()
    val userDataLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }

    val firstName = MutableLiveData<String?>()
    val lastName = MutableLiveData<String?>()
    val email = MutableLiveData<String?>()
    val phoneNumber = MutableLiveData<String?>()

    val shippingMethodsResponse =
        MediatorLiveData<NetworkRequestResponse<List<ShippingMethodModel>?>?>()
    val shippingMethodsLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }


    fun getShippingMethods(langTag: String) {
        shippingMethodsResponse.value = NetworkRequestResponse.loading()
        shippingMethodsResponse.apply {
            this.addSource(OrderRepository(langTag).getShippingMethods()) {
                shippingMethodsResponse.value = it
            }
        }
    }

    fun getAddresses(langTag: String, tokenId: String?) {
        addressesResponse.value = NetworkRequestResponse.loading()
        addressesResponse.apply {
            this.addSource(UserRepository(langTag).getAddresses(tokenId = tokenId)) {
                addressesResponse.value = it
                selectedAddressPosition.value =
                    it.data?.indexOfFirst { address -> address.isMainAddress == true }?.plus(1) ?: 0
            }
        }
    }

    fun getUserData(langTag: String, tokenId: String?) {
        userDataResponse.value = NetworkRequestResponse.loading()
        userDataResponse.apply {
            this.addSource(UserRepository(langTag).getUserData(tokenId = tokenId)) {
                userDataResponse.value = it
                firstName.value = it.data?.firstName
                lastName.value = it.data?.lastName
                email.value = it.data?.email
                phoneNumber.value = it.data?.phoneNumber
            }
        }
    }

    // checkout 2
    val selectedShippingMethodId = MutableLiveData<String?>()
    val haveSelectedShippingMethod = MutableLiveData<Boolean?>()


    val orderLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }.also {
        it.value?.response?.value = NetworkRequestResponse.success(data = masterOrder)
    }
    val orderResponse = MediatorLiveData<NetworkRequestResponse<MasterOrderModel>>().also {
        it.value = NetworkRequestResponse.success(data = masterOrder)
    }


    fun checkCartItems(langTag: String, tokenId: String?) {
        orderResponse.value = NetworkRequestResponse.loading()

        orderResponse.apply {
            addSource(
                OrderRepository(langTag).checkCartItems(
                    tokenId = tokenId,
                    serviceId = serviceId,
                    addressId = selectedAddress?.id,
                    shippingId = selectedShippingMethodId.value,
                    couponCode = couponCode.value,
                    servantsCount = orderResponse.value?.data?.servants,
                    apiShopOrders = if (isProductCheckout)
                        productApiShopOrder
                    else
                        listOf(ApiShopOrderModel(
                            shopId = serviceShopId,
                            branchId = serviceBranchId,
                            items = mutableListOf<ApiShopOrderItemModel>().apply {
                                for (variation in serviceVariations ?: listOf())
                                    this.add(
                                        ApiShopOrderItemModel(
                                            productId = variation.productId,
                                            sourceId = variation.sourceId,
                                            stockId = variation.stockId,
                                            categoryId = variation.categoryId,
                                            quantity = serviceQuantity,
                                            variationsIds = listOf()
                                        )
                                    )
                            }
                        ))
                )
            ) {
                orderResponse.value = it
            }
        }
    }

    /*fun getCartItems(langTag: String, tokenId: String?) {
        cartItemsResponse.value = NetworkRequestResponse.loading()

        // fetch request
        cartItemsResponse.apply {
            addSource(
                CartRepository(langTag).getCartItems(
                    tokenId = tokenId
                )
            ) { cartItemsResponse.value = it }
        }
    }*/

    // checkout 3
    val isCashOnDelivery = MutableLiveData<Boolean?>().apply { this.value = true }
    val isStcPayChecked = MutableLiveData<Boolean?>().apply { this.value = true }
    val isMasterCardChecked = MutableLiveData<Boolean?>().apply { this.value = false }


    // checkout 4
    val selectedAddress
        get() = addressesResponse.value?.data?.getOrNull(
            (selectedAddressPosition.value ?: 0) - 1
        )


    val couponCode = MutableLiveData<String?>()


    fun checkout(langTag: String, tokenId: String?) =
        liveData(Dispatchers.IO) {
            emit(NetworkRequestResponse.loading())

            emitSource(
                OrderRepository(langTag).checkout(
                    tokenId = tokenId,
                    shippingId = selectedShippingMethodId.value,
                    addressId = selectedAddress?.id,
                    couponCode = couponCode.value,
                    firstName = firstName.value,
                    lastName = lastName.value,
                    email = email.value,
                    phoneNumber = phoneNumber.value,
                    serviceId = serviceId,
                    servantsCount = orderResponse.value?.data?.servants,
                    apiShopOrders = if (isProductCheckout) productApiShopOrder
                    else listOf(ApiShopOrderModel(
                        shopId = serviceShopId,
                        branchId = serviceBranchId,
                        items = mutableListOf<ApiShopOrderItemModel>().apply {
                            for (variation in serviceVariations ?: listOf())
                                this.add(
                                    ApiShopOrderItemModel(
                                        productId = variation.productId,
                                        sourceId = variation.sourceId,
                                        stockId = variation.stockId,
                                        categoryId = variation.categoryId,
                                        quantity = serviceQuantity,
                                        variationsIds = listOf()
                                    )
                                )
                        }
                    ))
                )
            )
        }

    fun checkoutIsPaid(langTag: String, tokenId: String?, isPaid: Boolean) =
        liveData(Dispatchers.IO) {
            emit(NetworkRequestResponse.loading())

            emitSource(
                CartRepository(langTag).checkoutIsPaid(
                    tokenId = tokenId,
                    orderId = orderResponse.value?.data?.id,
                    isPaid = isPaid,
                )
            )
        }
}