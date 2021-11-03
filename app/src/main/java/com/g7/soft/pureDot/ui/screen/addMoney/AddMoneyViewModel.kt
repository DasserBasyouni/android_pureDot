package com.g7.soft.pureDot.ui.screen.addMoney

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.g7.soft.pureDot.Application
import com.g7.soft.pureDot.constant.ApiConstant
import com.g7.soft.pureDot.model.CheckoutSuccessResponseModel
import com.g7.soft.pureDot.model.StcPayAuthModel
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.PaymentRepository
import com.g7.soft.pureDot.repo.WalletRepository
import com.g7.soft.pureDot.utils.ValidationUtils
import kotlinx.coroutines.Dispatchers

class AddMoneyViewModel : ViewModel() {

    val isStcPayChecked = MutableLiveData<Boolean?>().apply { this.value = false }
    val isMasterCardChecked = MutableLiveData<Boolean?>().apply { this.value = false }

    val masterCardNameOnCard = MutableLiveData<String?>()
    val masterCardNumber = MutableLiveData<String?>()
    val masterCardSecurityCode = MutableLiveData<String?>()
    val masterCardExpiryMonth = MutableLiveData<String?>()
    val masterCardExpiryYear = MutableLiveData<String?>()

    val stcPhoneNumber = MutableLiveData<String?>()
    val stcMobileOtp = MutableLiveData<String?>()

    val stcPayAuthResponse = MediatorLiveData<NetworkRequestResponse<StcPayAuthModel>>()
    val addMoneyResponse = MediatorLiveData<NetworkRequestResponse<CheckoutSuccessResponseModel>>()

    val amount = MutableLiveData<String?>()


    fun addMoney(langTag: String, tokenId: String?) {
        addMoneyResponse.value = NetworkRequestResponse.loading()

        // validate inputs
        ValidationUtils()
            .setAmount(amount.value)
            .getError()?.let {
                addMoneyResponse.value =
                    NetworkRequestResponse.invalidInputData(validationError = it)
                return@let
            }


        addMoneyResponse.apply {
            addSource(
                WalletRepository(langTag).addMoney(
                    tokenId = tokenId,
                    amount = amount.value?.toInt(),
                )
            ) {
                addMoneyResponse.value = it
            }
        }
    }


    fun authenticateStcPay(langTag: String) {
        stcPayAuthResponse.value = NetworkRequestResponse.loading()

        stcPayAuthResponse.apply {
            addSource(
                PaymentRepository(langTag).authenticateStcPay(
                    mobile = stcPhoneNumber.value,
                    masterOrderNumber = addMoneyResponse.value?.data?.orderNumber,
                    masterOrderId = addMoneyResponse.value?.data?.masterOrderId,
                    orderAmount = amount.value?.toDoubleOrNull(),
                    description = addMoneyResponse.value?.data?.description,
                )
            ) {
                stcPayAuthResponse.value = it
            }
        }
    }

    fun confirmStcPay(langTag: String) = liveData(Dispatchers.IO) {
        emit(NetworkRequestResponse.loading())

        emitSource(
            PaymentRepository(langTag).confirmStcPay(
                stcPayPmtReference = stcPayAuthResponse.value?.data?.stcPayPmtReference,
                otpReference = stcPayAuthResponse.value?.data?.otpReference,
                otpValue = stcMobileOtp.value,
                payType = if (Application.isClientFlavour)
                    ApiConstant.PaymentType.DRIVER_WALLET.value
                else ApiConstant.PaymentType.DRIVER_WALLET.value
            )
        )
    }

    fun createMasterCardSession(langTag: String) =
        liveData(Dispatchers.IO) {
            emit(NetworkRequestResponse.loading())

            emitSource(
                PaymentRepository(langTag).createMasterCardSession(
                    masterOrderId = addMoneyResponse.value?.data?.masterOrderId,
                    orderAmount = amount.value?.toDoubleOrNull(),
                    description = addMoneyResponse.value?.data?.description
                )
            )
        }

    fun addMoneyIsPaid(langTag: String, tokenId: String?, isPaid: Boolean) =
        liveData(Dispatchers.IO) {
            emit(NetworkRequestResponse.loading())

            emitSource(
                WalletRepository(langTag).addMoneyIsPaid(
                    tokenId = tokenId,
                    amount = amount.value?.toDoubleOrNull(),
                    isPaid = isPaid,
                    masterOrderId = addMoneyResponse.value?.data?.masterOrderId,
                    paymentMethod = ApiConstant.PaymentMethod.fromBooleans(
                        isMasterCardChecked = isMasterCardChecked.value,
                        isDigitalWallet = false,
                        isStcPayChecked = isStcPayChecked.value,
                        isCashOnDelivery = false,
                    )?.value
                )
            )
        }
}