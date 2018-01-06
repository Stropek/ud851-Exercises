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
package com.example.android.background

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import com.example.android.background.sync.ReminderTasks
import com.example.android.background.sync.ReminderUtilities
import com.example.android.background.sync.WaterReminderIntentService
import com.example.android.background.utilities.PreferenceUtilities

class MainActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    private var mWaterCountDisplay: TextView? = null
    private var mChargingCountDisplay: TextView? = null
    private var mChargingImageView: ImageView? = null

    private var mToast: Toast? = null

    private lateinit var mChargingReceiver: ChargingBroadcastReceiver
    private lateinit var mChargingIntentFilter: IntentFilter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /** Get the views  */
        mWaterCountDisplay = findViewById(R.id.tv_water_count) as TextView
        mChargingCountDisplay = findViewById(R.id.tv_charging_reminder_count) as TextView
        mChargingImageView = findViewById(R.id.iv_power_increment) as ImageView

        /** Set the original values in the UI  */
        updateWaterCount()
        updateChargingReminderCount()
        ReminderUtilities.scheduleChargingReminder(this)

        /** Setup the shared preference listener  */
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        prefs.registerOnSharedPreferenceChangeListener(this)

        /*
         * Setup and register the broadcast receiver
         */
        mChargingIntentFilter = IntentFilter()
        mChargingReceiver = ChargingBroadcastReceiver()
        mChargingIntentFilter.addAction(Intent.ACTION_POWER_CONNECTED)
        mChargingIntentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED)
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(mChargingReceiver, mChargingIntentFilter)

        var isCharging = false

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val batteryManager = getSystemService(BATTERY_SERVICE) as BatteryManager
            isCharging = batteryManager.isCharging
        } else {
            val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            val intent = registerReceiver(null, intentFilter)
            val status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, 0)

            isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL
        }

        showCharging(isCharging)
        registerReceiver(mChargingReceiver, mChargingIntentFilter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(mChargingReceiver)
    }


    /**
     * Updates the TextView to display the new water count from SharedPreferences
     */
    private fun updateWaterCount() {
        val waterCount = PreferenceUtilities.getWaterCount(this)
        mWaterCountDisplay!!.text = waterCount.toString() + ""
    }

    /**
     * Updates the TextView to display the new charging reminder count from SharedPreferences
     */
    private fun updateChargingReminderCount() {
        val chargingReminders = PreferenceUtilities.getChargingReminderCount(this)
        val formattedChargingReminders = resources.getQuantityString(
                R.plurals.charge_notification_count, chargingReminders, chargingReminders)
        mChargingCountDisplay!!.text = formattedChargingReminders

    }

    /**
     * Adds one to the water count and shows a toast
     */
    fun incrementWater(view: View) {
        if (mToast != null) mToast!!.cancel()
        mToast = Toast.makeText(this, R.string.water_chug_toast, Toast.LENGTH_SHORT)
        mToast!!.show()

        val incrementWaterCountIntent = Intent(this, WaterReminderIntentService::class.java)
        incrementWaterCountIntent.action = ReminderTasks.ACTION_INCREMENT_WATER_COUNT
        startService(incrementWaterCountIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        /** Cleanup the shared preference listener  */
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        prefs.unregisterOnSharedPreferenceChangeListener(this)
    }

    /**
     * This is a listener that will update the UI when the water count or charging reminder counts
     * change
     */
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (PreferenceUtilities.KEY_WATER_COUNT == key) {
            updateWaterCount()
        } else if (PreferenceUtilities.KEY_CHARGING_REMINDER_COUNT == key) {
            updateChargingReminderCount()
        }
    }

    private fun showCharging(isCharging: Boolean) {
        if (isCharging) {
            mChargingImageView!!.setImageResource(R.drawable.ic_power_pink_80px)
        } else {
            mChargingImageView!!.setImageResource(R.drawable.ic_power_grey_80px)
        }
    }

    private inner class ChargingBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            val isCharging = action == Intent.ACTION_POWER_CONNECTED

            showCharging(isCharging)
        }
    }
}