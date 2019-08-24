package com.scullyapps.spoonsbottleup.ui.main;



import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;


import com.scullyapps.spoonsbottleup.R;

import java.util.Map;
import java.util.Set;


public class GeneralSettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.layout_settings_general, rootKey);

        SwitchPreference floating = (SwitchPreference) findPreference("floatbtn");



    }
}
