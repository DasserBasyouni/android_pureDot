package com.g7.soft.pureDot.repo

import android.content.Context
import androidx.lifecycle.liveData
import com.g7.soft.pureDot.database.product.ProductCart
import com.g7.soft.pureDot.database.product.ProductCartDatabase
import com.g7.soft.pureDot.model.ProductModel
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

    /*fun getCartItems(
        tokenId: String?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.getCartItems(
                tokenId = tokenId,
            )
        }))
    }*/

    fun addProductToCart(
        lifecycleScope: CoroutineScope,
        context: Context,
        product: ProductModel?,
        quantityInCart: Int?,
        variationsIds: List<String>?,
        onComplete: () -> Unit
    ) {
        lifecycleScope.launch {
            val dao = ProductCartDatabase.getDatabase(context)?.cartDao()
            val productId = product?.id
            val productPrice = product?.priceWithDiscount

            productId
                ?: LogEventUtils.logApiError("CartRepository.addProductToCart: null productId")
                    .run { return@launch }
            productPrice
                ?: LogEventUtils.logApiError("CartRepository.addProductToCart: null productPrice")
                    .run { return@launch }
            quantityInCart
                ?: LogEventUtils.logApiError("CartRepository.addProductToCart: null quantityInCart")
                    .run { return@launch }
            dao ?: LogEventUtils.logApiError("CartRepository.addProductToCart: null dao")
                .run { return@launch }

            val existingProduct = dao.getItemById(productId)

            dao.insertIntoProduct(
                ProductCart(
                    apiId = existingProduct?.apiId,
                    productId = productId,
                    price = productPrice,
                    quantityInCart = (existingProduct?.quantityInCart ?: 0) + quantityInCart,
                    variationsIds = variationsIds
                )
            )

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
                productsInCart?.map { it?.quantityInCart?.times(it.price) ?: 0.0 }?.sum() ?: 0.0

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

    /*fun getCartItems(
        tokenId: String?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.getCartItems(
                tokenId = tokenId,
            )
        }))
    }*/

    fun checkCartProducts(
        tokenId: String?,
        ids: List<String>?,
        quantities: List<Int>?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.checkCartProducts(
                tokenId = tokenId,
                ids = ids,
                quantities = quantities
            )
        }))
    }

    fun checkout(
        tokenId: String?,
        firstName: String?,
        lastName: String?,
        email: String?,
        phoneNumber: String?,
        addressId: String?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.checkout(
                tokenId = tokenId,
                firstName = firstName,
                lastName = lastName,
                email = email,
                phoneNumber = phoneNumber,
                addressId = addressId,
            )
        }))
    }

    fun checkCoupon(
        tokenId: String?,
        coupon: String?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.checkCoupon(
                tokenId = tokenId,
                coupon = coupon,
            )
        }))
    }

    fun checkoutIsPaid(
        tokenId: String?,
        paidAmount: Double?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.checkoutIsPaid(
                tokenId = tokenId,
                paidAmount = paidAmount,
            )
        }))
    }

    fun getWishList(
        tokenId: String?,
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.getWishList(
                tokenId = tokenId,
            )
        }))
    }
}