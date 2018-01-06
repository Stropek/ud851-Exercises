package com.example.android.background.sync

import android.content.Context
import com.example.android.background.utilities.PreferenceUtilities

class ReminderTasks {
    companion object {
        val ACTION_INCREMENT_WATER_COUNT: String = "increment_water_count"

        fun executeTask(context: Context, action: String) {
            if (action == ACTION_INCREMENT_WATER_COUNT) {
                incrementWaterCount(context)
            }
        }

        private fun incrementWaterCount(context: Context) {
            PreferenceUtilities.incrementWaterCount(context)
        }
    }
}
