package com.g7.soft.pureDot.util

import com.g7.soft.pureDot.constant.ProjectConstant
import com.g7.soft.pureDot.model.CityModel
import com.g7.soft.pureDot.model.ComplaintOrderModel
import com.g7.soft.pureDot.model.IdNameModel
import com.g7.soft.pureDot.model.ZipCodeModel
import com.wajahatkarim3.easyvalidation.core.view_ktx.*
import java.sql.Timestamp


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

    fun setSelectedRelatedOrder(value: ComplaintOrderModel?): ValidationUtils {
        if (validationError == null)
            validationError = when (value) {
                null -> ProjectConstant.Companion.ValidationError.EMPTY_COMPLAINT_RELATED_ORDER
                else -> null
            }
        return this
    }

    fun setSelectedCategory(value: IdNameModel?): ValidationUtils {
        if (validationError == null)
            validationError = when (value) {
                null -> ProjectConstant.Companion.ValidationError.EMPTY_COMPLAINT_CATEGORY
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

    fun setName(text: String?): ValidationUtils {
        if (validationError == null)
            validationError = when {
                text.isNullOrEmpty() -> ProjectConstant.Companion.ValidationError.EMPTY_NAME
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

    /*fun setAddressSelectedPosition(index: Int?): ValidationUtils {
        if (validationError == null)
            validationError = when (index) {
                null, 0 -> ProjectConstant.Companion.ValidationError.UNSELECTED_ADDRESS
                else -> null
            }
        return this
    }*/

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
                text.isNullOrEmpty() -> ProjectConstant.Companion.ValidationError.EMPTY_COMMENT
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

    fun setCity(value: CityModel?): ValidationUtils {
        if (validationError == null)
            validationError = when {
                value == null -> ProjectConstant.Companion.ValidationError.EMPTY_CITY
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

    fun setZipCode(value: ZipCodeModel?, haveZipCode: Boolean?): ValidationUtils {
        if (validationError == null)
            validationError = when {
                haveZipCode == true && value == null -> ProjectConstant.Companion.ValidationError.EMPTY_ZIP_CODE
                else -> null
            }
        return this
    }

    /*fun setAddress(text: String?): ValidationUtils {
        if (validationError == null)
            validationError = when {
                text.isNullOrEmpty() -> ProjectConstant.Companion.ValidationError.EMPTY_ADDRESS
                else -> null
            }
        return this
    }*/

    fun getError(): ProjectConstant.Companion.ValidationError? = validationError

    fun setDateOfBirth(value: Timestamp?): ValidationUtils {
        if (validationError == null)
            validationError = when (value) {
                null -> ProjectConstant.Companion.ValidationError.EMPTY_DATE_OF_BIRTH
                else -> null
            }
        return this
    }

    fun setCarBrand(value: String?): ValidationUtils {
        if (validationError == null)
            validationError = when (value) {
                null -> ProjectConstant.Companion.ValidationError.EMPTY_CAR_BRAND
                else -> null
            }
        return this
    }

    fun setIsMale(value: Boolean?): ValidationUtils {
        if (validationError == null)
            validationError = when (value) {
                null -> ProjectConstant.Companion.ValidationError.EMPTY_IS_MALE
                else -> null
            }
        return this
    }

    fun setDoAcceptTerms(value: Boolean?): ValidationUtils {
        if (validationError == null)
            validationError = when (value) {
                null -> ProjectConstant.Companion.ValidationError.CHECK_TERMS_ACCEPTANCE
                else -> null
            }
        return this
    }

    fun setLicenceImagePath(value: String?): ValidationUtils {
        if (validationError == null)
            validationError = when (value) {
                null -> ProjectConstant.Companion.ValidationError.EMPTY_LICENCE_IMAGE
                else -> null
            }
        return this
    }

    fun setCarFrontImagePath(value: String?): ValidationUtils {
        if (validationError == null)
            validationError = when (value) {
                null -> ProjectConstant.Companion.ValidationError.EMPTY_CAR_FRONT_IMAGE
                else -> null
            }
        return this
    }

    fun setCarBackImagePath(value: String?): ValidationUtils {
        if (validationError == null)
            validationError = when (value) {
                null -> ProjectConstant.Companion.ValidationError.EMPTY_CAR_BACK_IMAGE
                else -> null
            }
        return this
    }

    fun setNationalIdImagePath(value: String?): ValidationUtils {
        if (validationError == null)
            validationError = when (value) {
                null -> ProjectConstant.Companion.ValidationError.EMPTY_NATIONAL_ID_IMAGE
                else -> null
            }
        return this
    }
}