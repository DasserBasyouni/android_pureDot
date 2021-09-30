package com.g7.soft.pureDot.ui.screen.phoneVerification

import android.os.CountDownTimer
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.lifecycle.*
import com.g7.soft.pureDot.constant.ProjectConstant
import com.g7.soft.pureDot.model.UserDataModel
import com.g7.soft.pureDot.model.project.LceeModel
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.UserRepository
import com.g7.soft.pureDot.utils.ValidationUtils
import com.g7.soft.pureDot.utils.combine
import kotlinx.coroutines.Dispatchers

class PhoneVerificationViewModel(
    val isPasswordReset: Boolean,
    private val emailOrPhoneNumber: String?
) :
    ViewModel() {

    val verificationCode = MutableLiveData<String?>()
    val password = MutableLiveData<String?>()

    val verificationResponse = MediatorLiveData<NetworkRequestResponse<UserDataModel?>>()
    val verificationLcee = MediatorLiveData<LceeModel>().apply {
        this.value = LceeModel()
    }

    val editorActionListener: TextView.OnEditorActionListener
    val performInputsAction = MutableLiveData<Boolean?>()

    // countdown
    private val timer: CountDownTimer
    private val canResend = MutableLiveData<Boolean>()
    private val remainingTimeInSeconds = MutableLiveData<Int>()
    val remainingSeconds = Transformations.map(remainingTimeInSeconds) { it % 60 }
    val remainingMinutes = Transformations.map(remainingTimeInSeconds) { it / 60 }

    // ui visibilities
    var resendVisibility = Transformations.map(canResend.combine(resetPasswordVisibility)) {
        when {
            it.second == View.VISIBLE -> View.GONE
            it.first == true -> View.VISIBLE
            else -> View.GONE
        }
    }
    val countDownVisibility
        get() = Transformations.map(resetPasswordVisibility) {
            if (it == View.VISIBLE) View.GONE else View.VISIBLE
        }
    val resetPasswordVisibility
        get() = Transformations.map(verificationLcee.value!!.contentVisibility) {
            if (it == View.VISIBLE && isPasswordReset) View.VISIBLE else View.GONE
        }


    init {
        this.editorActionListener = TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                performInputsAction.value = true
                true
            } else false
        }

        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {

            override fun onTick(millisUntilFinished: Long) {
                remainingTimeInSeconds.value = (millisUntilFinished / ONE_SECOND).toInt()
            }

            override fun onFinish() {
                remainingTimeInSeconds.value = 0
                canResend.value = true
            }
        }
        startCountDownTimer()
    }

    companion object {
        internal const val ONE_SECOND = 1000L
        internal const val COUNTDOWN_TIME = 5 * 60 * 1000L
    }


    fun verify(langTag: String) {
        verificationResponse.value = NetworkRequestResponse.loading()

        // validate inputs
        ValidationUtils().setVerificationCode(verificationCode.value)
            .getError()?.let {
                verificationResponse.value =
                    NetworkRequestResponse.invalidInputData(validationError = it)
                return
            }

        verificationResponse.apply {
            this.addSource(
                UserRepository(langTag).verify(
                    emailOrPhoneNumber = emailOrPhoneNumber,
                    verificationCode = verificationCode.value
                )
            ) {
                if (it.status == ProjectConstant.Companion.Status.SUCCESS)
                    timer.cancel()

                verificationResponse.value = it
            }
        }
    }

    fun resendCode(langTag: String) = liveData(Dispatchers.IO) {
        emit(NetworkRequestResponse.loading())
        emitSource(
            com.g7.soft.pureDot.repo.UserRepository(langTag).resendVerification(
                emailOrPhoneNumber = emailOrPhoneNumber
            )
        )
    }

    fun changePassword(langTag: String) = liveData(Dispatchers.IO) {
        emit(NetworkRequestResponse.loading())
        emitSource(
            UserRepository(langTag).resetPassword(
                emailOrPhoneNumber = emailOrPhoneNumber,
                password = password.value
            )
        )
    }

    fun startCountDownTimer() {
        canResend.value = false
        timer.start()
    }
}