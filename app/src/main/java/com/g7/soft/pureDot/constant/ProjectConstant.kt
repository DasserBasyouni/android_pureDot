package com.g7.soft.pureDot.constant

import com.g7.soft.pureDot.R

class ProjectConstant {
    companion object {
        const val MIN_PRICE_RANGE = 0
        const val MAX_PRICE_RANGE = 12000

        const val DEFAULT_LANGUAGE_TAG = "ar"

        const val BUNDLE_PAGE_NUMBER = "page_number"
        const val BUNDLE_DAILY_NEEDS = "daily_needs"
        const val BUNDLE_RATING = "rating"
        const val BUNDLE_ADDRESS = "address"
        const val BUNDLE_NOTIFICATION = "notification"


        const val REQUEST_ENABLE_LOCATION_SETTINGS = 100
        const val REQUEST_CODE_SUBMIT_REVIEW = 200
        const val RESULTS_CODE_GPS_PERMISSION = 1237
        const val RESULTS_ADD_ADDRESS = "400"

        const val ITEMS_PER_PAGE = 10

        enum class Status {
            IDLE,
            LOADING,
            VALIDATION_ERROR,
            SUCCESS,
            API_ERROR,
            NETWORK_ERROR
        }

        enum class ValidationError {
            EMPTY_PHONE_NUMBER,
            EMPTY_STC_OTP_CODE,
            EMPTY_COUPON_CODE,
            EMPTY_PASSWORD,
            INVALID_PASSWORD,
            EMPTY_PHONE_NUMBER_OR_EMAIL,
            EMPTY_VERIFICATION_CODE,
            INVALID_VERIFICATION_CODE,
            NON_IDENTICAL_PASSWORD,
            EMPTY_AMOUNT,
            EMPTY_MESSAGE,
            EMPTY_NAME,
            EMPTY_FIRST_NAME,
            EMPTY_LAST_NAME,
            EMPTY_EMAIL,
            INVALID_EMAIL,

            //UNSELECTED_ADDRESS,
            EMPTY_COMPLAINT_TITLE,
            EMPTY_COMPLAINT_DESCRIPTION,
            EMPTY_COMPLAINT_RELATED_ORDER,
            EMPTY_COMPLAINT_CATEGORY,
            EMPTY_RATING_BAR,
            EMPTY_COMMENT,
            EMPTY_COUNTRY,
            EMPTY_CITY,
            EMPTY_AREA,
            EMPTY_ZIP_CODE,
            EMPTY_STREET_NAME,
            EMPTY_FLAT,
            EMPTY_FLOOR,
            EMPTY_BUILDING_NUMBER,

            //EMPTY_ADDRESS,
            EMPTY_DATE_OF_BIRTH,
            EMPTY_CAR_BRAND,
            EMPTY_IS_MALE,
            EMPTY_LICENCE_IMAGE,
            EMPTY_CAR_FRONT_IMAGE,
            EMPTY_CAR_BACK_IMAGE,
            EMPTY_NATIONAL_ID_IMAGE,
            CHECK_TERMS_ACCEPTANCE,
            INVALID_FROM_TIME,
            INVALID_TO_TIME,
            INVALID_TIME_RANGE,
            EMPTY_BANK_NAME,
            EMPTY_IBAN,
            EMPTY_NAME_ON_CARD,
            EMPTY_CARD_NUMBER,
            EMPTY_CARD_SECURITY_CODE,
            EMPTY_CARD_EXPIRY_MONTH,
            INVALID_CARD_EXPIRY_MONTH,
            EMPTY_CARD_EXPIRY_YEAR,
            INVALID_CARD_EXPIRY_YEAR,
            EMPTY_LENGTH,
            EMPTY_WIDTH,
            EMPTY_HEIGHT,
            EMPTY_WEIGHT,
            EMPTY_DESCRIPTION;

            // don't change null of some fields here, as that will make a popup appears + the editText validation in every screen
            fun toMessageResId() = when (this) {
                EMPTY_COUPON_CODE -> null
                EMPTY_CITY -> R.string.error_empty_city
                EMPTY_ZIP_CODE -> R.string.error_empty_zip_code
                EMPTY_PASSWORD -> null //R.string.error_empty_password
                INVALID_PASSWORD -> null //R.string.error_invalid_password
                NON_IDENTICAL_PASSWORD -> null //R.string.error_non_identical_passwords
                EMPTY_IS_MALE -> R.string.error_empty_is_male
                CHECK_TERMS_ACCEPTANCE -> R.string.error_accept_terms
                EMPTY_PHONE_NUMBER -> null //R.string.error_empty_phone_number
                EMPTY_PHONE_NUMBER_OR_EMAIL -> null //R.string.error_empty_phone_number_or_email
                EMPTY_VERIFICATION_CODE -> null //R.string.error_empty_verification_code
                INVALID_VERIFICATION_CODE -> null //R.string.error_invalid_verification_code
                EMPTY_NAME -> null //R.string.error_empty_name
                EMPTY_FIRST_NAME -> null //R.string.error_empty_first_name
                EMPTY_LAST_NAME -> null //R.string.error_empty_last_name
                EMPTY_EMAIL -> null //R.string.error_empty_email
                INVALID_EMAIL -> null //R.string.error_invalid_email
                //UNSELECTED_ADDRESS ->
                EMPTY_COMPLAINT_TITLE -> null //
                EMPTY_COMPLAINT_DESCRIPTION -> null //
                EMPTY_RATING_BAR -> R.string.error_empty_rating
                EMPTY_COMMENT -> null //
                EMPTY_COUNTRY -> R.string.error_empty_country
                EMPTY_AREA -> null //
                //EMPTY_ADDRESS ->
                EMPTY_DATE_OF_BIRTH -> null //
                EMPTY_CAR_BRAND -> null //
                EMPTY_STREET_NAME -> null //
                EMPTY_FLAT -> null //
                EMPTY_FLOOR -> null //
                EMPTY_BUILDING_NUMBER -> null //
                EMPTY_MESSAGE -> null
                EMPTY_AMOUNT -> null
                EMPTY_LICENCE_IMAGE -> R.string.error_empty_licence_image
                EMPTY_CAR_FRONT_IMAGE -> R.string.error_empty_car_front_image
                EMPTY_CAR_BACK_IMAGE -> R.string.error_empty_car_back_image
                EMPTY_NATIONAL_ID_IMAGE -> R.string.error_empty_national_id_image
                EMPTY_COMPLAINT_RELATED_ORDER -> R.string.error_empty_related_order
                EMPTY_COMPLAINT_CATEGORY -> R.string.error_empty_category
                INVALID_FROM_TIME -> R.string.error_invalid_from_time
                INVALID_TO_TIME -> R.string.error_invalid_to_time
                INVALID_TIME_RANGE -> R.string.error_invalid_time_range
                EMPTY_BANK_NAME -> null
                EMPTY_IBAN -> null
                EMPTY_NAME_ON_CARD -> null
                EMPTY_CARD_NUMBER -> null
                EMPTY_CARD_SECURITY_CODE -> null
                EMPTY_CARD_EXPIRY_MONTH -> null
                INVALID_CARD_EXPIRY_MONTH -> null
                EMPTY_CARD_EXPIRY_YEAR -> null
                INVALID_CARD_EXPIRY_YEAR -> null
                EMPTY_STC_OTP_CODE -> null
                EMPTY_LENGTH -> null
                EMPTY_WIDTH -> null
                EMPTY_HEIGHT -> null
                EMPTY_WEIGHT -> null
                EMPTY_DESCRIPTION -> null
            }
        }

        enum class ConversationType {
            REQUEST_TRAINING_PLAN,
            NORMAL_CHAT,
            VALIDATION_ERROR,
            SUCCESS,
            API_ERROR,
            NETWORK_ERROR
        }

    }
}