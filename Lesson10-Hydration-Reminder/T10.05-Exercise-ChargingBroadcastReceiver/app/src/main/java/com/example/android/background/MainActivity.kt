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

import android.content.*
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

    private lateinit var mBbroadcastReceiver: BroadcastReceiver
    private lateinit var mIntentFilter: IntentFilter

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

        mBbroadcastReceiver = ChargingBroadcastReceiver()
        mIntentFilter = IntentFilter()
        mIntentFilter.addAction(Intent.ACTION_POWER_CONNECTED)
        mIntentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED)
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(mBbroadcastReceiver, mIntentFilter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(mBbroadcastReceiver)
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

    fun showCharging(show: Boolean) {
        val image = getDrawable(if (show) R.drawable.ic_power_pink_80px else R.drawable.ic_power_grey_80px)
        mChargingImageView?.setImageDrawable(image)
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

    inner class ChargingBroadcastReceiver: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            showCharging(intent.action == Intent.ACTION_POWER_CONNECTED)
        }
    }
}