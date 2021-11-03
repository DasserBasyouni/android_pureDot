package com.g7.soft.pureDot.constant

import android.content.Context
import com.g7.soft.pureDot.R


class ApiConstant {

    enum class Status(val value: Int) {
        NULL_STATUS(998),

        //NULL_DATA(999),
        SUCCESS(1000),
        ERROR(1001),
        WRONG_AUTHENTICATION(1002),
        EMAIL_EXIST(1007),
        MOBILE_EXIST(1008),
        WRONG_PASSWORD(1009),
        ACCOUNT_NOT_FOUND(1010),
        NOT_VERIFIED(1013),
        INCORRECT_VERIFICATION(1014),
        EXIST_BEFORE(1015),
        CAN_NOT_CANCEL_ORDER(1016),
        INVALID_CART_ITEMS(1017),
        CAN_NOT_RETURN(1018),
        CAN_NOT_ACCEPT(1019),
        ORDER_STATUS_CHANGED(1020),
        ORDER_LIMIT(1021),
        SHIPMENT_ERROR(1022),
        WALLET_LIMIT(1023);

        companion object {
            fun fromInt(value: Int) = values().first { it.value == value }

            fun getMessageResId(status: Status?): Int = when (status) {
                SUCCESS, NULL_STATUS, ERROR, WRONG_AUTHENTICATION,  null -> R.string.something_went_wrong
                EMAIL_EXIST -> R.string.email_exists
                MOBILE_EXIST -> R.string.mobile_exists
                WRONG_PASSWORD -> R.string.wrong_password
                ACCOUNT_NOT_FOUND -> R.string.account_not_fount
                NOT_VERIFIED -> R.string.not_verified
                INCORRECT_VERIFICATION -> R.string.incorrect_verification
                EXIST_BEFORE -> R.string.exists_before
                CAN_NOT_CANCEL_ORDER -> R.string.can_not_cancel_order
                INVALID_CART_ITEMS -> R.string.msg_invalid_cart_items
                CAN_NOT_RETURN -> R.string.msg_can_not_return
                CAN_NOT_ACCEPT -> R.string.msg_can_not_accept
                ORDER_STATUS_CHANGED -> R.string.msg_status_already_changed
                ORDER_LIMIT -> R.string.msg_order_does_not_meet_min_
                SHIPMENT_ERROR -> R.string.msg_shipment_error
                WALLET_LIMIT -> R.string.insufficient_amount
            }
        }
    }

    /*enum class CountryCode(val value: String) {
        EGYPT("+20"),
        SAUDI_ARABIA("+966");

        companion object {
            fun fromValue(value: String) = values().first { it.value == value }

            fun toCurrency(context: Context, value: String): String {
                return context.getString(
                    when (values().first { it.value == value }) {
                        EGYPT -> R.string.currency_egypt
                        SAUDI_ARABIA -> R.string.currency_saudi_arabia
                    }
                )
            }
        }
    }*/

    enum class OrderStatus(val value: Int) {
        NEW(2000),
        CONFIRMED(2001),
        BEING_SHIPPED(2002),
        PICKED(2003),
        DELIVERED(2004),
        CANCELED(2005),
        ORDER_IS_OUT(2006);

        companion object {
            fun getSpinnerData(context: Context) = arrayOf(
                context.getString(R.string.select_status),
                context.getString(R.string.label_new),
                context.getString(R.string.confirmed),
                context.getString(R.string.being_shipping),
                context.getString(R.string.picked),
                context.getString(R.string.delivered),
                context.getString(R.string.canceled),
            )

            fun fromInt(value: Int?) = values().firstOrNull { it.value == value }

            fun fromPosition(position: Int?): OrderStatus? {
                return when (position) {
                    0 -> NEW
                    1 -> CONFIRMED
                    2 -> BEING_SHIPPED
                    3 -> PICKED
                    4 -> DELIVERED
                    5 -> CANCELED
                    6 -> ORDER_IS_OUT
                    else -> null
                }
            }

            fun isCancelable(value: Int?) = value == NEW.value

            fun doShopOwnerHaveAction(status: Int?): Boolean =
                status == NEW.value || status == CONFIRMED.value || status == BEING_SHIPPED.value

            fun getShopOwnerNextStatus(status: Int?) = when (fromInt(status)) {
                NEW -> CONFIRMED
                CONFIRMED -> BEING_SHIPPED
                BEING_SHIPPED -> ORDER_IS_OUT
                else -> null
            }

            // todo check this cycle
            fun isReached(currentStatus: Int?, reachedStatus: Int?): Boolean =
                currentStatus ?: -1 >= reachedStatus ?: 0

            // todo check this cycle
            fun isBefore(currentStatus: Int?, reachedStatus: Int?): Boolean =
                currentStatus ?: -1 > reachedStatus ?: 0
        }
    }

    enum class OrderDeliveryStatus(val value: Int) {
        NEW(5000),
        ACCEPTED(5001),
        STARTED(5002),
        PICKED(5003),
        ARRIVED(5004),
        DELIVERED(5005),
        CANCELED(5006),
        REJECTED(5007);

