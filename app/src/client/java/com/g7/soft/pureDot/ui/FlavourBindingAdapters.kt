package com.g7.soft.pureDot.ui

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.ext.dpToPx
import com.g7.soft.pureDot.model.ProductVariationModel
import com.g7.soft.pureDot.model.ShippingMethodModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup


@BindingAdapter("bindChipsData", "bindChipsAreChecked")
fun bindColorChipData(
    chipGroup: ChipGroup,
    productVariation: ProductVariationModel?,
    areChecked: Boolean?
) {
    chipGroup.removeAllViewsInLayout()

    productVariation?.values?.forEach { idValueModel ->
        if (productVariation.isColor == true) {
            val chip =
                LayoutInflater.from(chipGroup.context)
                    .inflate(R.layout.item_colored_chip, chipGroup, false) as Chip
            chip.chipBackgroundColor = ColorStateList.valueOf(Color.parseColor(idValueModel.value))
            chip.setOnCheckedChangeListener { _, isChecked ->
                val padding = if (isChecked) 0 else 12.dpToPx()
                chip.textStartPadding = padding.toFloat()
                chip.textEndPadding = padding.toFloat()
            }
            areChecked?.let { chip.isChecked = it }

            // case areChecked == false & first time to load data
            val padding = if (chip.isChecked) 0 else 12.dpToPx()
            chip.textStartPadding = padding.toFloat()
            chip.textEndPadding = padding.toFloat()
            chip.tag = idValueModel.id

            chipGroup.addView(chip)

        } else if (productVariation.isColor == false) {
            val chip =
                LayoutInflater.from(chipGroup.context)
                    .inflate(R.layout.item_black_white_chip, chipGroup, false) as Chip
            chip.text = idValueModel.value
            chip.tag = idValueModel.id
            areChecked?.let { chip.isChecked = it }
            chipGroup.addView(chip)
        }
    }
}

@BindingAdapter("bindShippingMethodsRadioButtons")
fun bindShippingMethodsRadioButtons(
    radioGroup: RadioGroup,
    shippingMethods: List<ShippingMethodModel>?
) {
    radioGroup.removeAllViews()

    for (shippingMethod in shippingMethods ?: listOf()) {
        val context = radioGroup.context

        // add delivery app
        if (shippingMethod == shippingMethods?.getOrNull(0))
            radioGroup.addView((getStyleRadioButton(context)).apply {
                this.text = context.getString(R.string.delivery_app)
            })

        // add api shipping methods
        radioGroup.addView((getStyleRadioButton(context)).apply {
            this.text = shippingMethod.name
            this.tag = shippingMethod.id
        })
    }
}

fun getStyleRadioButton(context: Context) = RadioButton(context).apply {
    setTextColor(ContextCompat.getColor(context, R.color.warm_grey))
    setPadding(0, 12.dpToPx(), 0, 12.dpToPx())
}