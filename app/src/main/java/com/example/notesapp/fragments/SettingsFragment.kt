package com.example.notesapp.fragments

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreferenceCompat
import com.example.notesapp.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        val sharedPreferences = context?.let { PreferenceManager.getDefaultSharedPreferences(it) }

        // Set the theme based on saved preference
        val themePreference = findPreference<ListPreference>("theme")
        if (sharedPreferences != null) {
            themePreference?.value = sharedPreferences.getString("theme", "system")
        }
        themePreference?.setOnPreferenceChangeListener { _, newValue ->
            // Handle theme change
            if (sharedPreferences != null) {
                with(sharedPreferences.edit()) {
                    putString("theme", newValue as String)
                    apply()
                }
            }
            applyTheme(newValue as String)
            true
        }

        // Handle reminders
        val reminderPreference = findPreference<SwitchPreferenceCompat>("reminders")
        reminderPreference?.setOnPreferenceChangeListener { preference, newValue ->
            // Handle reminder toggle
            true
        }
    }

    private fun applyTheme(theme: String) {
        when (theme) {
            "light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            "dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            "system" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }
}
