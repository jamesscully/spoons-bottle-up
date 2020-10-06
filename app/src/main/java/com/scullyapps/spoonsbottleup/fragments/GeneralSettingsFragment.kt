package com.scullyapps.spoonsbottleup.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.scullyapps.spoonsbottleup.R

class GeneralSettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.layout_settings_general, rootKey)
    }
}