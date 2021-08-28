package com.g7.soft.pureDot.network

import com.g7.soft.pureDot.constant.ApiConstant
import java.util.*

class OfflineDemoRepository {

    companion object {

        val getSuccessfulStatus = hashMapOf<String, Any?>(
            "status" to ApiConstant.Status.SUCCESS.value,
        )

        val getSuccessfulStatusWithId = hashMapOf<String, Any?>(
            "status" to ApiConstant.Status.SUCCESS.value,
            "data" to {
                "id" to 1
            }
        )

        val getSuccessfulStatusWithNumber = hashMapOf<String, Any?>(
            "status" to ApiConstant.Status.SUCCESS.value,
            "data" to {
                "number" to 1
            }
        )

        private val city1 = hashMapOf(
            "id" to 1,
            "name" to "Alex",
        )
        private val city2 = hashMapOf(
            "id" to 2,
            "name" to "Cairo",
        )

        private val country1 = hashMapOf(
            "id" to 1,
            "name" to "Egypt",
        )
        private val country2 = hashMapOf(
            "id" to 2,
            "name" to "Saudi Arabia",
        )

        // models
        private val address1 = hashMapOf(
            "id" to 1,
            "flat" to "flat x",
            "floor" to "floor y",
            "homeNumber" to "1234567",
            "streetName" to "Dolphin Grove",
            "area" to "area 51",
            "isMainAddress" to false
        )
        private val address2 = hashMapOf(
            "id" to 2,
            "flat" to "flat a",
            "floor" to "floor b",
            "homeNumber" to "7654321",
            "streetName" to "Silverdale Glas",
            "area" to "area 11",
            "isMainAddress" to true
        )
        private val store1 = hashMapOf(
            "id" to 1,
            "name" to "Nuts",
            "logoImageUrl" to "photo-1496200186974-4293800e2c20?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=2089&q=80",
        )
        private val store2 = hashMapOf(
            "id" to 2,
            "name" to "Beer Nuts",
            "logoImageUrl" to "photo-1553835973-dec43bfddbeb?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80",
        )
        private val review1 = hashMapOf(
            "id" to 2,
            "reviewerName" to "John Foster",
            "rating" to 5.0,
            "review" to "A review is an evaluation of a publication, service, or company such as a movie, video game, musical composition, book; a piece of hardware like a car, home appliance, or computer; or softwares",
            //"isMarked" to true,
            "reviewerImageUrl" to "photo-1609010697446-11f2155278f0?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1050&q=80",
            "date" to Calendar.getInstance().apply {
                this.set(Calendar.HOUR_OF_DAY, 0)
                this.set(Calendar.MINUTE, 0)
                this.set(Calendar.SECOND, 0)
                this.set(Calendar.MILLISECOND, 0)
                this.set(Calendar.ZONE_OFFSET, 0)
            }.timeInMillis / 1000
        )
        private val review2 = hashMapOf(
            "id" to 2,
            "reviewerName" to "Jack Max",
            "rating" to 4.5,
            "review" to "A review is an evaluation of a publication, service, or company such as a movie, video game, musical composition, book; a piece of hardware like a car, home appliance, or computer; or softwares",
            //"isMarked" to false,
            "reviewerImageUrl" to "photo-1492562080023-ab3db95bfbce?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1031&q=80",
            "date" to Calendar.getInstance().apply {
                this.set(Calendar.DAY_OF_MONTH, this.get(Calendar.DAY_OF_MONTH) - 1)
                this.set(Calendar.HOUR_OF_DAY, 0)
                this.set(Calendar.MINUTE, 0)
                this.set(Calendar.SECOND, 0)
                this.set(Calendar.MILLISECOND, 0)
                this.set(Calendar.ZONE_OFFSET, 0)
            }.timeInMillis / 1000
        )
        private val notAvailableProduct = hashMapOf(
            "id" to 3,
            "name" to "product 3",
            "price" to 19.50,
            "quantity" to 100,
            "measureUnit" to "gm",
            //"logoImageUrl" to "photo-1496200186974-4293800e2c20?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=2089&q=80",
            "imageUrl" to "photo-1576618148400-f54bed99fcfd?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=800&q=80",
            "vat" to 2.00,
            "shop" to store1,
            "isAvailable" to false,
            "currency" to "EGP",
            "discountPercentage" to .23,
            "quantityInCart" to 2,
            "reviews" to hashMapOf(
                "totalCount" to 10,
                "data" to listOf(review1, review2)
            ),
        )
        private val product1 = hashMapOf(
            "id" to 1,
            "name" to "product 1",
            "price" to 19.50,
            "quantity" to 100,
            "measureUnit" to "gm",
            //"logoImageUrl" to "photo-1496200186974-4293800e2c20?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=2089&q=80",
            "imageUrl" to "photo-1523275335684-37898b6baf30?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1289&q=80",
            "vat" to 2.00,
            "shop" to store1,
            "isAvailable" to true,
            "currency" to "EGP",
            "discountPercentage" to .23,
            "quantityInCart" to null,
        )

        // TODO delete logoImageUrl from api?
        private val product2 = hashMapOf(
            "id" to 2,
            "name" to "product 2",
            "price" to 10.50,
            "quantity" to 100,
            "measureUnit" to "gm",
            //"logoImageUrl" to "photo-1496200186974-4293800e2c20?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=2089&q=80",
            "imageUrl" to "photo-1505740420928-5e560c06d30e?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80",
            "vat" to 1.00,
            "shop" to store2,
            "isAvailable" to true,
            "currency" to "EGP",
            "discountPercentage" to null,
            "quantityInCart" to 2,
            "userReview" to review2
        )
        private val product1InCart = hashMapOf(
            "id" to 1,
            "name" to "product 1",
            "price" to 19.99,
            "quantity" to 150,
            "measureUnit" to "gm",
            //"logoImageUrl" to "photo-1496200186974-4293800e2c20?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=2089&q=80",
            "imageUrl" to "photo-1523275335684-37898b6baf30?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1289&q=80",
            "vat" to 2.00,
            "shop" to store1,
            "isAvailable" to true,
            "currency" to "EGP",
            "discountPercentage" to .23,
            "quantityInCart" to 1,
            "userReview" to null
        )
        private val sliderOffer1 = hashMapOf(
            "id" to 1,
            "imageUrl" to "photo-1607082350899-7e105aa886ae?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80",
        )
        private val sliderOffer2 = hashMapOf(
            "id" to 2,
            "imageUrl" to "photo-1621018987439-ebca083ef360?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80",
        )
        private val category1 = hashMapOf(
            "id" to 1,
            "name" to "category 1",
            "imageUrl" to "photo-1543158181-1274e5362710?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1050&q=80",
        )
        private val category2 = hashMapOf(
            "id" to 2,
            "name" to "category 2",
            "imageUrl" to "photo-1543208541-0961a29a8c3d?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1050&q=80",
        )
        private val cartItemsModel1 = hashMapOf(
            "products" to listOf(product1InCart, product2, product1InCart , product2),
            "serviceDateTime" to Calendar.getInstance().timeInMillis / 1000
        )
        private val cartItemsModel2 = hashMapOf(
            "products" to listOf(product2, product1, product2, product1),
            "serviceDateTime" to null
        )
        private val size1 = hashMapOf(
            "id" to 1,
            "text" to "Small",
        )
        private val size2 = hashMapOf(
            "id" to 2,
            "text" to "Large",
        )
        private val flavour1 = hashMapOf(
            "id" to 1,
            "name" to "Choco Flavour",
        )
        private val flavour2 = hashMapOf(
            "id" to 2,
            "name" to "Mango Flavour",
        )
        private val color1 = hashMapOf(
            "id" to 1,
            "hexColor" to "#ff0000",
        )
        private val color2 = hashMapOf(
            "id" to 2,
            "hexColor" to "#fffb00",
        )

        private val order1 = hashMapOf(
            "number" to 512,
            "dateTime" to Calendar.getInstance().apply {
                this.set(Calendar.HOUR_OF_DAY, 0)
                this.set(Calendar.MINUTE, 0)
                this.set(Calendar.SECOND, 0)
                this.set(Calendar.MILLISECOND, 0)
                this.set(Calendar.ZONE_OFFSET, 0)
            }.timeInMillis / 1000,
            "fromAddress" to "2583  Coffman Alley",
            "toAddress" to "41 Euclid St. Easton, CT 06514",
            "price" to 41.50,
            "note" to "the red door is my flat",
            "products" to listOf(product1InCart, product2),
            "shippingCost" to 10.50,
            "commission" to 10.00,
            "clientLat" to 31.180703,
            "clientLng" to 29.928346,
            "clientImageUrl" to "photo-1534308143481-c55f00be8bd7?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1488&q=80",
            "status" to ApiConstant.OrderStatus.OUT_FOR_DELIVERY.value,
            "shopName" to "shop x",
            "shopLat" to 31.208480,
            "shopLng" to 29.933935,
            "finalRouteImageUrl" to "photo-1604357209793-fca5dca89f97?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=700&q=80",
            "clientPhoneNumber" to "+965443436778"
        )
        private val order2 = hashMapOf(
            "number" to 664,
            "dateTime" to Calendar.getInstance().apply {
                this.set(Calendar.HOUR_OF_DAY, 0)
                this.set(Calendar.MINUTE, 0)
                this.set(Calendar.SECOND, 0)
                this.set(Calendar.MILLISECOND, 0)
                this.set(Calendar.ZONE_OFFSET, 0)
            }.timeInMillis / 1000,
            "fromAddress" to "2583  Coffman Alley",
            "toAddress" to "41 Euclid St. Easton, CT 06514",
            "price" to 11.50,
            "note" to "the red door is my flat",
            "products" to listOf(product1InCart, product2),
            "shippingCost" to 5.50,
            "commission" to 4.00,
            "clientLat" to 31.180703,
            "clientLng" to 29.928346,
            "clientImageUrl" to "photo-1534308143481-c55f00be8bd7?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1488&q=80",
            "status" to ApiConstant.OrderStatus.DELIVERED.value,
            "shopName" to "shop x",
            "shopLat" to 31.208480,
            "shopLng" to 29.933935,
            "finalRouteImageUrl" to "photo-1604357209793-fca5dca89f97?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=700&q=80",
            "clientPhoneNumber" to "+965443436778"
        )
        private val service1 = hashMapOf(
            "id" to 1,
            "name" to "service 1",
            "price" to 41.50,
            "items" to cartItemsModel1,
            "shopLogoUrl" to "photo-1496200186974-4293800e2c20?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=2089&q=80",
            "imageUrl" to "photo-1523275335684-37898b6baf30?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1289&q=80",
            "description" to "Lorem Ipsum is simply dummy text of the printing and typesetting industry."
        )
        private val service2 = hashMapOf(
            "id" to 2,
            "name" to "service 2",
            "price" to 11.50,
            "items" to cartItemsModel2,
            "shopLogoUrl" to "photo-1496200186974-4293800e2c20?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=2089&q=80",
            "imageUrl" to "photo-1505740420928-5e560c06d30e?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80",
            "description" to "Lorem Ipsum is simply dummy text of the printing and typesetting industry."
        )
        private val transaction1 = hashMapOf(
            "id" to 1,
            "dateTime" to Calendar.getInstance().apply {
                this.set(Calendar.HOUR_OF_DAY, 0)
                this.set(Calendar.MINUTE, 0)
                this.set(Calendar.SECOND, 0)
                this.set(Calendar.MILLISECOND, 0)
                this.set(Calendar.ZONE_OFFSET, 0)
            }.timeInMillis / 1000,
            "type" to ApiConstant.TransactionType.DEPOSIT.value,
            "amount" to 5.50,
            "currency" to "EGP"
        )
        private val transaction2 = hashMapOf(
            "id" to 2,
            "dateTime" to Calendar.getInstance().apply {
                this.set(Calendar.HOUR_OF_DAY, 0)
                this.set(Calendar.MINUTE, 0)
                this.set(Calendar.SECOND, 0)
                this.set(Calendar.MILLISECOND, 0)
                this.set(Calendar.ZONE_OFFSET, 0)
            }.timeInMillis / 1000,
            "type" to ApiConstant.TransactionType.FROM_YOUR_ACCOUNT.value,
            "amount" to 15.00,
            "currency" to "EGP"
        )
        private val transaction3 = hashMapOf(
            "id" to 1,
            "dateTime" to Calendar.getInstance().apply {
                this.set(Calendar.HOUR_OF_DAY, 0)
                this.set(Calendar.MINUTE, 0)
                this.set(Calendar.SECOND, 0)
                this.set(Calendar.MILLISECOND, 0)
                this.set(Calendar.ZONE_OFFSET, 0)
            }.timeInMillis / 1000,
            "type" to ApiConstant.TransactionType.TO_YOUR_ACCOUNT.value,
            "amount" to 5.50,
            "currency" to "EGP"
        )
        private val transaction4 = hashMapOf(
            "id" to 2,
            "dateTime" to Calendar.getInstance().apply {
                this.set(Calendar.HOUR_OF_DAY, 0)
                this.set(Calendar.MINUTE, 0)
                this.set(Calendar.SECOND, 0)
                this.set(Calendar.MILLISECOND, 0)
                this.set(Calendar.ZONE_OFFSET, 0)
            }.timeInMillis / 1000,
            "type" to ApiConstant.TransactionType.WITHDRAW.value,
            "amount" to 15.00,
            "currency" to "EGP"
        )
        private val transaction5 = hashMapOf(
            "id" to 1,
            "dateTime" to Calendar.getInstance().apply {
                this.set(Calendar.HOUR_OF_DAY, 0)
                this.set(Calendar.MINUTE, 0)
                this.set(Calendar.SECOND, 0)
                this.set(Calendar.MILLISECOND, 0)
                this.set(Calendar.ZONE_OFFSET, 0)
            }.timeInMillis / 1000,
            "type" to ApiConstant.TransactionType.POINTS.value,
            "amount" to 8.50,
            "currency" to "EGP"
        )
        private val contact1 = hashMapOf(
            "id" to 1,
            "firstName" to "Dasser",
            "lastName" to "Basyouni",
            "imageUrl" to "photo-1611743880692-b06aadf6594e?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80",
            "phoneNumber" to "01208968336",
        )

        // user
        val userSignUp = hashMapOf<String, Any?>(
            "status" to ApiConstant.Status.SUCCESS.value,
            "data" to hashMapOf(
                "tokenId" to "OFFLINE_DEMO_USER_TOKENIZED_ID"
            )
        )

        val userLogin = hashMapOf<String, Any?>(
            "status" to ApiConstant.Status.SUCCESS.value,
            "data" to hashMapOf(
                "tokenId" to "OFFLINE_DEMO_USER_TOKENIZED_ID",
                "firstName" to "Dasser",
                "lastName" to "Basyouni",
                "email" to "dasserbasyouni@gmail.com",
                "mobileNumber" to "123456789",
                "isMale" to true,
                "imageUrl" to "photo-1611743880692-b06aadf6594e?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80",
                "points" to 50,
                "credit" to 30.50,
                "country" to "Egypt",
                "countryCode" to "+966",
                "currency" to "EGP",
                "city" to city1,
                "country" to country1,
                "dateOfBirth" to Calendar.getInstance().apply {
                    this.set(Calendar.HOUR_OF_DAY, 0)
                    this.set(Calendar.MINUTE, 0)
                    this.set(Calendar.SECOND, 0)
                    this.set(Calendar.MILLISECOND, 0)
                    this.set(Calendar.ZONE_OFFSET, 0)
                    this.set(Calendar.YEAR, 1990)
                }.timeInMillis / 1000
            )
        )

        val getAddresses = hashMapOf<String, Any?>(
            "status" to ApiConstant.Status.SUCCESS.value,
            "data" to listOf(address1, address2)
        )

        // general
        val getAllCounties: HashMap<String, Any?>
            get() = hashMapOf(
                "status" to ApiConstant.Status.SUCCESS.value,
                "data" to listOf(
                    hashMapOf(
                        "id" to 1,
                        "name" to "Egypt",
                    ),
                    hashMapOf(
                        "id" to 2,
                        "name" to "Saudi Arabia",
                    ),
                )
            )

        val getAllCities: HashMap<String, Any?>
            get() = hashMapOf(
                "status" to ApiConstant.Status.SUCCESS.value,
                "data" to listOf(
                    hashMapOf(
                        "id" to 1,
                        "name" to "Alexandria",
                    ),
                    hashMapOf(
                        "id" to 2,
                        "name" to "Cairo",
                    ),
                )
            )

        val getAllZipCodes: HashMap<String, Any?>
            get() = hashMapOf(
                "status" to ApiConstant.Status.SUCCESS.value,
                "data" to listOf(
                    hashMapOf(
                        "id" to 1,
                        "code" to "21111",
                    ),
                    hashMapOf(
                        "id" to 2,
                        "code" to "54321",
                    ),
                )
            )

        // product
        val get4Products: HashMap<String, Any?>
            get() = hashMapOf(
                "status" to ApiConstant.Status.SUCCESS.value,
                "data" to hashMapOf(
                    "totalCount" to 512,
                    "data" to listOf(
                        product1,
                        product2,
                        product1,
                        product2,
                    )
                )
            )
        val get9Products: HashMap<String, Any?>
            get() = hashMapOf(
                "status" to ApiConstant.Status.SUCCESS.value,
                "data" to hashMapOf(
                    "totalCount" to 512,
                    "data" to listOf(
                        product1,
                        product2,
                        product1,
                        product2,
                        product1,
                        product2,
                        product1,
                        product2,
                        product1,
                    )
                )
            )
        val get10Products: HashMap<String, Any?>
            get() = hashMapOf(
                "status" to ApiConstant.Status.SUCCESS.value,
                "data" to hashMapOf(
                    "totalCount" to 512,
                    "data" to listOf(
                        notAvailableProduct,
                        product2,
                        product1,
                        product2,
                        product1,
                        product2,
                        product1,
                        product2,
                        product1,
                        product2
                    )
                )
            )

        // offers
        val getSliderOffers: HashMap<String, Any?>
            get() = hashMapOf(
                "status" to ApiConstant.Status.SUCCESS.value,
                "data" to listOf(sliderOffer1, sliderOffer2, sliderOffer1)
            )


        // store
        val getStores: HashMap<String, Any?>
            get() = hashMapOf(
                "status" to ApiConstant.Status.SUCCESS.value,
                "data" to hashMapOf(
                    "totalCount" to 324,
                    "data" to listOf(
                        store1,
                        store2,
                        store1,
                        store2,
                        store1,
                        store2,
                        store1,
                        store2,
                        store1,
                        store2
                    )
                )
            )

        // categories
        val getCategories: HashMap<String, Any?>
            get() = hashMapOf(
                "status" to ApiConstant.Status.SUCCESS.value,
                "data" to hashMapOf(
                    "totalCount" to 123,
                    "data" to listOf(
                        category1, category2, category1, category2, category1,
                        category2, category1, category2, category1, category2,
                    )
                )
            )

        // cart
        val editCartQuantity: HashMap<String, Any?>
            get() = hashMapOf(
                "status" to ApiConstant.Status.SUCCESS.value,
                "data" to hashMapOf(
                    "totalPriceInCart" to 230
                )
            )
        val getCartItems: HashMap<String, Any?>
            get() = hashMapOf(
                "status" to ApiConstant.Status.SUCCESS.value,
                "data" to cartItemsModel1
            )
        val getProductDetails: HashMap<String, Any?>
            get() = hashMapOf(
                "status" to ApiConstant.Status.SUCCESS.value,
                "data" to hashMapOf(
                    "sizes" to listOf(size1, size2),
                    "flavours" to listOf(flavour1, flavour2),
                    "colors" to listOf(color1, color2),
                    "description" to "Description is the pattern of narrative development that aims to make vivid a place, object, character, or group. Description is one of four rhetorical modes, along with exposition, argumentation, and narration.",
                    "similarItems" to listOf(product1, product2),
                    "quantityInCart" to 1,
                    "images" to listOf(
                        "photo-1523275335684-37898b6baf30?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1289&q=80",
                        "photo-1505740420928-5e560c06d30e?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80"
                    ),
                    "reviews" to hashMapOf(
                        "totalCount" to 10,
                        "data" to listOf(review1, review2)
                    ),
                )
            )
        val getItemReviews: HashMap<String, Any?>
            get() = hashMapOf(
                "status" to ApiConstant.Status.SUCCESS.value,
                "data" to hashMapOf(
                    "totalCount" to 100,
                    "data" to listOf(
                        review1, review2, review1, review2, review1,
                        review2, review1, review2, review1, review2,
                    )
                )
            )
        val checkout: HashMap<String, Any?>
            get() = hashMapOf(
                "status" to ApiConstant.Status.SUCCESS.value,
                "data" to hashMapOf(
                    "shippingCost" to 15.00
                )
            )
        val checkoutIsPaid: HashMap<String, Any?>
            get() = hashMapOf(
                "status" to ApiConstant.Status.SUCCESS.value,
                "data" to order1
            )
        val getWishList: HashMap<String, Any?>
            get() = hashMapOf(
                "status" to ApiConstant.Status.SUCCESS.value,
                "data" to hashMapOf(
                    "totalCount" to 512,
                    "data" to listOf(
                        product1,
                        notAvailableProduct,
                        product1,
                        product2,
                        product1,
                        notAvailableProduct,
                        product1,
                        product2,
                        notAvailableProduct,
                        product2
                    )
                )
            )
        val getMyOrders: HashMap<String, Any?>
            get() = hashMapOf(
                "status" to ApiConstant.Status.SUCCESS.value,
                "data" to listOf(
                        order1, order2, order1, order2, order1,
                        order2, order1, order2, order1, order2
                )
            )
        val trackOrder: HashMap<String, Any?>
            get() = hashMapOf(
                "status" to ApiConstant.Status.SUCCESS.value,
                "data" to hashMapOf(
                    "status" to ApiConstant.OrderStatus.DELIVERED.value, // todo
                    "deliveredEstimatedDateTime" to Calendar.getInstance().apply {
                        this.set(Calendar.HOUR_OF_DAY, 0)
                        this.set(Calendar.MINUTE, 0)
                        this.set(Calendar.SECOND, 0)
                        this.set(Calendar.MILLISECOND, 0)
                        this.set(Calendar.ZONE_OFFSET, 0)
                    }.timeInMillis / 1000,
                    "outForDeliveryEstimatedDateTime" to Calendar.getInstance().apply {
                        this.set(Calendar.HOUR_OF_DAY, 0)
                        this.set(Calendar.MINUTE, 0)
                        this.set(Calendar.SECOND, 0)
                        this.set(Calendar.MILLISECOND, 0)
                        this.set(Calendar.ZONE_OFFSET, 0)
                    }.timeInMillis / 1000,
                    "shippingEstimatedDateTime" to Calendar.getInstance().apply {
                        this.set(Calendar.HOUR_OF_DAY, 0)
                        this.set(Calendar.MINUTE, 0)
                        this.set(Calendar.SECOND, 0)
                        this.set(Calendar.MILLISECOND, 0)
                        this.set(Calendar.ZONE_OFFSET, 0)
                    }.timeInMillis / 1000,
                )
            )

        // service
        val getServices: HashMap<String, Any?>
            get() = hashMapOf(
                "status" to ApiConstant.Status.SUCCESS.value,
                "data" to listOf(
                    service1, service2, service1, service2, service1,
                    service2, service1, service2, service1, service2,
                )

            )
        val getServiceDetails: HashMap<String, Any?>
            get() = hashMapOf(
                "status" to ApiConstant.Status.SUCCESS.value,
                "data" to hashMapOf(
                    "maxServants" to 4,
                    "includingDescription" to "Item 1 (x3) + Item 2 (x3) + Item 3 (x3)",
                    "description" to "Description is the pattern of narrative development that aims to make vivid a place, object, character, or group. Description is one of four rhetorical modes, along with exposition, argumentation, and narration.",
                    "similarItems" to listOf(service1, service2),
                    "quantityInCart" to 1,
                    "images" to listOf(
                        "photo-1523275335684-37898b6baf30?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1289&q=80",
                        "photo-1505740420928-5e560c06d30e?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80"
                    ),
                    "reviews" to hashMapOf(
                        "totalCount" to 10,
                        "data" to listOf(review1, review2)
                    ),
                )
            )

        // wallet
        val getWalletData: HashMap<String, Any?>
            get() = hashMapOf(
                "status" to ApiConstant.Status.SUCCESS.value,
                "data" to hashMapOf(
                    "balance" to 50.50,
                    "totalEarning" to 300.00,
                    "totalDeliveries" to 10,
                    "currentWallet" to 50.50,
                    "totalTransfers" to 12.50,
                    "currency" to "EGP",
                    "ownerName" to "Abdelrahman Ahmed",
                    "points" to 50,
                    "pointsInCurrency" to 5,
                )

            )
        val getTransactions: HashMap<String, Any?>
            get() = hashMapOf(
                "status" to ApiConstant.Status.SUCCESS.value,
                "data" to listOf(transaction1, transaction2, transaction3, transaction4, transaction5)
            )
        val suggestContact: HashMap<String, Any?>
            get() = hashMapOf(
                "status" to ApiConstant.Status.SUCCESS.value,
                "data" to listOf(contact1)
            )


        // notification
        val getNotifications: HashMap<String, Any?>
            get() = hashMapOf(
                "status" to ApiConstant.Status.SUCCESS.value,
                "data" to listOf(
                    hashMapOf(
                        "id" to 1,
                        "text" to "Your order #456 is in progress",
                        "dateTime" to Calendar.getInstance().apply {
                            this.set(Calendar.HOUR_OF_DAY, 0)
                            this.set(Calendar.MINUTE, 0)
                            this.set(Calendar.SECOND, 0)
                            this.set(Calendar.MILLISECOND, 0)
                            this.set(Calendar.ZONE_OFFSET, 0)
                        }.timeInMillis / 1000,
                        "isRead" to false
                    ),
                    hashMapOf(
                        "id" to 1,
                        "text" to "Your order #356 is in progress",
                        "dateTime" to Calendar.getInstance().apply {
                            this.set(Calendar.HOUR_OF_DAY, 0)
                            this.set(Calendar.MINUTE, 0)
                            this.set(Calendar.SECOND, 0)
                            this.set(Calendar.MILLISECOND, 0)
                            this.set(Calendar.ZONE_OFFSET, 0)
                        }.timeInMillis / 1000,
                        "isRead" to true
                    ),
                )
            )


        // complains
        val getComplains: HashMap<String, Any?>
            get() {
                var list = listOf(
                    hashMapOf(
                        "id" to 1,
                        "number" to 23,
                        "dateTime" to 1617400800,
                        "status" to ApiConstant.ComplainStatus.IN_PROGRESS.value,
                        "description" to "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, "
                    ),
                    hashMapOf(
                        "id" to 2,
                        "number" to 25,
                        "dateTime" to 1617400800,
                        "status" to ApiConstant.ComplainStatus.RESOLVED.value,
                        "description" to "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, "
                    ),
                )
                list = list + list + list + list + list

                return hashMapOf(
                    "status" to ApiConstant.Status.SUCCESS.value,
                    "data" to list
                )
            }

        val getComplainComments: HashMap<String, Any?>
            get() {
                var list = listOf(
                    hashMapOf(
                        "id" to 1,
                        "dateTime" to 1617400800,
                        "comment" to "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the",
                        "commenterName" to "Ayman Ali",
                        "commenterImageUrl" to "photo-1597452485669-2c7bb5fef90d?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=1349&q=80",

                        ),
                    hashMapOf(
                        "id" to 2,
                        "dateTime" to 1617573600,
                        "comment" to "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the",
                        "commenterName" to "Mena Ibrahem",
                        "commenterImageUrl" to "photo-1526401485004-46910ecc8e51?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80",
                    )
                )
                list = list + list + list + list + list

                return hashMapOf(
                    "status" to ApiConstant.Status.SUCCESS.value,
                    "data" to list
                )
            }

        /*val addComment: HashMap<String, Any?>
            get() = hashMapOf(
                "status" to ApiConstant.Status.SUCCESS.value,
                "data" to hashMapOf(
                    "id" to 1,
                    "dateTime" to 1617400800,
                    "comment" to "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the",
                    "commenterName" to "Ayman Ali",
                    "commenterImageUrl" to "photo-1597452485669-2c7bb5fef90d?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=1349&q=80",

                    )
            )*/

    }
}