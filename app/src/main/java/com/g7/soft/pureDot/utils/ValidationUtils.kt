package com.g7.soft.pureDot.utils

import android.util.Log
import com.g7.soft.pureDot.constant.ProjectConstant
import com.g7.soft.pureDot.constant.ProjectConstant.Companion.ValidationError.*
import com.g7.soft.pureDot.model.*
import com.wajahatkarim3.easyvalidation.core.view_ktx.*
import java.sql.Timestamp


class ValidationUtils {

    private var validationError: ProjectConstant.Companion.ValidationError? = null


    fun setBankName(text: String?): ValidationUtils {
        if (validationError == null)
            validationError = when {
                text.isNullOrEmpty() -> EMPTY_BANK_NAME
                else -> null
            }
        return this
    }

    fun setIban(text: String?): ValidationUtils {
        if (validationError == null)
            validationError = when {
                text.isNullOrEmpty() -> EMPTY_IBAN
                else -> null
            }
        return this
    }

    fun setStcOtpCode(text: String?): ValidationUtils {
        if (validationError == null)
            validationError = when {
                text.isNullOrEmpty() -> EMPTY_STC_OTP_CODE
                else -> null
            }
        return this
    }

    fun setCouponCode(text: String?): ValidationUtils {
        if (validationError == null)
            validationError = when {
                text.isNullOrEmpty() -> EMPTY_COUPON_CODE
                else -> null
            }
        return this
    }

    fun setPhoneNumber(text: String?): ValidationUtils {
        if (validationError == null)
            validationError = when {
                text.isNullOrEmpty() -> EMPTY_PHONE_NUMBER
                else -> null
            }
        return this
    }

    fun setPassword(text: String?, validateEmptyOnly: Boolean = false): ValidationUtils {
        if (validationError == null)
            validationError = when {
                text.isNullOrEmpty() -> EMPTY_PASSWORD
                !validateEmptyOnly && (!text.minLength(8) || !text.atleastOneNumber()
                        || !text.atleastOneLowerCase() || !text.atleastOneUpperCase()
                        || !text.atleastOneSpecialCharacters()) -> INVALID_PASSWORD
                else -> null
            }
        return this
    }

    fun setPasswordRepeat(password1: String?, password2: String?): ValidationUtils {
        if (validationError == null)
            validationError = when {
                password1 != password2 -> NON_IDENTICAL_PASSWORD
                else -> null
            }
        return this
    }

    fun setSelectedRelatedOrder(value: ComplaintOrderModel?): ValidationUtils {
        if (validationError == null)
            validationError = when (value) {
                null -> EMPTY_COMPLAINT_RELATED_ORDER
                else -> null
            }
        return this
    }

    fun setSelectedCategory(value: IdNameModel?): ValidationUtils {
        if (validationError == null)
            validationError = when (value) {
                null -> EMPTY_COMPLAINT_CATEGORY
                else -> null
            }
        return this
    }

    fun setPhoneNumberOrEmail(text: String?): ValidationUtils {
        if (validationError == null)
            validationError = when {
                text.isNullOrEmpty() -> EMPTY_PHONE_NUMBER_OR_EMAIL
                else -> null
            }
        return this
    }

    fun setAmount(text: String?): ValidationUtils {
        if (validationError == null)
            validationError = when {
                text.isNullOrEmpty() -> EMPTY_AMOUNT
                else -> null
            }
        return this
    }

    fun setVerificationCode(text: String?): ValidationUtils {
        if (validationError == null)
            validationError = when {
                text.isNullOrEmpty() -> EMPTY_VERIFICATION_CODE
                !text.minLength(4) -> INVALID_VERIFICATION_CODE
                else -> null
            }
        return this
    }

    fun setName(text: String?): ValidationUtils {
        if (validationError == null)
            validationError = when {
                text.isNullOrEmpty() -> EMPTY_NAME
                else -> null
            }
        return this
    }

    fun setFirstName(text: String?): ValidationUtils {
        if (validationError == null)
            validationError = when {
                text.isNullOrEmpty() -> EMPTY_FIRST_NAME
                else -> null
            }
        return this
    }

