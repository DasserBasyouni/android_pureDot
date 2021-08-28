package com.g7.soft.pureDot.constant

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
        EXIST_BEFORE(1015);

        companion object {
            fun fromInt(value: Int) = values().first { it.value == value }

            fun getMessageResId(status: Status?): Int = when (status) {
                SUCCESS, NULL_STATUS, ERROR, WRONG_AUTHENTICATION, null -> R.string.something_went_wrong
                EMAIL_EXIST -> R.string.email_exists
                MOBILE_EXIST -> R.string.mobile_exists
                WRONG_PASSWORD -> R.string.wrong_password
                ACCOUNT_NOT_FOUND -> R.string.account_not_fount
                NOT_VERIFIED -> R.string.not_verified
                INCORRECT_VERIFICATION -> R.string.incorrect_verification
                EXIST_BEFORE -> R.string.exists_before
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
        CANCELED(2005);

        /*PENDING(2000),
        ACCEPTED(2001),
        DELIVERED(2002),
        CANCELED(2003),
        OUT_FOR_DELIVERY(2004),
        SHIPPING(2005),
        ORDER_PLACED(2006),
        DRIVER_ACCEPT(2007),
        DRIVER_REJECT(2008);*/

        companion object {
            fun fromInt(value: Int?) = values().firstOrNull { it.value == value }

            fun fromPosition(position: Int?): OrderStatus? {
                return when (position) {
                    0 -> NEW
                    1 -> CONFIRMED
                    2 -> BEING_SHIPPED
                    3 -> PICKED
                    4 -> DELIVERED
                    5 -> CANCELED
                    else -> null
                }
            }

            fun isCancelable(value: Int?) = value == NEW.value

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
        TO_YOUR_ACCOUNT(3001),
        WITHDRAW(3004),
        DEPOSIT(3003),
        FROM_YOUR_ACCOUNT(3004),
        POINTS(3005);

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
        INNER_BEST_SELLING(6005);

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


    companion object {

        const val BASE_URL = "http://207.38.87.126:2222/api/"
        const val SOCKET_IO_URL = "https://mars.udacity.com/"
        const val IMG_BASE_URL = "http://207.38.87.126:3333/"

    }
}