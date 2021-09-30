package com.g7.soft.pureDot.repo

import android.util.Log
import androidx.lifecycle.liveData
import com.g7.soft.pureDot.model.ApiShopOrderModel
import com.g7.soft.pureDot.network.Fetcher
import com.g7.soft.pureDot.network.NetworkRequestHandler
import kotlinx.coroutines.Dispatchers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

open class OrderRepositoryFlavour(private val langTag: String) {

    fun checkCartItems(
        tokenId: String?,
        addressId: String? = null,
        shippingId: String? = null,
        serviceId: String? = null,
        isServiceOrder: Boolean = serviceId != null,
        serviceDate: Long? = null,
        servantsCount: Int? = null,
        couponCode: String? = null,
        firstName: String? = null,
        lastName: String? = null,
        email: String? = null,
        phone: String? = null,
        notes: String? = null,
        apiShopOrders: List<ApiShopOrderModel>?,
        paymentMethod: String? = null
    ) = liveData(Dispatchers.IO) {

        val jsonObject = JSONObject()
        jsonObject.put("tokenId", tokenId)
        jsonObject.put("addressId", addressId)
        jsonObject.put("shippingId", shippingId)
        jsonObject.put("isServiceOrder", isServiceOrder)
        jsonObject.put("rentDate", serviceDate)
        jsonObject.put("serviceId", serviceId)
        jsonObject.put("servants", servantsCount ?: 0)
        jsonObject.put("couponCode", couponCode)
        jsonObject.put("paymentType", paymentMethod)
        /*jsonObject.put("firstName", firstName)
        jsonObject.put("lastName", lastName)
        jsonObject.put("email", email)
        jsonObject.put("phone", phone)
        jsonObject.put("notes", notes)*/

        jsonObject.put("shopOrders", JSONArray().apply {
            for (apiShopOrder in apiShopOrders ?: listOf())
                this.put(JSONObject().apply {
                    this.put("storeId", apiShopOrder.shopId)
                    this.put("branchId", apiShopOrder.branchId)
                    this.put("items", JSONArray().apply {
                        for (item in apiShopOrder.items ?: listOf())
                            this.put(JSONObject().apply {
                                this.put("productId", item.productId)
                                this.put("categoryId", item.categoryId)
                                this.put("sourceId", item.sourceId)
                                this.put("quantity", item.quantity)
                                this.put("stockId", item.stockId)
                                this.put("variationsIds", JSONArray().apply {
                                    for (variationsId in item.variationsIds ?: listOf())
                                        this.put(variationsId)
                                })
                            })
                    })
                })
        })
        val body = jsonObject.toString()
            .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        Log.e("ZZ_", "body: ${jsonObject.toString()}")

        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.checkCartItems(body)
        }))
    }

    fun checkout(
        tokenId: String?,
        addressId: String? = null,
        shippingId: String? = null,
        serviceId: String? = null,
        isServiceOrder: Boolean = serviceId != null,
        servantsCount: Int? = null,
        serviceDate: Long? = null,
        couponCode: String? = null,
        firstName: String? = null,
        lastName: String? = null,
        email: String? = null,
        phoneNumber: String? = null,
        notes: String? = null,
        apiShopOrders: List<ApiShopOrderModel>?,
        paymentMethod: String?
    ) = liveData(Dispatchers.IO) {

        val jsonObject = JSONObject()
        jsonObject.put("tokenId", tokenId)
        jsonObject.put("addressId", addressId)
        jsonObject.put("shippingId", shippingId)
        jsonObject.put("isServiceOrder", isServiceOrder)
        jsonObject.put("rentDate", serviceDate)
        jsonObject.put("serviceId", serviceId)
        jsonObject.put("servants", servantsCount ?: 0)
        jsonObject.put("couponCode", couponCode)
        jsonObject.put("firstName", firstName)
        jsonObject.put("lastName", lastName)
        jsonObject.put("email", email)
        jsonObject.put("phone", phoneNumber)
        jsonObject.put("notes", notes)
        jsonObject.put("paymentType", paymentMethod)

        jsonObject.put("shopOrders", JSONArray().apply {
            for (apiShopOrder in apiShopOrders ?: listOf())
                this.put(JSONObject().apply {
                    this.put("storeId", apiShopOrder.shopId)
                    this.put("branchId", apiShopOrder.branchId)
                    this.put("items", JSONArray().apply {
                        for (item in apiShopOrder.items ?: listOf())
                            this.put(JSONObject().apply {
                                this.put("productId", item.productId)
                                this.put("categoryId", item.categoryId)
                                this.put("sourceId", item.sourceId)
                                this.put("quantity", item.quantity)
                                this.put("stockId", item.stockId)
                                this.put("variationsIds", JSONArray().apply {
                                    for (variationsId in item.variationsIds ?: listOf())
                                        this.put(variationsId)
                                })
                            })
                    })
                })
        })
        val body = jsonObject.toString()
            .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        Log.e("ZZ_", "body: ${jsonObject.toString()}")

        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.checkout(body)
        }))
    }

    fun getMyOrders(
        tokenId: String?,
        itemsPerPage: Int?,
        pageNumber: Int?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.getMyOrders(
                tokenId = tokenId,
                itemsPerPage = itemsPerPage,
                pageNumber = pageNumber,
            )
        }))
    }

    fun trackOrder(
        tokenId: String?,
        orderId: String?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.trackOrder(
                tokenId = tokenId,
                orderId = orderId,
            )
        }))
    }

    fun rateOrder(
        tokenId: String?,
        orderId: String?,
        orderRating: Int?,
        orderComment: String?,
        deliveryRating: Int?,
        deliveryComment: String?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.rateOrder(
                tokenId = tokenId,
                orderId = orderId,
                orderRating = orderRating,
                orderComment = orderComment,
                deliveryRating = deliveryRating,
                deliveryComment = deliveryComment,
            )
        }))
    }

    fun calculateRefundShipping(
        detailsId: String?,
        shippingMethod: String?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.calculateRefundShipping(
                detailsId = detailsId,
                shippingMethod = shippingMethod
            )
        }))
    }

    fun returnItem(
        shippingMethod: String?,
        detailsId: String?,
        itemTotalPrice: Double?,
        refundAmount: Double?,
        shippingCost: Double?,
        commission: Double?,
        vat: Double?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.returnItem(
                detailsId = detailsId,
                itemTotalPrice = itemTotalPrice,
                shippingMethod = shippingMethod,
                refundAmount = refundAmount,
                shippingCost = shippingCost,
                commission = commission,
                vat = vat
            )
        }))
    }

    fun getShippingMethods() = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.getShippingMethods()
        }))
    }

    fun getMasterOrder(tokenId: String?, id: String?) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.getMasterOrder(tokenId = tokenId, id = id)
        }))
    }

    fun cancelMasterOrder(
        tokenId: String?,
        masterOrderId: String?
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)
                ?.cancelMasterOrder(tokenId = tokenId, masterOrderId = masterOrderId)
        }))
    }
}