package android.example.com.visualizerpreferences

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

import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.support.v7.preference.CheckBoxPreference
import android.support.v7.preference.EditTextPreference
import android.support.v7.preference.ListPreference
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import android.support.v7.preference.PreferenceScreen
import android.view.View
import android.widget.Toast

class SettingsFragment : PreferenceFragmentCompat(), OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener {
    override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
        val error: Toast = Toast.makeText(context, "Please select a number between 0.1 and 3.0", Toast.LENGTH_LONG)

        val sizeKey = getString(R.string.pref_size_key)
        if (preference?.key == sizeKey) {
            try {
                val fValue = newValue?.toString()?.toFloatOrNull()
                if (fValue == null || fValue <= 0.0 || fValue > 3.0) {
                    error.show()
                    return false
                }
            } catch (ex: Exception) {
                error.show()
                return false
            }
        }
        return true
    }

    override fun onCreatePreferences(bundle: Bundle?, s: String?) {

        // Add visualizer preferences, defined in the XML file in res->xml->pref_visualizer
        addPreferencesFromResource(R.xml.pref_visualizer)

        val sharedPreferences = preferenceScreen.sharedPreferences
        val prefScreen = preferenceScreen
        val count = prefScreen.preferenceCount

        // Go through all of the preferences, and set up their preference summary.
        for (i in 0 until count) {
            val p = prefScreen.getPreference(i)
            // You don't need to set up preference summaries for checkbox preferences because
            // they are already set up in xml using summaryOff and summary On
            if (p !is CheckBoxPreference) {
                val value = sharedPreferences.getString(p.key, "")
                setPreferenceSummary(p, value)
            } else if (p is EditTextPreference) {
                p.setOnPreferenceChangeListener(this@SettingsFragment)
            }
        }

        val sizePreference = findPreference(getString(R.string.pref_size_key))
        sizePreference.onPreferenceChangeListener = this@SettingsFragment
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        // Figure out which preference was changed
        val preference = findPreference(key)
        if (null != preference) {
            // Updates the summary for the preference
            if (preference !is CheckBoxPreference) {
                val value = sharedPreferences.getString(preference.key, "")
                setPreferenceSummary(preference, value)
            }
        }
    }

    /**
     * Updates the summary for the preference
     *
     * @param preference The preference to be updated
     * @param value      The value that the preference was updated to
     */
    private fun setPreferenceSummary(preference: Preference, value: String) {
        if (preference is ListPreference) {
            // For list preferences, figure out the label of the selected value
            val prefIndex = preference.findIndexOfValue(value)
            if (prefIndex >= 0) {
                // Set the summary to that label
                preference.summary = preference.entries[prefIndex]
            }
        } else (preference as? EditTextPreference)?.summary = value
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferenceScreen.sharedPreferences
                .registerOnSharedPreferenceChangeListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        preferenceScreen.sharedPreferences
                .unregisterOnSharedPreferenceChangeListener(this)
    }
}