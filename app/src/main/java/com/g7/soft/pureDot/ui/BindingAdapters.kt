package com.g7.soft.pureDot.ui

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.constant.ApiConstant
import com.g7.soft.pureDot.constant.ApiConstant.TransactionType
import com.g7.soft.pureDot.ext.toFormattedDateTime
import com.g7.soft.pureDot.utils.LogEventUtils
import com.g7.soft.pureDot.utils.UiUtils
import com.google.android.material.textfield.TextInputEditText
import com.kofigyan.stateprogressbar.StateProgressBar
import java.io.File
import java.util.*


@BindingAdapter("onEditorActionListener")
fun bindOnEditorActionListener(
    editText: TextInputEditText,
    editorActionListener: TextView.OnEditorActionListener,
) =
    editText.setOnEditorActionListener(editorActionListener)

@BindingAdapter("bindFileName")
fun bindFileName(textView: AppCompatTextView, filePath: String?) {
    filePath ?: LogEventUtils.logApiError("bindFileName: null filePath").run { return }
    textView.text = File(filePath).name
}

@BindingAdapter("bindFileSize")
fun bindFileSize(textView: AppCompatTextView, filePath: String?) {
    filePath ?: LogEventUtils.logApiError("bindFileSize: null filePath").run { return }
    textView.text =
        textView.context.getString(R.string.conc_file_size_in_kb, (File(filePath).length() / 1024))
}


@BindingAdapter("bindApiImageUrl")
fun bindApiImageUrl(imageView: ImageView, url: String?) {
    url ?: LogEventUtils.logApiError("bindApiImageUrl: null url").run { return }

    val dr = ContextCompat.getDrawable(imageView.context, android.R.drawable.ic_dialog_info)
    val bitmap = (dr as BitmapDrawable).bitmap
    val errorDrawable: Drawable =
        BitmapDrawable(imageView.resources, Bitmap.createScaledBitmap(bitmap, 50, 50, true))

    // todo fix error placeHolder appearance
    Glide.with(imageView.context)
        .load(ApiConstant.IMG_BASE_URL + url)
        .apply(
            RequestOptions()
                .placeholder(UiUtils.createThenGetProgressDrawable(imageView.context))
                .centerCrop()
                .error(errorDrawable)
            //.transform(CenterInside(), GranularRoundedCorners(0f, 0f, 32f, 32f))
            //.override(imageView.width, imageView.height)
        )
        .into(imageView)
}

@BindingAdapter(
    "bindPrice",
    "bindCurrency",
    "bindDiscountPercentage",
    "bindPrePriceText",
    requireAll = false
)
fun bindPriceWithCurrency(
    textView: TextView,
    price: Double?,
    currency: String?,
    discountPercentage: Float? = null,
    preText: String? = null
) {
    price ?: LogEventUtils.logApiError("bindPriceWithCurrency: null price").run { return }
    currency ?: LogEventUtils.logApiError("bindPriceWithCurrency: null currency")
        .run { return }


    if (preText != null)
        textView.text = textView.context.getString(
            R.string.conc_two_strings, preText, textView.context.getString(
                R.string.format_price_with_currency,
                price.times(discountPercentage ?: 1f),
                currency
            )
        )
    else
        textView.text =
            textView.context.getString(
                R.string.format_price_with_currency,
                price.times(discountPercentage ?: 1f),
                currency
            )

}

@BindingAdapter("bindDate", "bindDateFormat")
fun bindDate(textView: AppCompatTextView, date: Long?, dateFormat: String?) {
    date ?: LogEventUtils.logApiError("bindDate: null date").run { return }
    dateFormat ?: LogEventUtils.logApiError("bindDate: null dateFormat").run { return }

    textView.text = date.toFormattedDateTime(dateFormat)
}


@BindingAdapter("app:spb_currentStateNumber")
fun setCurrentStateNumber(spb: StateProgressBar, newValue: StateProgressBar.StateNumber?) {
    // Important to break potential infinite loops.
    if (getCurrentStateNumber(spb) != newValue) {
        spb.setCurrentStateNumber(newValue)
    }
}

