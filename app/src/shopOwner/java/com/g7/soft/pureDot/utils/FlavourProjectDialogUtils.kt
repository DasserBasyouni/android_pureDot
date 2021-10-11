package com.g7.soft.pureDot.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.constant.ProjectConstant.Companion.ValidationError
import com.g7.soft.pureDot.model.OrderReviewModel
import com.google.android.material.textfield.TextInputLayout


// todo a cleaner solution?
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


        fun showPackageSettings(
            context: Context,
            positiveCallback: (length: String?, width: String?, height: String?, weight: String?, description: String?) -> Unit
        ) {
            val dialog = Dialog(context)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.dialog_package_settings)

            dialog.findViewById<Button>(R.id.positiveBtn).setOnClickListener {
                val lengthTil = dialog.findViewById<TextInputLayout>(R.id.lengthTil)
                val length = lengthTil?.editText?.text?.toString()

                val widthTil = dialog.findViewById<TextInputLayout>(R.id.widthTil)
                val width = widthTil?.editText?.text?.toString()

                val heightTil = dialog.findViewById<TextInputLayout>(R.id.heightTil)
                val height = heightTil?.editText?.text?.toString()

                val weightTil = dialog.findViewById<TextInputLayout>(R.id.weightTil)
                val weight = weightTil?.editText?.text?.toString()

                val descriptionTil = dialog.findViewById<TextInputLayout>(R.id.descriptionTil)
                val description = descriptionTil?.editText?.text?.toString()

                // validate inputs
                ValidationUtils()
                    .setLength(length)
                    .setWidth(width)
                    .setHeight(height)
                    .setWeight(weight)
                    .setDescription(description)
                    .getError()?.let {
                        lengthTil.error =
                            if (it == ValidationError.EMPTY_LENGTH)
                                context.getString(R.string.error_empty_length) else null

                        widthTil.error =
                            if (it == ValidationError.EMPTY_WIDTH)
                                context.getString(R.string.error_empty_width) else null

                        heightTil.error =
                            if (it == ValidationError.EMPTY_HEIGHT)
                                context.getString(R.string.error_empty_height) else null

                        weightTil.error =
                            if (it == ValidationError.EMPTY_WEIGHT)
                                context.getString(R.string.error_empty_weight) else null

                        descriptionTil.error =
                            if (it == ValidationError.EMPTY_DESCRIPTION)
                                context.getString(R.string.error_empty_description) else null

                        return@setOnClickListener
                    }

                positiveCallback.invoke(length, width, height, weight, description)
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
    }
}