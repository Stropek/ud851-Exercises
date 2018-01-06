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

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.example.android.background.utilities.PreferenceUtilities.DEFAULT_COUNT
import com.example.android.background.utilities.PreferenceUtilities.KEY_CHARGING_REMINDER_COUNT
import com.example.android.background.utilities.PreferenceUtilities.KEY_WATER_COUNT

/**
 * This class contains utility methods which update water and charging counts in SharedPreferences
 */
object PreferenceUtilities {

    val KEY_WATER_COUNT = "water-count"
    val KEY_CHARGING_REMINDER_COUNT = "charging-reminder-count"

    private val DEFAULT_COUNT = 0

    @Synchronized private fun setWaterCount(context: Context, glassesOfWater: Int) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        editor.putInt(KEY_WATER_COUNT, glassesOfWater)
        editor.apply()
    }

    fun getWaterCount(context: Context): Int {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getInt(KEY_WATER_COUNT, DEFAULT_COUNT)
    }

    @Synchronized
    fun incrementWaterCount(context: Context) {
        var waterCount = PreferenceUtilities.getWaterCount(context)
        PreferenceUtilities.setWaterCount(context, ++waterCount)
    }

    @Synchronized
    fun incrementChargingReminderCount(context: Context) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        var chargingReminders = prefs.getInt(KEY_CHARGING_REMINDER_COUNT, DEFAULT_COUNT)

        val editor = prefs.edit()
        editor.putInt(KEY_CHARGING_REMINDER_COUNT, ++chargingReminders)
        editor.apply()
    }

    fun getChargingReminderCount(context: Context): Int {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getInt(KEY_CHARGING_REMINDER_COUNT, DEFAULT_COUNT)
    }
}