@InverseBindingAdapter(attribute = "app:spb_currentStateNumber")
fun getCurrentStateNumber(spb: StateProgressBar): StateProgressBar.StateNumber? =
    when (spb.currentStateNumber) {
        1 -> StateProgressBar.StateNumber.ONE
        2 -> StateProgressBar.StateNumber.TWO
        3 -> StateProgressBar.StateNumber.THREE
        else -> null
    }

@BindingAdapter("app:spb_currentStateNumberAttrChanged")
fun setCurrentStateNumberListeners(
    spb: StateProgressBar,
    attrChange: InverseBindingListener
) = spb.setOnStateItemClickListener { _, _, _, _ -> attrChange.onChange() }


@BindingAdapter("bindSrcCompat")
fun bindSrcCompat(imageView: ImageView, drawableResId: Int?) {
    drawableResId ?: LogEventUtils.logApiError("bindSrcCompat: null drawableResId").run { return }
    imageView.setImageResource(drawableResId)
}

@BindingAdapter("bindSrcCompat")
fun bindSrcCompat(imageView: ImageView, drawable: Drawable?) {
    drawable ?: LogEventUtils.logApiError("bindSrcCompat: null drawable").run { return }
    imageView.setImageDrawable(drawable)
}

@BindingAdapter("bindOrderStatus")
fun bindOrderStatus(textView: TextView, orderStatus: Int?) {
    orderStatus ?: LogEventUtils.logApiError("bindOrderStatus: null orderStatus").run { return }

    val context = textView.context
    when (ApiConstant.OrderStatus.fromInt(orderStatus)) {
        ApiConstant.OrderStatus.NEW -> {
            textView.text = context.getString(R.string.oder_placed)
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.ic_order_placed_small,
                0,
                0,
                0
            )
            textView.setBackgroundColor(ContextCompat.getColor(context, R.color.bluish))
        }
        ApiConstant.OrderStatus.CONFIRMED -> {
            textView.text = context.getString(R.string.confirmed)
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.ic_order_status_accepted,
                0,
                0,
                0
            )
            textView.setBackgroundColor(ContextCompat.getColor(context, R.color.sick_green))
        }
        ApiConstant.OrderStatus.BEING_SHIPPED, ApiConstant.OrderStatus.ORDER_IS_OUT -> {
            textView.text = context.getString(R.string.ready_for_delivery)
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.ic_order_shipping_small,
                0,
                0,
                0
            )
            textView.setBackgroundColor(ContextCompat.getColor(context, R.color.pumpkin_orange))
        }
        ApiConstant.OrderStatus.PICKED -> {
            textView.text = context.getString(R.string.out_for_delivery)
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.ic_out_of_delivery_small,
                0,
                0,
                0
            )
            textView.setBackgroundColor(ContextCompat.getColor(context, R.color.warm_purple))
        }
        ApiConstant.OrderStatus.DELIVERED -> {
            textView.text = context.getString(R.string.delivered)
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.ic_deliverd_small,
                0,
                0,
                0
            )
            textView.setBackgroundColor(ContextCompat.getColor(context, R.color.sick_green))
        }
        ApiConstant.OrderStatus.CANCELED -> {
            textView.text = context.getString(R.string.canceled)
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.ic_canceled_small,
                0,
                0,
                0
            )
            textView.setBackgroundColor(ContextCompat.getColor(context, R.color.driverRed))
        }
    }
}

