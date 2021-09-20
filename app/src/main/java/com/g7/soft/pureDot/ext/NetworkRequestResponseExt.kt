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

                    val popupMessageResId = it.validationError?.toMessageResId()
                    if (popupMessageResId != null)
                        ProjectDialogUtils.showSimpleMessage(
                            fragment.requireContext(),
                            messageResId = popupMessageResId,
                            drawableResId = R.drawable.ic_secure_shield
                        )
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
                else -> {
                    if (doLoading)
                        ProjectDialogUtils.hideLoading()
                    ProjectDialogUtils.showSimpleMessage(
                        fragment.requireContext(),
                        R.string.something_went_wrong,
                        R.drawable.ic_secure_shield
                    )
                }
            }
        }
    })
}