    fun setLastName(text: String?): ValidationUtils {
        if (validationError == null)
            validationError = when {
                text.isNullOrEmpty() -> EMPTY_LAST_NAME
                else -> null
            }
        return this
    }

    fun setEmail(text: String?): ValidationUtils {
        if (validationError == null)
            validationError = when {
                text.isNullOrEmpty() -> EMPTY_EMAIL
                !text.validEmail() -> INVALID_EMAIL
                else -> null
            }
        return this
    }

    fun setMessage(text: String?): ValidationUtils {
        if (validationError == null)
            validationError = when {
                text.isNullOrEmpty() -> EMPTY_MESSAGE
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
                text.isNullOrEmpty() -> EMPTY_COMPLAINT_TITLE
                else -> null
            }
        return this
    }

    fun setComplaintDescription(text: String?): ValidationUtils {
        if (validationError == null)
            validationError = when {
                text.isNullOrEmpty() -> EMPTY_COMPLAINT_DESCRIPTION
                else -> null
            }
        return this
    }

    fun setRating(rating: Float?): ValidationUtils {
        if (validationError == null)
            validationError = when (rating) {
                null, 0f -> EMPTY_RATING_BAR
                else -> null
            }
        return this
    }

    fun setComment(text: String?): ValidationUtils {
        if (validationError == null)
            validationError = when {
                text.isNullOrEmpty() -> EMPTY_COMMENT
                else -> null
            }
        return this
    }

    fun setCountry(value: CountryModel?): ValidationUtils {
        if (validationError == null)
            validationError = when (value) {
                null -> EMPTY_COUNTRY
                else -> null
            }
        return this
    }

    fun setCity(value: CityModel?): ValidationUtils {
        if (validationError == null)
            validationError = when (value) {
                null -> EMPTY_CITY
                else -> null
            }
        return this
    }

    fun setArea(text: String?): ValidationUtils {
        if (validationError == null)
            validationError = when {
                text.isNullOrEmpty() -> EMPTY_AREA
                else -> null
            }
        return this
    }

    fun setStreetName(text: String?): ValidationUtils {
        if (validationError == null)
            validationError = when {
                text.isNullOrEmpty() -> EMPTY_STREET_NAME
                else -> null
            }
        return this
    }

    fun setFlat(text: String?): ValidationUtils {
        if (validationError == null)
            validationError = when {
                text.isNullOrEmpty() -> EMPTY_FLAT
                else -> null
            }
        return this
    }

    fun setFloor(text: String?): ValidationUtils {
        if (validationError == null)
            validationError = when {
                text.isNullOrEmpty() -> EMPTY_FLOOR
                else -> null
            }
        return this
    }

    fun setBuildingNumber(text: String?): ValidationUtils {
        if (validationError == null)
            validationError = when {
                text.isNullOrEmpty() -> EMPTY_BUILDING_NUMBER
                else -> null
            }
        return this
    }

    fun setZipCode(value: ZipCodeModel?, haveZipCode: Boolean? = true): ValidationUtils {
        if (validationError == null)
            validationError = when {
                haveZipCode == true && value == null -> EMPTY_ZIP_CODE
                else -> null
            }
        return this
    }

    fun setNameOnCard(text: String?): ValidationUtils {
        if (validationError == null)
            validationError = when {
                text.isNullOrEmpty() -> EMPTY_NAME_ON_CARD
                else -> null
            }
        return this
    }

    fun setCardNumber(text: String?): ValidationUtils {
        if (validationError == null)
            validationError = when {
                text.isNullOrEmpty() -> EMPTY_CARD_NUMBER
                else -> null
            }
        return this
    }

    fun setCardSecurityCode(text: String?): ValidationUtils {
        if (validationError == null)
            validationError = when {
                text.isNullOrEmpty() -> EMPTY_CARD_SECURITY_CODE
                else -> null
            }
        return this
    }

