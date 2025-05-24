package com.example.FillThePlate

import android.os.Bundle
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import android.widget.Toast

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        // Create preferences UI from XML

        setPreferencesFromResource(R.xml.preferences, rootKey)

        // Get the preference object for backend URL input
        val urlPreference: EditTextPreference? = findPreference("backend_url")

        // Set a listener to save URL and show confirmation toast
        urlPreference?.setOnPreferenceChangeListener { preference, newValue ->
            // Save new URL
            val sharedPref = PreferenceManager.getDefaultSharedPreferences(requireContext())
            with(sharedPref.edit()) {
                putString("backend_url", newValue as String)  // Save URL in SharedPreferences
                apply()
            }
            Toast.makeText(context, "Backend URL saved!", Toast.LENGTH_SHORT).show()
            true  // Return true to save the new value
        }
    }
}
