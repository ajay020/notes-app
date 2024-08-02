package com.example.notesapp.fragments

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreferenceCompat
import com.example.notesapp.R
import com.example.notesapp.ReminderBroadcastReceiver
import java.util.Calendar

class SettingsFragment : PreferenceFragmentCompat() {
    val TAG = "SettingsFragment"
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
        val reminderTimePreference = findPreference<Preference>("reminder_time")

        // Set the initial visibility of the reminder time preference
        reminderTimePreference?.isVisible = reminderPreference?.isChecked == true

        reminderPreference?.setOnPreferenceChangeListener { _, newValue ->
            // Handle reminder toggle
            if (newValue as Boolean) {
                context?.let { checkAndRequestExactAlarmPermission(it) }
                reminderTimePreference?.isVisible = true
                val hour = sharedPreferences?.getInt("reminder_hour", 8) ?: 8
                val minute = sharedPreferences?.getInt("reminder_minute", 0) ?: 0
                saveReminderTime(context, hour, minute)
            } else {
                context?.let { cancelReminder(it) }
                reminderTimePreference?.isVisible = false
            }
            true
        }

        reminderTimePreference?.setOnPreferenceClickListener {
            showTimePickerDialog(context)
            true
        }
    }

    private fun showTimePickerDialog(context: Context?) {
        val sharedPreferences = context?.let { PreferenceManager.getDefaultSharedPreferences(it) }
        val hour = sharedPreferences?.getInt("reminder_hour", 8)!!
        val minute = sharedPreferences.getInt("reminder_minute", 0)

        val timePickerDialog = TimePickerDialog(
            context,
            { _, selectedHour, selectedMinute ->
                saveReminderTime(context, selectedHour, selectedMinute)
            }, hour, minute, true
        )
        timePickerDialog.show()
    }

    private fun updateReminderTimeSummary() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val hour = sharedPreferences.getInt("reminder_hour", -1)
        val minute = sharedPreferences.getInt("reminder_minute", -1)

        if (hour != -1 && minute != -1) {
            val formattedTime = String.format("%02d:%02d", hour, minute)
            findPreference<Preference>("reminder_time")?.summary = formattedTime
        }
    }

    private fun saveReminderTime(context: Context?, hour: Int, minute: Int) {
        // Save time in SharedPreferences or set alarm directly
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        with(sharedPreferences.edit()) {
            putInt("reminder_hour", hour)
            putInt("reminder_minute", minute)
            apply()
        }

        updateReminderTimeSummary()
        // Set the alarm here if you want
        if (context != null) {
            setDailyReminder(context, hour, minute)
        }
    }

    private fun setDailyReminder(context: Context, hour: Int, minute: Int) {
        // Your existing code to set an alarm
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderBroadcastReceiver::class.java).apply {
            action = "com.example.notesapp.ACTION_REMINDER"
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }

        if (calendar.timeInMillis < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
                )
            }
        } else {
            // For older versions
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        }
    }

    // Change theme
    private fun applyTheme(theme: String) {
        when (theme) {
            "light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            "dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            "system" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }

    private fun cancelReminder(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderBroadcastReceiver::class.java).apply {
            action = "com.example.notesapp.ACTION_REMINDER"
        }
        val pendingIntent =
            PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        alarmManager.cancel(pendingIntent)
    }

    private fun checkAndRequestExactAlarmPermission(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (!alarmManager.canScheduleExactAlarms()) {
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                context.startActivity(intent)
            }
        }
    }
}
