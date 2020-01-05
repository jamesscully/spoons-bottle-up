package com.scullyapps.spoonsbottleup.ui.settings;



import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;


import com.scullyapps.spoonsbottleup.R;


public class GeneralSettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.layout_settings_general, rootKey);

        SwitchPreference floating = (SwitchPreference) findPreference("floatbtn");



    }
}
