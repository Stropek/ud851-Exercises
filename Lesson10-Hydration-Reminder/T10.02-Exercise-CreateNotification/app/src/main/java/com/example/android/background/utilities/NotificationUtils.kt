package com.example.android.background.utilities

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import com.example.android.background.MainActivity
import com.example.android.background.R

/**
 * Utility class for creating hydration notifications
 */
class NotificationUtils {
    companion object {
        val CONTENT_INTENT_ID = 1
        val NOTIFICATION_ID = 11
        val NOTIFICATION_CHANNEL_ID = "reminder-notification-channel"

        fun contentIntent(context: Context): PendingIntent {
            val mainActivityIntent = Intent(context, MainActivity::class.java)
            return PendingIntent.getActivity(context, CONTENT_INTENT_ID, mainActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        fun largeIcon(context: Context): Bitmap? {
            return BitmapFactory.decodeResource(context.resources, R.drawable.ic_local_drink_black_24px)
        }

        fun remindUserBecauseCharging(context: Context) {
            val notificationManger = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID,
                        context.getString(R.string.main_notification_channel_name),
                        NotificationManager.IMPORTANCE_HIGH)
                notificationManger.createNotificationChannel(channel)
            }

            val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                    .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                    .setSmallIcon(R.drawable.ic_drink_notification)
                    .setLargeIcon(largeIcon(context))
                    .setContentTitle(context.getString(R.string.charging_reminder_notification_title))
                    .setContentText(context.getString(R.string.charging_reminder_notification_body))
                    .setStyle(NotificationCompat.BigTextStyle().bigText(context.getString(R.string.charging_reminder_notification_body)))
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setContentIntent(contentIntent(context))
                    .setAutoCancel(true)

            notificationManger.notify(NOTIFICATION_ID, builder.build())
        }
    }
}