@BindingAdapter("bindOrderDeliveryStatus")
fun bindOrderDeliveryStatus(textView: TextView, orderDeliveryStatus: Int?) {
    orderDeliveryStatus
        ?: LogEventUtils.logApiError("bindOrderDeliveryStatus: null orderDeliveryStatus")
            .run { return }

    val context = textView.context
    when (ApiConstant.OrderDeliveryStatus.fromInt(orderDeliveryStatus)) {
        ApiConstant.OrderDeliveryStatus.NEW -> { /*hidden status*/
        }
        ApiConstant.OrderDeliveryStatus.ACCEPTED -> {
            textView.text = context.getString(R.string.accepted)
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.ic_order_status_accepted,
                0,
                0,
                0
            )
            textView.setBackgroundColor(ContextCompat.getColor(context, R.color.sick_green))
        }
        ApiConstant.OrderDeliveryStatus.STARTED -> {
            textView.text = context.getString(R.string.started)
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.ic_order_status_accepted,
                0,
                0,
                0
            )
            textView.setBackgroundColor(ContextCompat.getColor(context, R.color.sick_green))
        }
        ApiConstant.OrderDeliveryStatus.PICKED -> {
            textView.text = context.getString(R.string.picked_up_the_item)
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.ic_order_status_picked,
                0,
                0,
                0
            )
            textView.setBackgroundColor(ContextCompat.getColor(context, R.color.lavender_pink))
        }
        ApiConstant.OrderDeliveryStatus.ARRIVED -> {
            textView.text = context.getString(R.string.arrived_to_customer)
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.ic_out_of_delivery_small,
                0,
                0,
                0
            )
            textView.setBackgroundColor(ContextCompat.getColor(context, R.color.dark_sky_blue))
        }
        ApiConstant.OrderDeliveryStatus.DELIVERED -> {
            textView.text = context.getString(R.string.delivered_to_customer)
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.ic_deliverd_small,
                0,
                0,
                0
            )
            textView.setBackgroundColor(ContextCompat.getColor(context, R.color.warm_purple))
        }
        ApiConstant.OrderDeliveryStatus.REJECTED -> {
            textView.text = context.getString(R.string.rejected)
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.ic_canceled_small,
                0,
                0,
                0
            )
            textView.setBackgroundColor(ContextCompat.getColor(context, R.color.driverRed))
        }
        ApiConstant.OrderDeliveryStatus.CANCELED -> {
            textView.text = context.getString(R.string.canceled)
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.ic_canceled_small,
                0,
                0,
                0
            )
            textView.setBackgroundColor(ContextCompat.getColor(context, R.color.driverRed))
        }
    }
}

@BindingAdapter("bindOrderDeliveryActionText")
fun bindOrderDeliveryActionText(button: Button, orderDeliveryStatus: Int?) {
    orderDeliveryStatus
        ?: LogEventUtils.logApiError("bindOrderDeliveryActionText: null orderDeliveryStatus")
            .run { return }

    val textResId = when (ApiConstant.OrderDeliveryStatus.fromInt(orderDeliveryStatus)) {
        ApiConstant.OrderDeliveryStatus.NEW -> R.string.accept
        ApiConstant.OrderDeliveryStatus.ACCEPTED -> R.string.start_head_to_the_shop
        ApiConstant.OrderDeliveryStatus.STARTED -> R.string.arrived_to_shop
        ApiConstant.OrderDeliveryStatus.PICKED -> R.string.picked_up_the_item
        ApiConstant.OrderDeliveryStatus.ARRIVED -> R.string.delivered_to_customer
        ApiConstant.OrderDeliveryStatus.DELIVERED -> R.string.done
        else -> null
    }

    if (textResId != null)
        button.text = button.context.getString(textResId)
}


@BindingAdapter("layoutMarginStart")
fun setLayoutMarginStart(view: View, dimen: Float) {
    val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
    layoutParams.marginStart = dimen.toInt()
    view.layoutParams = layoutParams
}

@BindingAdapter("layoutMarginEnd")
fun setLayoutMarginEnd(view: View, dimen: Float) {
    val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
    layoutParams.marginEnd = dimen.toInt()
    view.layoutParams = layoutParams
}

@BindingAdapter("bindNotificationStatus")
fun bindNotificationStatus(imageView: ImageView, isRead: Boolean?) {
    imageView.setColorFilter(
        ContextCompat.getColor(
            imageView.context,
            if (isRead == true) R.color.sick_green else R.color.warm_grey
        ), android.graphics.PorterDuff.Mode.SRC
    )
}