        companion object {
            fun fromInt(value: Int?) = values().firstOrNull { it.value == value }

            fun fromPosition(position: Int?): OrderDeliveryStatus? {
                return when (position) {
                    0 -> NEW
                    1 -> ACCEPTED
                    2 -> STARTED
                    3 -> PICKED
                    4 -> ARRIVED
                    5 -> DELIVERED
                    6 -> CANCELED
                    7 -> REJECTED
                    else -> null
                }
            }

            // todo check this cycle
            fun isReached(currentStatus: Int?, reachedStatus: Int?): Boolean =
                currentStatus ?: -1 >= reachedStatus ?: 0

            // todo check this cycle
            fun isBefore(currentStatus: Int?, reachedStatus: Int?): Boolean =
                currentStatus ?: -1 > reachedStatus ?: 0

            fun isDriverPendingOrAccepted(value: Int?): Boolean =
                value == NEW.value || value == ACCEPTED.value
        }
    }

    enum class TransactionType(val value: Int) {
        REDEEM(3001),
        PAY_FOR_ORDER(3002),
        RETURN_PRODUCT(3003),
        TRANSFER(3004),
        DEPOSIT(3005),
        REFUND(3006),
        REFUND_MONEY(3007),
        DELIVERY_INCOME(3008),
        SALES(3009),
        RETURN_ORDER(3010)

        /*TO_YOUR_ACCOUNT(3001),
        WITHDRAW(3004),
        DEPOSIT(3003),
        FROM_YOUR_ACCOUNT(3004),
        POINTS(3005)*/;

        companion object {
            fun fromInt(value: Int?) = values().firstOrNull { it.value == value }
        }
    }

    enum class ComplainStatus(val value: Int) {
        NEW(4001),
        IN_PROGRESS(4002),
        RESOLVED(4003);

        companion object {
            fun fromInt(value: Int) = values().firstOrNull { it.value == value }
        }
    }

    enum class SliderOfferType(val value: Int) {
        SEARCH_RESULTS(-1),
        HOME_MAIN_SLIDER(6000),
        HOME_CATEGORY_SLIDER(6001),
        HOME_LATEST_PRODUCT(6002),
        INNER_LATEST_PRODUCTS(6003),
        INNER_LATEST_OFFERS(6004),
        INNER_BEST_SELLING(6005),
        INNER_CATEGORY(6006),
        INNER_SHOP(6007);

        companion object {
            fun fromInt(value: Int) = values().firstOrNull { it.value == value }
        }
    }

    enum class RedirectType(val value: Int) {
        SHOP(7000), OFFER(7001), PRODUCT(7002);

        companion object {
            fun fromInt(value: Int?) = values().firstOrNull { it.value == value }
        }
    }

    enum class Language(val value: Int) {
        ARABIC(0),
        ENGLISH(1);

        companion object {
            fun fromInt(value: Int) = values().firstOrNull { it.value == value }

            fun fromIsEnglish(isEnglish: Boolean) = if (isEnglish) ENGLISH else ARABIC
        }
    }

    enum class PaymentType(val value: Int) {
        ORDER(0),
        CLIENT_WALLET(1),
        DRIVER_WALLET(2);

        companion object {
            fun fromInt(value: Int) = values().firstOrNull { it.value == value }
        }
    }

    enum class PaymentMethod(val value: String) {
        CASH_ON_DELIVERY("CashOnDelivery"),
        DIGITAL_WALLET("DigitalWallet"),
        STC_PAY("STCPAY"),
        MASTER_CARD("MASCRD");

        companion object {
            fun fromBooleans(
                isCashOnDelivery: Boolean?,
                isStcPayChecked: Boolean?,
                isMasterCardChecked: Boolean?,
                isDigitalWallet: Boolean?
            ) = when {
                isCashOnDelivery == true -> CASH_ON_DELIVERY
                isDigitalWallet == true -> DIGITAL_WALLET
                isMasterCardChecked == true -> MASTER_CARD
                isStcPayChecked == true -> STC_PAY
                else -> null
            }

            fun fromInt(value: String) = values().firstOrNull { it.value == value }
        }
    }

    companion object {

        const val isDevelopmentApi = false

        // mock api
        //const val BASE_URL = "https://tests.free.beeceptor.com/api/"

        // live
        val BASE_URL = if (isDevelopmentApi)
            "http://207.38.87.126:2222/api/" else "https://api.puredot.com.sa/api/"
        val IMG_BASE_URL = if (isDevelopmentApi)
            "http://207.38.87.126:3333" else "https://cp.puredot.com.sa"
        val MERCHANT_ID = if (isDevelopmentApi) "TEST3000000963" else "3000000963"
        val MASTER_CARD_API_VERSION = if (isDevelopmentApi) "61" else "60"

        //const val SOCKET_IO_URL = "https://mars.udacity.com/"

    }
}