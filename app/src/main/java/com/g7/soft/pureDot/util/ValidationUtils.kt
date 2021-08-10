package com.g7.soft.pureDot.util

import com.g7.soft.pureDot.constant.ProjectConstant
import com.wajahatkarim3.easyvalidation.core.view_ktx.*


class ValidationUtils {

    private var validationError: ProjectConstant.Companion.ValidationError? = null


    fun setPhoneNumber(text: String?): ValidationUtils {
        if (validationError == null)
            validationError = when {
                text.isNullOrEmpty() -> ProjectConstant.Companion.ValidationError.EMPTY_PHONE_NUMBER
                else -> null
            }
        return this
    }

    fun setPassword(text: String?, validateEmptyOnly: Boolean = false): ValidationUtils {
        if (validationError == null)
            validationError = when {
                text.isNullOrEmpty() -> ProjectConstant.Companion.ValidationError.EMPTY_PASSWORD
                !validateEmptyOnly && (!text.minLength(8) || !text.atleastOneNumber()
                        || !text.atleastOneLowerCase() || !text.atleastOneUpperCase()
                        || !text.atleastOneSpecialCharacters()) -> ProjectConstant.Companion.ValidationError.INVALID_PASSWORD
                else -> null
            }
        return this
    }

    fun setPasswordRepeat(password1: String?, password2: String?): ValidationUtils {
        if (validationError == null)
            validationError = when {
                password1 != password2 -> ProjectConstant.Companion.ValidationError.NON_IDENTICAL_PASSWORD
                else -> null
            }
        return this
    }

    fun setPhoneNumberOrEmail(text: String?): ValidationUtils {
        if (validationError == null)
            validationError = when {
                text.isNullOrEmpty() -> ProjectConstant.Companion.ValidationError.EMPTY_PHONE_NUMBER_OR_EMAIL
                else -> null
            }
        return this
    }


    fun setVerificationCode(text: String?): ValidationUtils {
        if (validationError == null)
            validationError = when {
                text.isNullOrEmpty() -> ProjectConstant.Companion.ValidationError.EMPTY_VERIFICATION_CODE
                !text.minLength(5) -> ProjectConstant.Companion.ValidationError.INVALID_VERIFICATION_CODE
                else -> null
            }
        return this
    }

    fun setFirstName(text: String?): ValidationUtils {
        if (validationError == null)
            validationError = when {
                text.isNullOrEmpty() -> ProjectConstant.Companion.ValidationError.EMPTY_FIRST_NAME
                else -> null
            }
        return this
    }

    fun setLastName(text: String?): ValidationUtils {
        if (validationError == null)
            validationError = when {
                text.isNullOrEmpty() -> ProjectConstant.Companion.ValidationError.EMPTY_LAST_NAME
                else -> null
            }
        return this
    }

    fun setEmail(text: String?): ValidationUtils {
        if (validationError == null)
            validationError = when {
                text.isNullOrEmpty() -> ProjectConstant.Companion.ValidationError.EMPTY_EMAIL
                !text.validEmail() -> ProjectConstant.Companion.ValidationError.INVALID_EMAIL
                else -> null
            }
        return this
    }


    fun setAge(text: String?): ValidationUtils {
        if (validationError == null)
            validationError = when {
                text.isNullOrEmpty() -> ProjectConstant.Companion.ValidationError.EMPTY_AGE
                else -> null
            }
        return this
    }

    fun setHeight(text: String?): ValidationUtils {
        if (validationError == null)
            validationError = when {
                text.isNullOrEmpty() -> ProjectConstant.Companion.ValidationError.EMPTY_HEIGHT
                else -> null
            }
        return this
    }

    fun setWeight(text: String?): ValidationUtils {
        if (validationError == null)
            validationError = when {
                text.isNullOrEmpty() -> ProjectConstant.Companion.ValidationError.EMPTY_WEIGHT
                else -> null
            }
        return this
    }

    fun setAddressSelectedPosition(index: Int?): ValidationUtils {
        if (validationError == null)
            validationError = when (index) {
                null, 0 -> ProjectConstant.Companion.ValidationError.UNSELECTED_ADDRESS
                else -> null
            }
        return this
    }

    fun setConversationMessage(text: String?): ValidationUtils {
        if (validationError == null)
            validationError = when {
                text.isNullOrEmpty() -> ProjectConstant.Companion.ValidationError.EMPTY_CONVERSATION_MESSAGE
                else -> null
            }
        return this
    }

    fun setComplaintTitle(text: String?): ValidationUtils {
        if (validationError == null)
            validationError = when {
                text.isNullOrEmpty() -> ProjectConstant.Companion.ValidationError.EMPTY_COMPLAINT_TITLE
                else -> null
            }
        return this
    }

    fun setComplaintDescription(text: String?): ValidationUtils {
        if (validationError == null)
            validationError = when {
                text.isNullOrEmpty() -> ProjectConstant.Companion.ValidationError.EMPTY_COMPLAINT_DESCRIPTION
                else -> null
            }
        return this
    }

    fun setRating(rating: Float?): ValidationUtils {
        if (validationError == null)
            validationError = when (rating) {
                null, 0f -> ProjectConstant.Companion.ValidationError.EMPTY_RATING_BAR
                else -> null
            }
        return this
    }

    fun setComment(text: String?): ValidationUtils {
        if (validationError == null)
            validationError = when {
                text.isNullOrEmpty() -> ProjectConstant.Companion.ValidationError.EMPTY_FIRST_NAME
                else -> null
            }
        return this
    }

    fun setCountry(text: String?): ValidationUtils {
        if (validationError == null)
            validationError = when {
                text.isNullOrEmpty() -> ProjectConstant.Companion.ValidationError.EMPTY_COUNTRY
                else -> null
            }
        return this
    }

    fun setCity(text: String?): ValidationUtils {
        if (validationError == null)
            validationError = when {
                text.isNullOrEmpty() -> ProjectConstant.Companion.ValidationError.EMPTY_CITY
                else -> null
            }
        return this
    }

    fun setArea(text: String?): ValidationUtils {
        if (validationError == null)
            validationError = when {
                text.isNullOrEmpty() -> ProjectConstant.Companion.ValidationError.EMPTY_AREA
                else -> null
            }
        return this
    }

    fun setZipCode(text: String?): ValidationUtils {
        if (validationError == null)
            validationError = when {
                text.isNullOrEmpty() -> ProjectConstant.Companion.ValidationError.EMPTY_ZIP_CODE
                else -> null
            }
        return this
    }

    fun setAddress(text: String?): ValidationUtils {
        if (validationError == null)
            validationError = when {
                text.isNullOrEmpty() -> ProjectConstant.Companion.ValidationError.EMPTY_ADDRESS
                else -> null
            }
        return this
    }

    fun getError(): ProjectConstant.Companion.ValidationError? = validationError
}