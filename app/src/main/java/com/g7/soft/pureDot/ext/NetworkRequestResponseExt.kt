package com.g7.soft.pureDot.ext

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.constant.ApiConstant
import com.g7.soft.pureDot.constant.ProjectConstant
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.util.ProjectDialogUtils
import kotlinx.coroutines.launch


fun <T> LiveData<NetworkRequestResponse<T>>.observeApiResponse(
    fragment: Fragment,
    successObserve: suspend (data: T?) -> Unit,
    validationObserve: (suspend (validationError: ProjectConstant.Companion.ValidationError?) -> Unit)? = null,
    doLoading: Boolean = true,
    apiStatusToObserve: Array<ApiConstant.Status>? = null,
    chosenApiStatusObserve: (suspend (status: ApiConstant.Status) -> Unit)? = null
) {
    this.observe(fragment, {
        Log.e("Z_", "here - ${it.status} - ${it.apiErrorStatus} - ${it.exception}")
        fragment.context?.let { context ->
            when (it.status) {
                ProjectConstant.Companion.Status.LOADING -> {
                    if (doLoading) ProjectDialogUtils.showLoading(context)
                }
                ProjectConstant.Companion.Status.VALIDATION_ERROR -> {
                    if (doLoading) ProjectDialogUtils.hideLoading()

                    fragment.lifecycleScope.launch { validationObserve?.invoke(it.validationError) }

                    /*val msgResId: Int? = when (it.validationError) {
                        ProjectConstant.Companion.ValidationError.EMPTY_PHONE_NUMBER,
                        ProjectConstant.Companion.ValidationError.EMPTY_PASSWORD ->
                            R.string.msg_empty_login_data
                        ProjectConstant.Companion.ValidationError.INVALID_PASSWORD ->
                            R.string.msg_invalid_password
                        ProjectConstant.Companion.ValidationError.EMPTY_PHONE_NUMBER_OR_EMAIL ->
                            R.string.msg_empty_email_or_password
                        ProjectConstant.Companion.ValidationError.EMPTY_VERIFICATION_CODE -> R.string.msg_empty_verify_code
                        ProjectConstant.Companion.ValidationError.INVALID_VERIFICATION_CODE -> R.string.msg_invalid_verify_code
                        ProjectConstant.Companion.ValidationError.NON_IDENTICAL_PASSWORD -> R.string.msg_non_identical_password
                        ProjectConstant.Companion.ValidationError.EMPTY_FIRST_NAME -> R.string.msg_empty_first_name
                        ProjectConstant.Companion.ValidationError.EMPTY_LAST_NAME -> R.string.msg_empty_last_name
                        ProjectConstant.Companion.ValidationError.EMPTY_EMAIL -> R.string.msg_empty_email
                        ProjectConstant.Companion.ValidationError.INVALID_EMAIL -> R.string.msg_invalid_email
                        ProjectConstant.Companion.ValidationError.EMPTY_AGE -> TODO()
                        ProjectConstant.Companion.ValidationError.EMPTY_HEIGHT -> TODO()
                        ProjectConstant.Companion.ValidationError.EMPTY_WEIGHT -> TODO()
                        ProjectConstant.Companion.ValidationError.UNSELECTED_ACTIVITY_LEVEL -> TODO()
                        ProjectConstant.Companion.ValidationError.EMPTY_CONVERSATION_MESSAGE -> TODO()
                        ProjectConstant.Companion.ValidationError.EMPTY_COMPLAINT_TITLE -> TODO()
                        ProjectConstant.Companion.ValidationError.EMPTY_COMPLAINT_DESCRIPTION -> TODO()
                        ProjectConstant.Companion.ValidationError.EMPTY_RATING_BAR -> TODO()
                        ProjectConstant.Companion.ValidationError.EMPTY_COMMENT -> TODO()
                        ProjectConstant.Companion.ValidationError.EMPTY_COUNTRY -> TODO()
                        ProjectConstant.Companion.ValidationError.EMPTY_CITY -> TODO()
                        ProjectConstant.Companion.ValidationError.EMPTY_AREA -> TODO()
                        ProjectConstant.Companion.ValidationError.EMPTY_ZIP_CODE -> TODO()
                        ProjectConstant.Companion.ValidationError.EMPTY_ADDRESS -> TODO()
                        null -> null
                    }
                    msgResId?.let { it -> ProjectDialogUtils.showSimpleMessage(context, it) }*/
                }
                ProjectConstant.Companion.Status.SUCCESS -> {
                    if (doLoading) ProjectDialogUtils.hideLoading()

                    fragment.lifecycleScope.launch { successObserve(it.data) }
                }
                ProjectConstant.Companion.Status.API_ERROR -> {
                    if (doLoading) ProjectDialogUtils.hideLoading()

                    when {
                        it.apiErrorStatus == null -> {
                            ProjectDialogUtils.showSimpleMessage(
                                fragment.requireContext(),
                                R.string.something_went_wrong,
                                R.drawable.ic_secure_shield
                            )
                        }
                        apiStatusToObserve?.contains(it.apiErrorStatus) == true -> {
                            fragment.lifecycleScope.launch {
                                chosenApiStatusObserve?.let { function -> function(it.apiErrorStatus) }
                            }
                        }
                        else ->
                            ProjectDialogUtils.showSimpleMessage(
                                fragment.requireContext(),
                                ApiConstant.Status.getMessageResId(it.apiErrorStatus),
                                R.drawable.ic_secure_shield
                            )

                    }
                }
                ProjectConstant.Companion.Status.NETWORK_ERROR -> {
                    if (doLoading) ProjectDialogUtils.hideLoading()
                }
            }
        }
    })
}
