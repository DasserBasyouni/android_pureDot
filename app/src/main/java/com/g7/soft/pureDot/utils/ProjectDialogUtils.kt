package com.g7.soft.pureDot.utils

import android.app.Activity
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
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.g7.soft.pureDot.Application
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.constant.ProjectConstant
import com.g7.soft.pureDot.ext.dpToPx
import com.g7.soft.pureDot.ext.observeApiResponse
import com.g7.soft.pureDot.model.OrderReviewModel
import com.g7.soft.pureDot.model.ReviewModel
import com.g7.soft.pureDot.repo.UserRepository
import com.g7.soft.pureDot.ui.bindPriceWithCurrency
import com.g7.soft.pureDot.ui.screen.complain.ComplainFragment
import com.google.android.material.textfield.TextInputLayout
import com.zeugmasolutions.localehelper.currentLocale
import kotlinx.android.synthetic.main.dialog_complaint_service_rating.*
import kotlinx.android.synthetic.main.dialog_rating.disableClicksIv
import kotlinx.android.synthetic.main.dialog_rating.positiveBtn
import kotlinx.coroutines.launch


// todo a cleaner solution?
class ProjectDialogUtils : FlavourProjectDialogUtils() {

    companion object {

        //private var driverRegisterConfirmation: Dialog? = null
        //private var noConnectionDialog: Dialog? = null
        private var loadingDialog: Dialog? = null

        fun onExitApp(activity: Activity, negativeRunnable: Runnable) = showAskingDialog(
            activity,
            messageResId = R.string.msg_sure_exit_app,
            iconResId = R.drawable.ic_popup_logout,
            positiveRunnable = activity::finish,
            negativeRunnable = negativeRunnable
        )


        fun showLoading(context: Context) {
            val isFirstInit = loadingDialog == null

            if (isFirstInit) {
                loadingDialog = Dialog(context)
                loadingDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }

            val progressBar = ProgressBar(context, null, android.R.attr.progressBarStyleLarge)
            progressBar.indeterminateDrawable.colorFilter =
                BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                    ContextCompat.getColor(
                        context,
                        if (Application.isClientFlavour) R.color.bluish else R.color.driverRed
                    ),
                    BlendModeCompat.SRC_ATOP
                )

            if (isFirstInit) {
                loadingDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
                loadingDialog?.setCancelable(false)
                loadingDialog?.setContentView(progressBar)
            }

            loadingDialog?.show()

            if (isFirstInit) {
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
        }

        fun hideLoading(): Unit = if (loadingDialog?.isShowing == true) {
            loadingDialog?.dismiss()
            loadingDialog = null
        } else Unit

        fun showSimpleMessage(
            context: Context,
            messageResId: Int? = null,
            message: String? = null,
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
            dialog.findViewById<TextView>(R.id.messageTv).text =
                if (messageResId != null) context.getString(messageResId) else message

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
            titleResId: Int? = null,
            messageResId: Int,
            positiveRunnable: Runnable,
            negativeRunnable: Runnable? = null
        ) {
            val dialog = Dialog(context)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.project_asking_dialog)

            dialog.findViewById<ImageView>(R.id.iconIv).setImageResource(iconResId)
            if (titleResId != null)
                dialog.findViewById<TextView>(R.id.titleTv).text = context.getString(titleResId)
            dialog.findViewById<TextView>(R.id.messageTv).text = context.getString(messageResId)

            dialog.findViewById<Button>(R.id.positiveBtn).setOnClickListener {
                positiveRunnable.run()
                dialog.dismiss()
            }

            dialog.findViewById<Button>(R.id.negativeBtn).setOnClickListener {
                negativeRunnable?.run()
                dialog.dismiss()
            }

            dialog.show()

            dialog.window?.setLayout(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
            )
        }

        fun showOrderRating(
            fragment: Fragment,
            currencySymbol: String,
            userReview: OrderReviewModel?,
            orderNumber: Int?,
            totalOrderCost: Double?,
            onClickPositiveAction: () -> Unit,
        ): Dialog? {
            return FlavourProjectDialogUtils.showOrderRating(
                fragment,
                userReview = userReview,
                orderNumber = orderNumber,
                currencySymbol = currencySymbol,
                totalOrderCost = totalOrderCost,
                onClickPositiveAction = onClickPositiveAction
            )
        }

        fun showComplaintServiceRating(
            fragment: ComplainFragment,
        ) {
            val dialog = Dialog(fragment.requireContext())
            val viewModel = fragment.viewModel

            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            dialog.setCancelable(true)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_complaint_service_rating)


            val review = viewModel.complain?.review
            if (review != null) {
                dialog.disableClicksIv.visibility = View.VISIBLE
                dialog.positiveBtn.visibility = View.GONE

                dialog.ratingRb.rating = review.rating?.toFloat() ?: 0f
                dialog.commentTil.editText?.setText(review.review ?: "")
            }

            dialog.show()

            dialog.window?.setLayout(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            )

            dialog.findViewById<TextView>(R.id.positiveBtn).apply {
                this.text = context.getString(R.string.submit_review)
                this.setOnClickListener {
                    fragment.lifecycleScope.launch {
                        val tokenId =
                            UserRepository("").getTokenId(fragment.requireContext())

                        viewModel.rateComplainService(
                            langTag = fragment.requireActivity().currentLocale.toLanguageTag(),
                            tokenId = tokenId,
                            message = dialog.commentTil.editText?.text?.toString(),
                            ratting = dialog.ratingRb.rating,
                        ).observeApiResponse(fragment, {
                            viewModel.complain?.review = ReviewModel(
                                rating = dialog.ratingRb.rating.toInt(),
                                review = dialog.commentTil.editText?.text?.toString(),
                                id = it,
                            )
                            dialog.dismiss()
                            fragment.binding.invalidateAll()
                        }, validationObserve = {
                            dialog.commentTil.error =
                                if (it == ProjectConstant.Companion.ValidationError.EMPTY_COMMENT)
                                    dialog.context.getString(R.string.error_comment) else null
                        })
                    }
                }
            }

            //dialog.findViewById<FrameLayout>(R.id.dialogFl).setOnClickListener { dialog.dismiss() }
        }

        fun showStcPaymentConfirmation(
            context: Context,
            positiveCallback: (verificationCode: String?) -> Unit
        ) {
            val dialog = Dialog(context)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.dialog_stc_payment)

            dialog.findViewById<Button>(R.id.positiveBtn).setOnClickListener {
                val stcOtpTil = dialog.findViewById<TextInputLayout>(R.id.verificationCodeTIl)
                val stcOtpCode = stcOtpTil?.editText?.text?.toString()

                // validate inputs
                ValidationUtils()
                    .setStcOtpCode(stcOtpCode)
                    .getError()?.let {
                        stcOtpTil.error =
                            if (it == ProjectConstant.Companion.ValidationError.EMPTY_STC_OTP_CODE)
                                context.getString(R.string.error_empty_stc_otp_code) else null

                        return@setOnClickListener
                    }

                positiveCallback.invoke(stcOtpCode)
                dialog.dismiss()
            }

            dialog.findViewById<Button>(R.id.negativeBtn).setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()

            dialog.window?.setLayout(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
            )
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