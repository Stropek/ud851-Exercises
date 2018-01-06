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
package com.example.android.background.sync

import android.content.Context
import com.firebase.jobdispatcher.*
import java.util.concurrent.TimeUnit


class ReminderUtilities {
    companion object {
        val REMINDER_INTERVAL_SECONDS: Int = 10
        val SYNC_FLEXTIME_SECONDS = REMINDER_INTERVAL_SECONDS

        val REMINDER_JOB_TAG = "hydration-reminder-tag"
        var sInitialized = false

        @Synchronized
        fun scheduleChargingReminder(context: Context) {
            if (sInitialized)
                return

            val driver = GooglePlayDriver(context)
            val dispatcher = FirebaseJobDispatcher(driver)
            val job = dispatcher.newJobBuilder()
                    .setService(WaterReminderFirebaseJobService::class.java)
                    .setTag(REMINDER_JOB_TAG)
                    .setConstraints(Constraint.DEVICE_CHARGING)
                    .setLifetime(Lifetime.FOREVER)
                    .setRecurring(true)
                    .setTrigger(Trigger.executionWindow(REMINDER_INTERVAL_SECONDS, REMINDER_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                    .setReplaceCurrent(true)
                    .build()
            dispatcher.schedule(job)

            sInitialized = true
        }
    }
}
