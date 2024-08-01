package com.example.notesapp.fragments

import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.example.notesapp.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        // Access and set up preferences if needed
        val themePreference = findPreference<ListPreference>("theme")
        themePreference?.setOnPreferenceChangeListener { preference, newValue ->
            // Handle theme change
            true
        }

        val reminderPreference = findPreference<SwitchPreferenceCompat>("reminders")
        reminderPreference?.setOnPreferenceChangeListener { preference, newValue ->
            // Handle reminder toggle
            true
        }
    }
}
