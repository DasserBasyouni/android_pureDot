package com.g7.soft.pureDot.util

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import com.g7.soft.pureDot.Application
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.ext.dpToPx
import com.g7.soft.pureDot.ext.observeApiResponse
import com.g7.soft.pureDot.model.OrderReviewModel
import com.g7.soft.pureDot.ui.bindPriceWithCurrency
import com.g7.soft.pureDot.ui.screen.order.OrderFragment
import com.g7.soft.pureDot.ui.screen.order.OrderViewModel
import com.zeugmasolutions.localehelper.currentLocale
import kotlinx.android.synthetic.main.dialog_rating.*


// todo a cleaner solution?
class ProjectDialogUtils {

    companion object {

        //private var driverRegisterConfirmation: Dialog? = null
        //private var noConnectionDialog: Dialog? = null
        private var loadingDialog: Dialog? = null

        fun showLoading(context: Context) {
            loadingDialog = Dialog(context)
            loadingDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val progressBar = ProgressBar(context, null, android.R.attr.progressBarStyleLarge)
            progressBar.indeterminateDrawable.colorFilter =
                BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                    ContextCompat.getColor(
                        context,
                        if (Application.isClientFlavour) R.color.bluish else R.color.driverRed
                    ),
                    BlendModeCompat.SRC_ATOP
                )

            loadingDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            loadingDialog?.setCancelable(false)
            loadingDialog?.setContentView(progressBar)

            loadingDialog?.show()

            loadingDialog?.window?.setLayout(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
            )
            (progressBar.layoutParams as FrameLayout.LayoutParams).let {
                it.width = 64.dpToPx()
                it.height = 64.dpToPx()
                it.gravity = Gravity.CENTER
            }

            progressBar.requestLayout()
        }

        fun hideLoading(): Unit = if (loadingDialog?.isShowing == true) {
            loadingDialog?.dismiss()
            loadingDialog = null
        } else Unit

        fun showSimpleMessage(
            context: Context,
            messageResId: Int,
            drawableResId: Int,
            title: String? = null,
            positiveBtnTextResId: Int = R.string.ok,
            positiveBtnOnClick: (() -> Unit)? = null
        ) {
            val dialog = Dialog(context)

            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            dialog.setCancelable(false)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_simple)

            dialog.show()

            dialog.window?.setLayout(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            )

            dialog.findViewById<ImageView>(R.id.imageView).setImageResource(drawableResId)

            dialog.findViewById<TextView>(R.id.titleTv).apply {
                if (title == null)
                    this.visibility = View.GONE
                else
                    this.text = title
            }
            dialog.findViewById<TextView>(R.id.messageTv).text = context.getString(messageResId)

            dialog.findViewById<TextView>(R.id.positiveBtn).apply {
                this.text = context.getString(positiveBtnTextResId)
                this.setOnClickListener {
                    dialog.dismiss()
                    positiveBtnOnClick?.invoke()
                }
            }

