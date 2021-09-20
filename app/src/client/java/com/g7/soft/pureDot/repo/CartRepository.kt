package com.g7.soft.pureDot.repo

import android.content.Context
import androidx.lifecycle.liveData
import com.g7.soft.pureDot.data.database.product.ProductCart
import com.g7.soft.pureDot.data.database.product.ProductCartDatabase
import com.g7.soft.pureDot.model.ApiShopOrderItemModel
import com.g7.soft.pureDot.model.ApiShopOrderModel
import com.g7.soft.pureDot.network.Fetcher
import com.g7.soft.pureDot.network.NetworkRequestHandler
import com.g7.soft.pureDot.util.LogEventUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CartRepository(private val langTag: String) {

    /*fun editCartQuantity(
        itemId: Int?,
        quantity: Int?,
        serviceDateTime: Long?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.editCartQuantity(
                itemId = itemId,
                quantity = quantity,
                serviceDateTime = serviceDateTime,
            )
        }))
    }*/

    fun addProductToCart(
        lifecycleScope: CoroutineScope,
        context: Context,
        productId: String?,
        storeId: String?,
        selectedBranchId: String?,
        categoryId: String?,
        stockId: String? = null,
        sourceId: String? = null,
        quantityInCart: Int?,
        variationsIds: List<String>?,
        price: Double?,
        onComplete: () -> Unit,
    ) {
        lifecycleScope.launch {
            val dao = ProductCartDatabase.getDatabase(context)?.cartDao()

            productId
                ?: LogEventUtils.logApiError("CartRepository.addProductToCart: null productId")
                    .run { return@launch }
            price
                ?: LogEventUtils.logApiError("CartRepository.addProductToCart: null price")
                    .run { return@launch }
            quantityInCart
                ?: LogEventUtils.logApiError("CartRepository.addProductToCart: null quantityInCart")
                    .run { return@launch }
            storeId
                ?: LogEventUtils.logApiError("CartRepository.addProductToCart: null storeId")
                    .run { return@launch }
            dao ?: LogEventUtils.logApiError("CartRepository.addProductToCart: null dao")
                .run { return@launch }


            val dataInCart = dao.getAll()
            val existingStoreData = dataInCart?.firstOrNull {
                it?.apiShopOrder?.shopId == storeId && it.apiShopOrder.branchId == selectedBranchId
            }

            if (existingStoreData == null) {
                dao.insertIntoProduct(
                    ProductCart(
                        apiShopOrder = ApiShopOrderModel(
                            shopId = storeId,
                            branchId = selectedBranchId,
                            items = mutableListOf(
                                ApiShopOrderItemModel(
                                    productId = productId,
                                    categoryId = categoryId,
                                    stockId = stockId,
                                    sourceId = sourceId,
                                    quantity = quantityInCart,
                                    variationsIds = variationsIds,
                                    price = price,
                                )
                            )
                        )
                    )
                )
            } else {
                val existingProductData = existingStoreData.apiShopOrder.items?.firstOrNull {
                    it.productId == productId && it.categoryId == categoryId
                }

                if (existingProductData == null) {
                    dao.insertIntoProduct(
                        ProductCart(
                            id = existingStoreData.id,
                            apiShopOrder = ApiShopOrderModel(
                                shopId = existingStoreData.apiShopOrder.shopId,
                                branchId = existingStoreData.apiShopOrder.branchId,
                                items = existingStoreData.apiShopOrder.items.apply {
                                    this?.add(
                                        ApiShopOrderItemModel(
                                            productId = productId,
                                            categoryId = categoryId,
                                            stockId = stockId,
                                            sourceId = sourceId,
                                            quantity = quantityInCart,
                                            variationsIds = variationsIds,
                                            price = price,
                                        )
                                    )
                                }
                            )
                        )
                    )
                } else {
                    dao.insertIntoProduct(
                        ProductCart(
                            id = existingStoreData.id,
                            apiShopOrder = ApiShopOrderModel(
                                shopId = existingStoreData.apiShopOrder.shopId,
                                branchId = existingStoreData.apiShopOrder.branchId,
                                items = existingStoreData.apiShopOrder.items.apply {
                                    this?.firstOrNull {
                                        it.productId == productId && it.categoryId == categoryId
                                    }?.quantity =
                                        this?.firstOrNull {
                                            it.productId == productId && it.categoryId == categoryId
                                        }?.quantity?.plus(quantityInCart)
                                }
                            )
                        )
                    )
                }
            }

            onComplete.invoke()
        }
    }

    fun updateProductQuantity(
        lifecycleScope: CoroutineScope,
        context: Context,
        productId: String?,
        quantity: Int?,
        onComplete: () -> Unit,
    ) {
        lifecycleScope.launch {
            val dao = ProductCartDatabase.getDatabase(context)?.cartDao()

            productId
                ?: LogEventUtils.logApiError("CartRepository.addProductToCart: null productId")
                    .run { return@launch }
            quantity
                ?: LogEventUtils.logApiError("CartRepository.addProductToCart: null quantity")
                    .run { return@launch }
            dao ?: LogEventUtils.logApiError("CartRepository.addProductToCart: null dao")
                .run { return@launch }

            val dataInCart = dao.getAll()
            val existingStoreData = dataInCart?.firstOrNull {
                it?.apiShopOrder?.items?.firstOrNull { item -> item.productId == productId } != null
            }

            if (quantity != 0)
                dao.insertIntoProduct(existingStoreData.apply {
                    this?.apiShopOrder?.items?.firstOrNull {
                        it.productId == productId
                    }?.quantity = quantity
                })
            else {
                if (existingStoreData?.apiShopOrder?.items?.size ?: 1 > 1)
                    dao.insertIntoProduct(existingStoreData.apply {
                        this?.apiShopOrder?.items?.remove(this.apiShopOrder.items?.firstOrNull {
                            it.productId == productId
                        })
                    })
                else if (existingStoreData?.id != null)
                    dao.deleteItem(existingStoreData.id!!)
            }
            onComplete.invoke()
        }
    }

    fun getTotalProductsPriceInCart(
        lifecycleScope: CoroutineScope,
        context: Context,
        onComplete: (totalPrice: Double) -> Unit
    ) {
        lifecycleScope.launch {
            val dao = ProductCartDatabase.getDatabase(context)?.cartDao()

            dao ?: LogEventUtils.logApiError("CartRepository.addProductToCart: null dao")
                .run { return@launch }

            val productsInCart = dao.getAll()
            val totalPriceInCart =
                productsInCart?.sumOf {
                    it?.apiShopOrder?.items?.sumOf { item ->
                        (item.itemSubTotal ?: 0.0)
                    } ?: 0.0
                } ?: 0.0

            onComplete.invoke(totalPriceInCart)
        }
    }

    fun getProductsInCart(
        lifecycleScope: CoroutineScope,
        context: Context,
        onComplete: (productsInCart: List<ProductCart?>?) -> Unit
    ) {
        lifecycleScope.launch {
            val dao = ProductCartDatabase.getDatabase(context)?.cartDao()

            dao ?: LogEventUtils.logApiError("CartRepository.getProductsInCart: null dao")
                .run { return@launch }

            val productsInCart = dao.getAll()

            onComplete.invoke(productsInCart)
        }
    }

    fun clearCart(
        lifecycleScope: CoroutineScope,
        context: Context
    ) {
        lifecycleScope.launch {
            val dao = ProductCartDatabase.getDatabase(context)?.cartDao()

            dao ?: LogEventUtils.logApiError("CartRepository.clearCart: null dao")
                .run { return@launch }

            dao.deleteALl()
        }
    }

/*fun addServiceToCart(
    lifecycleScope: CoroutineScope,
    context: Context,
    serviceId: Int?,
    quantityInCart: Int?,
    onComplete: () -> Unit
) {
    lifecycleScope.launch {
        val dao = ServiceCartDatabase.getDatabase(context)?.cartDao()

        serviceId
            ?: LogEventUtils.logApiError("CartRepository.addServiceToCart: null serviceId")
                .run { return@launch }
        quantityInCart
            ?: LogEventUtils.logApiError("CartRepository.addServiceToCart: null quantityInCart")
                .run { return@launch }
        dao ?: LogEventUtils.logApiError("CartRepository.addServiceToCart: null dao")
            .run { return@launch }

        val existingService = dao.getItemById(serviceId)

        dao.insertIntoProduct(
            ServiceCart(
                id = existingService?.id,
                productId = serviceId,
                quantityInCart = (existingService?.quantityInCart ?: 0) + quantityInCart
            )
        )

        onComplete.invoke()
    }
}*/

    fun checkoutIsPaid(
        tokenId: String?,
        orderId: String?,
        isPaid: Boolean?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.checkoutIsPaid(
                tokenId = tokenId,
                orderId = orderId,
                isPaid = isPaid,
            )
        }))
    }
}