package com.g7.soft.pureDot

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.mapbox.navigation.base.trip.model.TripNotificationState
import com.mapbox.navigation.base.trip.notification.TripNotification
import com.mapbox.navigation.utils.internal.NAVIGATION_NOTIFICATION_CHANNEL


class MapboxTripNotification(context: Context) : TripNotification { // TODO Eng. Omnia
    private val customNotification: Notification
    private val customNotificationBuilder: NotificationCompat.Builder
    private val notificationManager: NotificationManager
    private val numberOfUpdates = 0

    /*fun updateNotification(routeProgress: RouteProgress) {
        // Update the builder with a new number of updates
        customNotificationBuilder.setContentText("Diatance you traveled : " + routeProgress.distanceTraveled())

        // Notify the notification manager
        notificationManager.notify(notificationId, customNotificationBuilder.build())
    }*/

    companion object {
        val notificationId = 91234821
    }

    init {
        // Get the notification manager to update your notification
        notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Store the builder to update later
        customNotificationBuilder =
            NotificationCompat.Builder(context, NAVIGATION_NOTIFICATION_CHANNEL)
                .setSmallIcon(R.drawable.ic_stat_ic_notification)
                .setContentTitle("App name")
                .setContentText("Happy journey")

        // Build the notification
        customNotification = customNotificationBuilder.build()
    }

    override fun getNotification(): Notification = customNotification

    override fun getNotificationId(): Int = notificationId

    override fun onTripSessionStarted() {
        //TODO("Not yet implemented")
    }

    override fun onTripSessionStopped() {
        //TODO("Not yet implemented")
    }

    override fun updateNotification(state: TripNotificationState) {
        //TODO("Not yet implemented")
    }
}