/*@BindingAdapter("bindTransactionMessage")
fun bindTransactionMessage(textView: TextView, type: Int?) {
    type ?: LogEventUtils.logApiError("bindTransactionMessage: null type").run { return }

    val textResId = when (ApiConstant.TransactionType.fromInt(type)) {
        ApiConstant.TransactionType.TO_YOUR_ACCOUNT -> R.string.msg_to_your_account
        ApiConstant.TransactionType.WITHDRAW -> R.string.msg_withdraw
        ApiConstant.TransactionType.DEPOSIT -> R.string.msg_deposit
        ApiConstant.TransactionType.FROM_YOUR_ACCOUNT -> R.string.msg_from_your_account
        ApiConstant.TransactionType.POINTS -> R.string.msg_points_replaced
        null -> null
    }

    if (textResId != null) textView.text = textView.context.getString(textResId)
}

@BindingAdapter("bindTransactionTitle")
fun bindTransactionTitle(textView: TextView, type: Int?) {
    type ?: LogEventUtils.logApiError("bindTransactionTitle: null type").run { return }

    val textResId = when (ApiConstant.TransactionType.fromInt(type)) {
        ApiConstant.TransactionType.FROM_YOUR_ACCOUNT, ApiConstant.TransactionType.TO_YOUR_ACCOUNT -> R.string.transfer
        ApiConstant.TransactionType.WITHDRAW -> R.string.withdraw
        ApiConstant.TransactionType.DEPOSIT -> R.string.deposit
        ApiConstant.TransactionType.POINTS -> R.string.points
        null -> null
    }

    if (textResId != null) textView.text = textView.context.getString(textResId)
}*/

@BindingAdapter("bindTransactionTypeImage")
fun bindTransactionTypeImage(imageView: ImageView, type: Int?) {
    type ?: LogEventUtils.logApiError("bindTransactionTypeImage: null type").run { return }

    val imageResId = when (TransactionType.fromInt(type)) {
        TransactionType.RETURN_ORDER, TransactionType.PAY_FOR_ORDER -> R.drawable.ic_transaction_type_transfer
        TransactionType.REDEEM, TransactionType.DEPOSIT, TransactionType.RETURN_PRODUCT, TransactionType.DELIVERY_INCOME, TransactionType.SALES -> R.drawable.ic_transaction_type_deposit
        TransactionType.TRANSFER, TransactionType.REFUND_MONEY, TransactionType.REFUND -> R.drawable.ic_transaction_type_withdraw
        //TransactionType.POINTS -> R.drawable.ic_transaction_type_points
        null -> null
    }

    if (imageResId != null) imageView.setImageResource(imageResId)
}

@BindingAdapter("bindComplainStatus")
fun bindComplainStatus(textView: TextView, status: Int?) {
    status ?: LogEventUtils.logApiError("bindComplainStatus: null status").run { return }

    val context = textView.context
    when (ApiConstant.ComplainStatus.fromInt(status)) {
        ApiConstant.ComplainStatus.NEW -> {
            textView.text = context.getString(R.string.label_new)
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.ic_resource_new,
                0,
                0,
                0
            )
            textView.setBackgroundColor(ContextCompat.getColor(context, R.color.sick_green))
        }
        ApiConstant.ComplainStatus.IN_PROGRESS -> {
            textView.text = context.getString(R.string.in_progress)
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.ic_in_progress,
                0,
                0,
                0
            )
            textView.setBackgroundColor(ContextCompat.getColor(context, R.color.pumpkin_orange))
        }
        ApiConstant.ComplainStatus.RESOLVED -> {
            textView.text = context.getString(R.string.resolved)
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.ic_resolved,
                0,
                0,
                0
            )
            textView.setBackgroundColor(ContextCompat.getColor(context, R.color.bluish))
        }
    }
}


@BindingAdapter("bindDayName")
fun bindDayName(textView: TextView, dayIndex: Int?) {
    dayIndex ?: LogEventUtils.logApiError("bindDayName: null dayIndex").run { return }

    textView.text = getFullDayName(dayIndex)
}

fun getFullDayName(day: Int): String? {
    val c: Calendar = Calendar.getInstance()
    // date doesn't matter - it has to be a Monday
    // I new that first August 2011 is one ;-)
    c.set(2021, 4, 1, 0, 0, 0)
    c.add(Calendar.DAY_OF_MONTH, day)
    return java.lang.String.format("%tA", c)
}