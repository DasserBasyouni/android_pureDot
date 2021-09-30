package com.g7.soft.pureDot.repo

import androidx.lifecycle.liveData
import com.g7.soft.pureDot.network.Fetcher
import com.g7.soft.pureDot.network.NetworkRequestHandler
import kotlinx.coroutines.Dispatchers

class PaymentRepository(private val langTag: String) {

    fun confirmStcPay(
        stcPayPmtReference: String?,
        otpValue: String?,
        otpReference: String?,
        payType: Int?
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.confirmStcPay(
                stcPayPmtReference = stcPayPmtReference,
                otpValue = otpValue,
                otpReference = otpReference,
                payType = payType
            )
        }))
    }

    fun authenticateStcPay(
        mobile: String?,
        masterOrderNumber: Int?,
        masterOrderId: String?,
        orderAmount: Double?,
        description: String?
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.authenticateStcPay(
                mobile = mobile,
                masterOrderNumber = masterOrderNumber,
                masterOrderId = masterOrderId,
                orderAmount = orderAmount,
                description = description
            )
        }))
    }

    fun createMasterCardSession(
        masterOrderId: String?,
        orderAmount: Double?,
        description: String?
    ) = liveData(Dispatchers.IO) {
        emitSource(NetworkRequestHandler().handle(request = {
            return@handle Fetcher().getInstance(langTag)?.createMasterCardSession(
                masterOrderId = masterOrderId,
                orderAmount = orderAmount,
                description = description
            )
        }))
    }
}