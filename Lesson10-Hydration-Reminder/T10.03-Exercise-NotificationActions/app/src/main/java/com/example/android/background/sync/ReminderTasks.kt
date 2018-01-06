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
import com.example.android.background.utilities.NotificationUtils

import com.example.android.background.utilities.PreferenceUtilities

object ReminderTasks {

    val ACTION_DISMISS_NOTIFICATION = "dismiss-notification"
    val ACTION_INCREMENT_WATER_COUNT = "increment-water-count"

    fun executeTask(context: Context, action: String) {
        when (action) {
            ACTION_INCREMENT_WATER_COUNT -> incrementWaterCount(context)
            ACTION_DISMISS_NOTIFICATION -> NotificationUtils.clearNotifications(context)
        }
    }

    private fun incrementWaterCount(context: Context) {
        PreferenceUtilities.incrementWaterCount(context)
        NotificationUtils.clearNotifications(context)
    }
}