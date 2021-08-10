package com.g7.soft.pureDot.constant


class ApiConstant {

    enum class Status(val value: Int) {
        NULL_STATUS(998),

        //NULL_DATA(999),
        SUCCESS(1000),
        WRONG_PHONE_OR_PASSWORD(1001);

        companion object {
            fun fromInt(value: Int) = values().first { it.value == value }
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

            // todo check this cycle
            fun isDriverPendingOrAccepted(value: Int?): Boolean = true
                //value == PENDING.value || value == DRIVER_ACCEPT.value || value == OUT_FOR_DELIVERY.value
        }
    }

    enum class OrderDeliveryStatus (val value: Int) {
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

            fun isDriverPendingOrAccepted(value: Int?): Boolean = value == NEW.value
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
            fun fromInt(value: Int) = values().firstOrNull() { it.value == value }
        }
    }

    enum class SliderOfferType(val value: Int) {
        SEARCH_RESULTS(-1),
        HOME_MAIN_SLIDER(5000),
        HOME_CATEGORY_SLIDER(5001),
        HOME_LATEST_PRODUCT(5002),
        INNER_LATEST_PRODUCTS(5003),
        INNER_LATEST_OFFERS(5004),
        INNER_BEST_SELLING(5005);

        companion object {
            fun fromInt(value: Int) = values().firstOrNull() { it.value == value }
        }
    }


    companion object {

        const val BASE_URL = "https://mars.udacity.com/"
        const val SOCKET_IO_URL = "https://mars.udacity.com/"
        const val IMG_BASE_URL = "https://images.unsplash.com/"

    }
}