package com.g7.soft.pureDot.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.text.TextUtils
import androidx.core.app.NotificationCompat
import androidx.core.os.bundleOf
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.constant.ProjectConstant
import com.g7.soft.pureDot.ui.screen.MainActivity
import com.g7.soft.pureDot.ui.screen.myOrders.MyOrdersFragment
import com.g7.soft.pureDot.ui.screen.order.OrderFragment
import com.g7.soft.pureDot.ui.screen.product.ProductFragment
import com.g7.soft.pureDot.ui.screen.store.StoreFragment
import com.g7.soft.pureDot.ui.screen.trackOrder.TrackOrderFragment


class ProjectFirebaseMessagingServiceFlavour {
    fun onMessageReceived(context: ProjectFirebaseMessagingService, data: Map<String, String>) {
        val storeId = data["storeId"]
        val productId = data["productId"]
        val orderId = data["orderId"]
        val orderNumber = data["orderNumber"]

        if (!TextUtils.isEmpty(data["click_action"])) {
            val title = data["title"]!!
            val body = data["body"]!!

            refreshResponsibleScreen(storeId, productId, orderId)
            sendNotification(
                context = context,
                title = title,
                messageBody = body,
                orderId = orderId,
                productId = productId,
                storeId = storeId,
                orderNumber = orderNumber
            )
        } else
            refreshResponsibleScreen(storeId, productId, orderId)
    }


    private fun refreshResponsibleScreen(
        storeId: String?,
        productId: String?,
        orderId: String?
    ) {
        when {
            storeId != null && StoreFragment.isRunning -> StoreFragment.refreshData?.invoke(storeId)
            productId != null && ProductFragment.isRunning ->
                ProductFragment.refreshData?.invoke(productId)
            orderId != null && OrderFragment.isRunning -> OrderFragment.refreshData?.invoke(orderId)
            orderId != null && MyOrdersFragment.isRunning -> MyOrdersFragment.refreshData?.invoke()
            orderId != null && TrackOrderFragment.isRunning -> TrackOrderFragment.refreshData?.invoke(orderId)
        }
    }

    private fun sendNotification(
        context: Context,
        title: String,
        messageBody: String,
        orderId: String?,
        productId: String?,
        storeId: String?,
        orderNumber: String?
    ) {
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra(
            ProjectConstant.BUNDLE_NOTIFICATION, bundleOf(
                "orderId" to orderId,
                "productId" to productId,
                "storeId" to storeId,
                "orderNumber" to orderNumber
            )
        )
        val pendingIntent = PendingIntent.getActivity(
            context,
            0 /* Request code */,
            intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val channelId = context.getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_stat_ic_notification)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }
}