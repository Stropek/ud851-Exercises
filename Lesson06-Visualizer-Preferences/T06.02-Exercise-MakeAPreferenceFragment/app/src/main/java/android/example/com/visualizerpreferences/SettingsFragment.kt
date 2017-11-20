package android.example.com.visualizerpreferences

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat

/**
 * Created by p.s.curzytek on 11/15/2017.
 */

class SettingsFragment: PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.pref_visualizer)
    }
}