    fun setCardExpiryMonth(text: String?): ValidationUtils {
        if (validationError == null)
            validationError = when {
                text.isNullOrEmpty() -> EMPTY_CARD_EXPIRY_MONTH
                text.toIntOrNull() ?: 0 > 12 -> INVALID_CARD_EXPIRY_MONTH
                else -> null
            }
        return this
    }

    fun setCardExpiryYear(text: String?): ValidationUtils {
        if (validationError == null)
            validationError = when {
                text.isNullOrEmpty() -> EMPTY_CARD_EXPIRY_YEAR
                //text.toIntOrNull() ?: 0 > 31 -> INVALID_CARD_EXPIRY_YEAR
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
                null -> EMPTY_DATE_OF_BIRTH
                else -> null
            }
        return this
    }

    fun setCarBrand(value: String?): ValidationUtils {
        if (validationError == null)
            validationError = when (value) {
                null -> EMPTY_CAR_BRAND
                else -> null
            }
        return this
    }

    fun setIsMale(value: Boolean?): ValidationUtils {
        if (validationError == null)
            validationError = when (value) {
                null -> EMPTY_IS_MALE
                else -> null
            }
        return this
    }

    fun setDoAcceptTerms(value: Boolean?): ValidationUtils {
        if (validationError == null)
            validationError = when (value) {
                null, false -> CHECK_TERMS_ACCEPTANCE
                else -> null
            }
        return this
    }

    fun setLicenceImagePath(value: String?): ValidationUtils {
        if (validationError == null)
            validationError = when (value) {
                null -> EMPTY_LICENCE_IMAGE
                else -> null
            }
        return this
    }

    fun setCarFrontImagePath(value: String?): ValidationUtils {
        if (validationError == null)
            validationError = when (value) {
                null -> EMPTY_CAR_FRONT_IMAGE
                else -> null
            }
        return this
    }

    fun setCarBackImagePath(value: String?): ValidationUtils {
        if (validationError == null)
            validationError = when (value) {
                null -> EMPTY_CAR_BACK_IMAGE
                else -> null
            }
        return this
    }

    fun setNationalIdImagePath(value: String?): ValidationUtils {
        if (validationError == null)
            validationError = when (value) {
                null -> EMPTY_NATIONAL_ID_IMAGE
                else -> null
            }
        return this
    }

    fun workingDays(workingDays: List<WorkingDayModel>?): ValidationUtils {
        if (validationError == null)
            validationError = when {

                workingDays?.firstOrNull {
                    it.times?.firstOrNull { time -> time.fromTime == null } != null
                } != null -> INVALID_FROM_TIME

                workingDays?.firstOrNull {
                    it.times?.firstOrNull { time -> time.toTime == null } != null
                } != null -> INVALID_TO_TIME

                workingDays?.firstOrNull {
                    it.times?.firstOrNull { time ->
                        Log.e("Z_", "${time.fromTime}, ${time.toTime}")
                        time.fromTime!! >= time.toTime!!
                    } != null
                } != null -> INVALID_TIME_RANGE

                else -> null
            }
        return this
    }

    fun setLength(text: String?): ValidationUtils {
        if (validationError == null)
            validationError = when {
                text.isNullOrEmpty() -> EMPTY_LENGTH
                else -> null
            }
        return this
    }

    fun setWidth(text: String?): ValidationUtils {
        if (validationError == null)
            validationError = when {
                text.isNullOrEmpty() -> EMPTY_WIDTH
                else -> null
            }
        return this
    }

    fun setHeight(text: String?): ValidationUtils {
        if (validationError == null)
            validationError = when {
                text.isNullOrEmpty() -> EMPTY_HEIGHT
                else -> null
            }
        return this
    }

    fun setWeight(text: String?): ValidationUtils {
        if (validationError == null)
            validationError = when {
                text.isNullOrEmpty() -> EMPTY_WEIGHT
                else -> null
            }
        return this
    }

    fun setDescription(text: String?): ValidationUtils {
        if (validationError == null)
            validationError = when {
                text.isNullOrEmpty() -> EMPTY_DESCRIPTION
                else -> null
            }
        return this
    }
}