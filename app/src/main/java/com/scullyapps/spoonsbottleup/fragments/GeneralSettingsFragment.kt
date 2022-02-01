package com.scullyapps.spoonsbottleup.fragments

import android.os.Bundle
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.scullyapps.spoonsbottleup.App
import com.scullyapps.spoonsbottleup.R
import com.scullyapps.spoonsbottleup.data.BottleDatabase
import com.scullyapps.spoonsbottleup.ui.dialogs.DataWarningDialog

class  GeneralSettingsFragment : PreferenceFragmentCompat() {

    val RESET_DATABASE = "resetDatabase"

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.layout_settings_general, rootKey)
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        when(preference.key) {
            RESET_DATABASE -> {
                val dialog = DataWarningDialog(
                    requireContext(),
                    "Reset database",
                    "This will revert any changes to your fridges and bottles. Do you wish to continue?",
                    "Stay",
                    "Revert") { _, _ ->

                    // close database connections
                    BottleDatabase.getInstance(requireContext()).close()

                    // delete, requesting another instance will reset to db from assets folder
                    val deleted = App.getContext().deleteDatabase("BottleDatabase.db")

                    var toastText = "Database has been reset"
                    if(!deleted)
                        toastText = "Error resetting database"

                    Toast.makeText(requireContext(), toastText, Toast.LENGTH_SHORT).show()
                }

                dialog.show()
            }
        }

        return super.onPreferenceTreeClick(preference)
    }
}