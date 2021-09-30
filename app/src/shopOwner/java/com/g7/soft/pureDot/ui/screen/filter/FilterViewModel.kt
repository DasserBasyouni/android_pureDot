package com.g7.soft.pureDot.ui.screen.filter

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.g7.soft.pureDot.constant.ApiConstant
import java.util.*

class FilterViewModel : ViewModel() {

    val customerName = MutableLiveData<String?>()

    val fromDateCalendar =
        MutableLiveData<Calendar?>().apply { this.value = Calendar.getInstance() }
    val fromDate = MutableLiveData<String?>()

    val toDateCalendar =
        MutableLiveData<Calendar?>().apply { this.value = Calendar.getInstance() }
    val toDate = MutableLiveData<String?>()

    val selectedStatusPosition = MutableLiveData<Int?>().apply { this.value = 0 }
    val selectedStatus: ApiConstant.OrderStatus?
        get() {
            Log.e(
                "Z_",
                "gg: ${selectedStatusPosition.value} - ${
                    ApiConstant.OrderStatus.fromPosition(selectedStatusPosition.value!!.minus(1))
                }"
            )
            return ApiConstant.OrderStatus.fromPosition(selectedStatusPosition.value!!.minus(1))
        }


    val orderNumber = MutableLiveData<String?>()


    fun resetFilter() {
        customerName.value = null
        fromDateCalendar.value = Calendar.getInstance()
        fromDate.value = null
        toDateCalendar.value = Calendar.getInstance()
        toDate.value = null
        selectedStatusPosition.value = 0
        orderNumber.value = null
    }
}