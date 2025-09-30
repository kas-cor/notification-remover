package com.example.notificationremover.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.notificationremover.R

/**
 * Utility class for notification-related operations
 */
object NotificationUtils {

    private const val CHANNEL_ID = "notification_remover_channel"
    private const val NOTIFICATION_ID = 1

    /**
     * Creates the notification channel for Android 8.0+
     */
    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification Remover Service"
            val descriptionText = "Shows status of the notification remover service"
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            
            // Register the channel with the system
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Creates a notification for the foreground service
     */
    fun createServiceNotification(context: Context): android.app.Notification {
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Notification Remover Active")
            .setContentText("Monitoring notifications")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }
}
