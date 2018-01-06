/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.background.utilities

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationCompatBase
import android.support.v4.content.ContextCompat

import com.example.android.background.MainActivity
import com.example.android.background.R
import com.example.android.background.sync.ReminderTasks
import com.example.android.background.sync.WaterReminderIntentService

/**
 * Utility class for creating hydration notifications
 */
object NotificationUtils {

    /*
     * This notification ID can be used to access our notification after we've displayed it. This
     * can be handy when we need to cancel the notification, or perhaps update it. This number is
     * arbitrary and can be set to whatever you like. 1138 is in no way significant.
     */
    private val WATER_REMINDER_NOTIFICATION_ID = 1138
    /**
     * This pending intent id is used to uniquely reference the pending intent
     */
    private val WATER_REMINDER_PENDING_INTENT_ID = 3417

    private val ACTION_IGNORE_PENDING_INTENT_ID = 3418
    private val ACTION_INCREMENT_WATER_COUNT_ID = 3419

    fun clearNotifications(context: Context) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.cancelAll()
    }

    fun remindUserBecauseCharging(context: Context) {
        val notificationBuilder = NotificationCompat.Builder(context)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_drink_notification)
                .setLargeIcon(largeIcon(context))
                .setContentTitle(context.getString(R.string.charging_reminder_notification_title))
                .setContentText(context.getString(R.string.charging_reminder_notification_body))
                .setStyle(NotificationCompat.BigTextStyle().bigText(
                        context.getString(R.string.charging_reminder_notification_body)))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context))
                .addAction(ignoreReminderAction(context))
                .addAction(drinkWaterAction(context))
                .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notificationBuilder.priority = Notification.PRIORITY_HIGH
        }

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(WATER_REMINDER_NOTIFICATION_ID, notificationBuilder.build())
    }

    fun ignoreReminderAction(context: Context): NotificationCompat.Action {
        val intent = Intent(context, WaterReminderIntentService::class.java)
        intent.action = ReminderTasks.ACTION_DISMISS_NOTIFICATION

        val pendingIntent = PendingIntent.getService(context,
                ACTION_IGNORE_PENDING_INTENT_ID,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT)
        return NotificationCompat.Action(R.drawable.notification_icon_background, "Nope", pendingIntent)
    }

    fun drinkWaterAction(context: Context): NotificationCompat.Action {
        val intent = Intent(context, WaterReminderIntentService::class.java)
        intent.action = ReminderTasks.ACTION_INCREMENT_WATER_COUNT

        val pendingIntent = PendingIntent.getService(context,
                ACTION_INCREMENT_WATER_COUNT_ID,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT)
        return NotificationCompat.Action(R.drawable.notification_icon_background, "Yep, yep", pendingIntent)
    }

    private fun contentIntent(context: Context): PendingIntent {
        val startActivityIntent = Intent(context, MainActivity::class.java)

        return PendingIntent.getActivity(
                context,
                WATER_REMINDER_PENDING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT)
    }


    private fun largeIcon(context: Context): Bitmap? {
        val res = context.resources
        return BitmapFactory.decodeResource(res, R.drawable.ic_local_drink_black_24px)
    }
}
