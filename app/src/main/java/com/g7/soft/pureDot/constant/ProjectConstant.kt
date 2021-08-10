package com.g7.soft.pureDot.constant

class ProjectConstant {
    companion object{
        const val MIN_PRICE_RANGE = 0
        const val MAX_PRICE_RANGE = 12000

        const val DEFAULT_LANGUAGE_TAG = "ar"

        const val BUNDLE_PAGE_NUMBER = "page_number"
        const val BUNDLE_DAILY_NEEDS = "daily_needs"
        const val BUNDLE_RATING = "rating"

        const val REQUEST_CODE_PERSONAL_CALCULATOR = 100
        const val REQUEST_CODE_SUBMIT_REVIEW = 200
        const val RESULTS_CODE_GPS_PERMISSION = 1237

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
            EMPTY_PASSWORD,
            INVALID_PASSWORD,
            EMPTY_PHONE_NUMBER_OR_EMAIL,
            EMPTY_VERIFICATION_CODE,
            INVALID_VERIFICATION_CODE,
            NON_IDENTICAL_PASSWORD,
            EMPTY_FIRST_NAME,
            EMPTY_LAST_NAME,
            EMPTY_EMAIL,
            INVALID_EMAIL,
            EMPTY_AGE,
            EMPTY_HEIGHT,
            EMPTY_WEIGHT,
            UNSELECTED_ADDRESS,
            EMPTY_CONVERSATION_MESSAGE,
            EMPTY_COMPLAINT_TITLE,
            EMPTY_COMPLAINT_DESCRIPTION,
            EMPTY_RATING_BAR,
            EMPTY_COMMENT,
            EMPTY_COUNTRY,
            EMPTY_CITY,
            EMPTY_AREA,
            EMPTY_ZIP_CODE,
            EMPTY_ADDRESS,
            EMPTY_SELF_DESCRIPTION,
            //INVALID_PHONE_NUMBER_OR_EMAIL,
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