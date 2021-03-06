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

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.example.com.visualizerpreferences.AudioVisuals.AudioInputReader
import android.example.com.visualizerpreferences.AudioVisuals.VisualizerView
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.preference.PreferenceManager
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast

class VisualizerActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {
    private var mVisualizerView: VisualizerView? = null
    private var mAudioInputReader: AudioInputReader? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visualizer)
        mVisualizerView = findViewById(R.id.activity_visualizer) as VisualizerView
        setupSharedPreferences()
        setupPermissions()
    }

    private fun setupSharedPreferences() {
        // Get all of the values from shared preferences to set it up
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        mVisualizerView!!.setShowBass(sharedPreferences.getBoolean(getString(R.string.pref_show_bass_key),
                resources.getBoolean(R.bool.pref_show_bass_default)))
        mVisualizerView!!.setShowMid(sharedPreferences.getBoolean(getString(R.string.pref_show_mid_range_key),
                resources.getBoolean(R.bool.pref_show_mid_range_default)))
        mVisualizerView!!.setShowTreble(sharedPreferences.getBoolean(getString(R.string.pref_show_treble_key),
                resources.getBoolean(R.bool.pref_show_treble_default)))
        mVisualizerView!!.setMinSizeScale(1f)

        loadColorFromPreferences(sharedPreferences)
        // Register the listener
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    private fun loadColorFromPreferences(sharedPreferences: SharedPreferences) {
        mVisualizerView!!.setColor(sharedPreferences.getString(getString(R.string.pref_color_key), getString(R.string.pref_color_red_value)))
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key == getString(R.string.pref_show_bass_key)) {
            mVisualizerView!!.setShowBass(sharedPreferences.getBoolean(key, resources.getBoolean(R.bool.pref_show_bass_default)))
        } else if (key == getString(R.string.pref_show_mid_range_key)) {
            mVisualizerView!!.setShowMid(sharedPreferences.getBoolean(key, resources.getBoolean(R.bool.pref_show_mid_range_default)))
        } else if (key == getString(R.string.pref_show_treble_key)) {
            mVisualizerView!!.setShowTreble(sharedPreferences.getBoolean(key, resources.getBoolean(R.bool.pref_show_treble_default)))
        } else {
            loadColorFromPreferences(sharedPreferences)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Unregister VisualizerActivity as an OnPreferenceChangedListener to avoid any memory leaks.
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this)
    }

    /**
     * Methods for setting up the menu
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        val inflater = menuInflater
        /* Use the inflater's inflate method to inflate our visualizer_menu layout to this menu */
        inflater.inflate(R.menu.visualizer_menu, menu)
        /* Return true so that the visualizer_menu is displayed in the Toolbar */
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_settings) {
            val startSettingsActivity = Intent(this, SettingsActivity::class.java)
            startActivity(startSettingsActivity)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Below this point is code you do not need to modify; it deals with permissions
     * and starting/cleaning up the AudioInputReader
     */

    /**
     * onPause Cleanup audio stream
     */
    override fun onPause() {
        super.onPause()
        if (mAudioInputReader != null) {
            mAudioInputReader!!.shutdown(isFinishing)
        }
    }

    override fun onResume() {
        super.onResume()
        if (mAudioInputReader != null) {
            mAudioInputReader!!.restart()
        }
    }

    /**
     * App Permissions for Audio
     */
    private fun setupPermissions() {
        // If we don't have the record audio permission...
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            // And if we're on SDK M or later...
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // Ask again, nicely, for the permissions.
                val permissionsWeNeed = arrayOf(Manifest.permission.RECORD_AUDIO)
                requestPermissions(permissionsWeNeed, MY_PERMISSION_RECORD_AUDIO_REQUEST_CODE)
            }
        } else {
            // Otherwise, permissions were granted and we are ready to go!
            mAudioInputReader = AudioInputReader(mVisualizerView, this)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSION_RECORD_AUDIO_REQUEST_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // The permission was granted! Start up the visualizer!
                    mAudioInputReader = AudioInputReader(mVisualizerView, this)

                } else {
                    Toast.makeText(this, "Permission for audio not granted. Visualizer can't run.", Toast.LENGTH_LONG).show()
                    finish()
                    // The permission was denied, so we can show a message why we can't run the app
                    // and then close the app.
                }
            }
        }// Other permissions could go down here
    }

    companion object {

        private val MY_PERMISSION_RECORD_AUDIO_REQUEST_CODE = 88
    }
}