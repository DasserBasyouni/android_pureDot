package com.g7.soft.pureDot.util

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.model.OrderReviewModel
import com.g7.soft.pureDot.ui.bindPriceWithCurrency
import kotlinx.android.synthetic.main.dialog_rating.*


open class FlavourProjectDialogUtils {

    companion object {

        fun showOrderRating(
            fragment: Fragment,
            currencySymbol: String,
            userReview: OrderReviewModel?,
            orderNumber: Int?,
            totalOrderCost: Double?,
            onClickPositiveAction: () -> Unit,
        ): Dialog {
            val dialog = Dialog(fragment.requireContext())

            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            dialog.setCancelable(true)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_rating)

            // init data
            dialog.findViewById<TextView>(R.id.orderIdTv).text =
                fragment.getString(R.string.conc_order_id_, orderNumber)
            bindPriceWithCurrency(
                dialog.findViewById(R.id.totalPriceTv),
                price = totalOrderCost,
                currency = currencySymbol,
                preText = fragment.getString(R.string.total_)
            )

            if (userReview != null) {
                dialog.disableClicksIv.visibility = View.VISIBLE
                dialog.positiveBtn.visibility = View.GONE

                dialog.orderRatingRb.rating = userReview.orderRating?.toFloat() ?: 0f
                dialog.orderCommentTil.editText?.setText(userReview.orderComment ?: "")
                dialog.deliveryRatingRb.rating = userReview.deliveryRating?.toFloat() ?: 0f
                dialog.deliveryCommentTil.editText?.setText(userReview.deliveryComment ?: "")
            }

            dialog.show()

            dialog.window?.setLayout(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            )

            dialog.findViewById<TextView>(R.id.positiveBtn).apply {
                this.text = context.getString(R.string.submit_review)
                this.setOnClickListener { onClickPositiveAction.invoke() }
            }

            return dialog
            //dialog.findViewById<FrameLayout>(R.id.dialogFl).setOnClickListener { dialog.dismiss() }
        }

    }
}