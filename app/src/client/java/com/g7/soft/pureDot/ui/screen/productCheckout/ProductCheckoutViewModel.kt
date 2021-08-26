package com.g7.soft.pureDot.ui.screen.productCheckout

import androidx.lifecycle.*
import com.g7.soft.pureDot.model.*
import com.g7.soft.pureDot.model.project.LceeModel
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.CartRepository
import com.g7.soft.pureDot.repo.ClientRepository
import com.g7.soft.pureDot.util.ValidationUtils
import com.kofigyan.stateprogressbar.StateProgressBar
import kotlinx.coroutines.Dispatchers

class ProductCheckoutViewModel(
    private val service: ServiceModel?,
    private val selectedVariations: IntArray?,
    private val servantsNumber: Int?,
    private val time: Long?,
    private val date: Long?,
    private val quantity: Int?,
    val currency: String?, // todo delete
    internal val storesProductsCartDetails: MutableList<StoreProductsCartDetailsModel>?,
) : ViewModel() {

    // service data
    internal val isProductCheckout = service == null

    // checkout 1
    val currentStateNumber = MediatorLiveData<StateProgressBar.StateNumber>().apply {
        this.value = StateProgressBar.StateNumber.ONE
    }

    val addressesResponse = MediatorLiveData<NetworkRequestResponse<List<AddressModel>?>?>()
    val selectedAddressPosition = MutableLiveData<Int?>().apply { this.value = 0 }

    val userDataResponse = MediatorLiveData<NetworkRequestResponse<ClientDataModel>>()
    val userDataLcee = MediatorLiveData<LceeModel>().apply { this.value = LceeModel() }

    val firstName = MutableLiveData<String?>()
    val lastName = MutableLiveData<String?>()
    val email = MutableLiveData<String?>()
    val phoneNumber = MutableLiveData<String?>()


    fun getAddresses(langTag: String, tokenId: String) {
        addressesResponse.value = NetworkRequestResponse.loading()
        addressesResponse.apply {
            this.addSource(ClientRepository(langTag).getAddresses(tokenId = tokenId)) {
                addressesResponse.value = it
                selectedAddressPosition.value =
                    it.data?.indexOfFirst { address -> address.isMainAddress == true }?.plus(1) ?: 0
            }
        }
    }

    fun getUserData(langTag: String, tokenId: String) {
        userDataResponse.value = NetworkRequestResponse.loading()
        userDataResponse.apply {
            this.addSource(ClientRepository(langTag).getUserData(tokenId = tokenId)) {
                userDataResponse.value = it
                firstName.value = it.data?.firstName
                lastName.value = it.data?.lastName
                email.value = it.data?.email
                phoneNumber.value = it.data?.phoneNumber
            }
        }
    }

    fun checkout(langTag: String, tokenId: String) = liveData(Dispatchers.IO) {
        emit(NetworkRequestResponse.loading())

        // validate inputs
        ValidationUtils().setAddressSelectedPosition(selectedAddressPosition.value).getError()
            ?.let {
                emit(NetworkRequestResponse.invalidInputData(validationError = it))
                return@liveData
            }

        emitSource(
            CartRepository(langTag).checkout(
                tokenId = tokenId,
                firstName = firstName.value,
                lastName = lastName.value,
                phoneNumber = phoneNumber.value,
                email = email.value,
                addressId = addressesResponse.value?.data?.get(
                    selectedAddressPosition.value?.minus(
                        1
                    ) ?: 0
                )?.id, // -1 for removing the place holder of spinner
            )
        )
    }

    // checkout 2
    var shippingCostModel: ShippingCostModel? = null

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

    val totalPrice get() = storesProductsCartDetails?.sumOf { it.totalCost ?: 0.0 }

    /*fun getCartItems(langTag: String, tokenId: String) {
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
        get() = Transformations.map(selectedAddressPosition) {
            //Log.e("Z_", "here 1?")
            if (it != null) {
                /*Log.e("Z_", "here 2: ${addressesResponse.value}")
                Log.e("Z_", "here 3: ${addressesResponse.value?.data}")
                Log.e("Z_", "here 3: ${addressesResponse.value?.data?.get(0)}")
                Log.e("Z_", "here 3: ${addressesResponse.value?.data?.get(it-1)}")*/
                addressesResponse.value?.data?.get(it - 1)
            } else null
        }

    val coupon = MutableLiveData<String?>()

    fun checkoutIsPaid(langTag: String, tokenId: String, paidAmount: Double) =
        liveData(Dispatchers.IO) {
            emit(NetworkRequestResponse.loading())

            emitSource(
                CartRepository(langTag).checkoutIsPaid(
                    tokenId = tokenId,
                    paidAmount = paidAmount
                )
            )
        }
}