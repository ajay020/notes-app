package com.example.notesapp

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TimePicker
import androidx.preference.DialogPreference
import android.app.TimePickerDialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.preference.PreferenceDialogFragmentCompat


class TimePickerPreference(context: Context, attrs: AttributeSet?) :
    DialogPreference(context, attrs) {

    fun persistTime(hour: Int, minute: Int) {
        val time = "$hour:$minute"
        persistString(time)
        summary = time
    }

    fun getPersistedTime(): String {
        return getPersistedString("00:00")
    }
}


class TimePickerPreferenceDialogFragmentCompat : PreferenceDialogFragmentCompat(),
    TimePickerDialog.OnTimeSetListener {

    private lateinit var timePicker: TimePicker

    companion object {
        fun newInstance(key: String): TimePickerPreferenceDialogFragmentCompat {
            val fragment = TimePickerPreferenceDialogFragmentCompat()
            val bundle = Bundle(1)
            bundle.putString(ARG_KEY, key)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateDialogView(context: Context): View {
        // Inflate the custom layout
        return LayoutInflater.from(context).inflate(R.layout.time_picker_dialog, null)
    }

    override fun onBindDialogView(view: View) {
        super.onBindDialogView(view)

        // Find the TimePicker in the view
        timePicker = view.findViewById(R.id.time_picker)

        // Initialize TimePicker with current persisted time
        val preference = preference as TimePickerPreference
        val persistedTime = preference.getPersistedTime().split(":")
        timePicker.hour = persistedTime[0].toInt()
        timePicker.minute = persistedTime[1].toInt()
    }

    override fun onDialogClosed(positiveResult: Boolean) {
        if (positiveResult) {
            val hour = timePicker.hour
            val minute = timePicker.minute

            val preference = preference as TimePickerPreference
            // Persist the selected time
            preference.persistTime(hour, minute)
        }
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        // Handle time set
    }
}