            //dialog.findViewById<FrameLayout>(R.id.dialogFl).setOnClickListener { dialog.dismiss() }
        }

        fun showCheckoutTopPopup(
            context: Context,
            itemName: String?,
            totalPriceInCart: Double?,
            currency: String?,
            positiveBtnOnClick: (() -> Unit)? = null
        ) {
            val dialog = Dialog(context)

            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.popup_chekout)

            dialog.show()

            dialog.window?.setLayout(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
            )

            dialog.findViewById<TextView>(R.id.itemNameTv).text = itemName
            bindPriceWithCurrency(
                textView = dialog.findViewById(R.id.totalPriceTv),
                price = totalPriceInCart,
                currency = currency,
                discountPercentage = null
            )

            dialog.findViewById<Button>(R.id.positiveBtn).setOnClickListener {
                dialog.dismiss()
                positiveBtnOnClick?.invoke()
            }
            dialog.findViewById<Button>(R.id.negativeBtn).setOnClickListener {
                dialog.dismiss()
            }

            dialog.findViewById<FrameLayout>(R.id.dialogFl).setOnClickListener { dialog.dismiss() }
        }

        fun showAskingDialog(
            context: Context,
            iconResId: Int,
            titleResId: Int,
            messageResId: Int,
            positiveRunnable: Runnable
        ) {
            val dialog = Dialog(context)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.project_asking_dialog)

            dialog.findViewById<ImageView>(R.id.iconIv).setImageResource(iconResId)
            dialog.findViewById<TextView>(R.id.titleTv).text = context.getString(titleResId)
            dialog.findViewById<TextView>(R.id.messageTv).text = context.getString(messageResId)

            dialog.findViewById<Button>(R.id.positiveBtn).setOnClickListener {
                positiveRunnable.run()
                dialog.dismiss()
            }

            dialog.findViewById<Button>(R.id.negativeBtn).setOnClickListener { dialog.dismiss() }

            dialog.show()

            dialog.window?.setLayout(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
            )
        }

        fun showOrderRating(
            fragment: OrderFragment,
            viewModel: OrderViewModel
        ) {
            val dialog = Dialog(fragment.requireContext())

            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            dialog.setCancelable(true)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_rating)

            // init data
            dialog.findViewById<TextView>(R.id.orderIdTv).text =
                fragment.getString(R.string.conc_order_id_, viewModel.order?.number)
            bindPriceWithCurrency(
                dialog.findViewById(R.id.totalPriceTv),
                price = viewModel.order?.price,
                currency = viewModel.order?.currency,
                preText = fragment.getString(R.string.total_)
            )

            val review = viewModel.order?.review
            if (review != null) {
                dialog.disableClicksIv.visibility = View.VISIBLE
                dialog.positiveBtn.visibility = View.GONE

                dialog.orderRatingRb.rating = review.orderRating ?: 0f
                dialog.orderCommentTil.editText?.setText(review.orderComment ?: "")
                dialog.deliveryRatingRb.rating = review.deliveryRating ?: 0f
                dialog.deliveryCommentTil.editText?.setText(review.deliveryComment ?: "")
            }

            dialog.show()

            dialog.window?.setLayout(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            )

            dialog.findViewById<TextView>(R.id.positiveBtn).apply {
                this.text = context.getString(R.string.submit_review)
                this.setOnClickListener {
                    val tokenId = "" //todo
                    viewModel.rateOrder(
                        langTag = fragment.requireActivity().currentLocale.toLanguageTag(),
                        tokenId = tokenId,
                        orderRating = dialog.orderRatingRb.rating,
                        orderComment = dialog.orderCommentTil.editText?.text?.toString(),
                        deliveryRating = dialog.deliveryRatingRb.rating,
                        deliveryComment = dialog.deliveryCommentTil.editText?.text?.toString()
                    ).observeApiResponse(fragment, {
                        viewModel.order?.review = OrderReviewModel(
                            orderRating = dialog.orderRatingRb.rating,
                            orderComment = dialog.orderCommentTil.editText?.text?.toString(),
                            deliveryRating = dialog.deliveryRatingRb.rating,
                            deliveryComment = dialog.deliveryCommentTil.editText?.text?.toString()
                        )
                        dialog.dismiss()
                        fragment.binding.invalidateAll()
                    })
                }
            }

            //dialog.findViewById<FrameLayout>(R.id.dialogFl).setOnClickListener { dialog.dismiss() }
        }

        // App Essentials
        /*fun showInternetNoConnectionDialog(activity: KoodActivity, retryRunnable: Runnable){
            if (noConnectionDialog == null) {
                noConnectionDialog = Dialog(activity)
                noConnectionDialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                noConnectionDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                noConnectionDialog!!.setCancelable(false)
                noConnectionDialog!!.setContentView(R.layout.app_dialog)

                noConnectionDialog!!.dialogMessage_tv.visibility = GONE

                noConnectionDialog!!.dialogTitle_tv.text = activity.getString(R.string.dialog_title_no_connection)
                noConnectionDialog!!.dialogPositive_btn.text = activity.getString(R.string.label_retry)
                noConnectionDialog!!.dialogNegative_btn.text = activity.getString(R.string.label_cancel)

                noConnectionDialog!!.dialogNegative_btn.setOnClickListener{
                    activity.hideLoading()
                    noConnectionDialog!!.dismiss() }
            }
            noConnectionDialog!!.dialogPositive_btn.setOnClickListener{
                activity.showLoading()
                noConnectionDialog!!.dismiss()
                retryRunnable.run()
            }

            noConnectionDialog!!.show()

            noConnectionDialog!!.window?.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT)

            activity.hideLoading()
        }

        fun showSomethingWentWrongDialog(activity: AppCompatActivity, isDataCrucial: Boolean){
            val dialogActionRunnable = if (isDataCrucial)
                Runnable { activity.onBackPressed() } else null
            showCustomMessageDialog(activity, R.string.dialog_something_went_wrong,dialogActionRunnable)
        }


        // Customizations
        fun showCustomMessageDialog(activity: AppCompatActivity, customMessageRes: Int, customRunnable: Runnable?){
            val dialog = Dialog(activity)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.app_dialog)

            dialog.dialogMessage_tv.visibility = GONE
            dialog.dialogTitle_tv.text = activity.getString(customMessageRes)
            dialog.dialogPositive_btn.text = activity.getString(R.string.label_ok)

            dialog.dialogPositive_btn.setOnClickListener{
                dialog.dismiss()
                customRunnable?.run()
            }
            dialog.dialogNegative_btn.visibility = GONE

            activity.runOnUiThread{
                dialog.show()
                dialog.window?.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT)
            }
        }

        fun showApiNonFatalDialog(activity: AppCompatActivity, apiResponseCode: Int){
            val dialog = Dialog(activity)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.app_dialog)

            dialog.dialogMessage_tv.visibility = GONE
            dialog.dialogTitle_tv.text = when (apiResponseCode) {
                ApiConstants.MOBILE_EXIST -> activity.getString(R.string.dialog_phone_number_existed)
                else -> activity.getString(R.string.dialog_something_went_wrong)
            }

            dialog.dialogPositive_btn.text = activity.getString(R.string.label_ok)

            dialog.dialogPositive_btn.setOnClickListener{ dialog.dismiss() }
            dialog.dialogNegative_btn.visibility = GONE

            activity.runOnUiThread{
                dialog.show()
                dialog.window?.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT)
            }
        }

        fun showCustomFullDialogDialog(activity: AppCompatActivity, customTitleRes: Int, customMessage: String, customRunnable: Runnable?){
            val dialog = Dialog(activity)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.app_dialog)

            dialog.dialogTitle_tv.text = activity.getString(customTitleRes)
            dialog.dialogMessage_tv.text = customMessage
            dialog.dialogPositive_btn.text = activity.getString(R.string.label_ok)

            dialog.dialogPositive_btn.setOnClickListener{
                dialog.dismiss()
                customRunnable?.run()
            }
            dialog.dialogNegative_btn.visibility = GONE

            dialog.show()

            dialog.window?.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT)
        }*/
    }
}