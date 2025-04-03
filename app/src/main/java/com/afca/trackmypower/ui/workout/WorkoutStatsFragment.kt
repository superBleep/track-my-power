package com.afca.trackmypower.ui.workout

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.afca.trackmypower.R
import java.text.DateFormat
import java.util.Locale

class WorkoutStatsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_workout_stats, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ImageButton>(R.id.back_button).setOnClickListener {
            goToWorkout()
        }

        view.findViewById<TextView>(R.id.date_input).setOnClickListener {
            setDate(view, it as TextView)
        }

        view.findViewById<TextView>(R.id.start_time_input).setOnClickListener {
            setTime(view, it as TextView)
        }

        view.findViewById<TextView>(R.id.end_time_input).setOnClickListener {
            setTime(view, it as TextView)
        }
    }

    private fun goToWorkout() {
        activity?.supportFragmentManager?.popBackStack()
    }

    private fun setDate(view : View, dateInput : TextView) {
        val calendar = Calendar.getInstance()

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(
            view.context,
            { _, selYear, selMonth, selDay ->
                calendar.set(selYear, selMonth, selDay)

                val formatter = DateFormat.getDateInstance(DateFormat.MEDIUM,
                    Locale.getDefault())
                val date = formatter.format(calendar.time)

                dateInput.text = date
            },
            year, month, day
        )

        datePicker.show()
    }

    private fun setTime(view : View, timeInput : TextView) {
        val calendar = Calendar.getInstance()

        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePicker = TimePickerDialog(
            view.context,
            { _, selHour, selMinute ->
                val time = String.format(Locale.getDefault(), "%02d:%02d", selHour, selMinute)

                timeInput.text = time
            },
            hour, minute, true
        )

        timePicker.show()
    }
}