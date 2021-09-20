package com.g7.soft.pureDot.util

import androidx.fragment.app.Fragment
import com.g7.soft.pureDot.model.OrderReviewModel


open class FlavourProjectDialogUtils {

    companion object {

        fun showOrderRating(
            fragment: Fragment,
            currencySymbol: String,
            userReview: OrderReviewModel?,
            orderNumber: Int?,
            totalOrderCost: Double?,
            onClickPositiveAction: () -> Unit,
        ) = null

    }
}