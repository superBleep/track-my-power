package com.afca.trackmypower.ui.workout

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.database.sqlite.SQLiteException
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.afca.trackmypower.Constants
import com.afca.trackmypower.R
import com.afca.trackmypower.Utils.formatDate
import com.afca.trackmypower.Utils.formatTime
import com.afca.trackmypower.data.LocalDatabaseConverters
import com.afca.trackmypower.data.models.Workout
import com.afca.trackmypower.helpers.extensions.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class WorkoutStatsFragment : Fragment() {
    private val viewModel by viewModels<WorkoutStatsFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_workout_stats, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dateInput = view.findViewById<TextView>(R.id.date_input)
        val yearInput = view.findViewById<EditText>(R.id.year_input)
        val monthInput = view.findViewById<EditText>(R.id.month_input)
        val weekInput = view.findViewById<EditText>(R.id.week_input)
        val dayInput = view.findViewById<EditText>(R.id.day_input)
        val startTimeInput = view.findViewById<TextView>(R.id.start_time_input)
        val endTimeInput = view.findViewById<TextView>(R.id.end_time_input)

        viewModel.workoutLiveData.observe(viewLifecycleOwner) { workout ->
            workout!!

            dateInput.text = formatDate(workout.date)
            dateInput.setOnClickListener {
                setDate(it as TextView, workout.date.year, workout.date.monthValue, workout.date.dayOfMonth)
            }

            startTimeInput.text = formatTime(LocalTime.of(workout.startTime.hour, workout.startTime.minute))
            startTimeInput.setOnClickListener {
                setTime(it as TextView, workout.startTime.hour, workout.startTime.minute)
            }

            endTimeInput.text = formatTime(LocalTime.of(workout.endTime.hour, workout.endTime.minute))
            endTimeInput.setOnClickListener {
                setTime(it as TextView, workout.endTime.hour, workout.endTime.minute)
            }

            yearInput.setText(workout.year.toString())
            monthInput.setText(workout.month.toString())
            view.findViewById<EditText>(R.id.week_input).setText(workout.week.toString())
            view.findViewById<EditText>(R.id.day_input).setText(workout.day.toString())
        }

        view.findViewById<ImageButton>(R.id.back_button).setOnClickListener {
            goToWorkout()
        }

        view.findViewById<TextView>(R.id.done_button).setOnClickListener {
            viewModel.updateWorkout(
                Workout(
                    id = viewModel.id,
                    date = LocalDate.parse(dateInput.text.toString(),
                        DateTimeFormatter.ofLocalizedDate(Constants.LOCAL_DATE_FORMAT_STYLE)),
                    year = yearInput.text.toString().toInt(),
                    month = monthInput.text.toString().toInt(),
                    week = weekInput.text.toString().toInt(),
                    day = dayInput.text.toString().toInt(),
                    startTime = LocalDatabaseConverters.toLocalTime(startTimeInput.text.toString()),
                    endTime = LocalDatabaseConverters.toLocalTime(endTimeInput.text.toString()),
                )
            )
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.updateState.collect { res ->
                    res.onSuccess {
                        goToWorkout()
                    }.onFailure { e ->
                        if (e is SQLiteException) {
                            requireContext().showToast("Failed to update workout!")
                        } else {
                            requireContext().showToast(e.message.toString())
                        }
                    }
                }
            }
        }
    }

    private fun goToWorkout() {
        activity?.supportFragmentManager?.popBackStack()
    }

    private fun setDate(dateInput: TextView, year: Int, month: Int, day: Int) {
        val calendar = Calendar.getInstance()

        val datePicker = DatePickerDialog(
            requireContext(),
            { _, selYear, selMonth, selDay ->
                calendar.set(selYear, selMonth, selDay)

                dateInput.text = formatDate(calendar.time
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                )
            },
            year, month - 1, day
        )
        datePicker.show()
    }

    private fun setTime(timeInput: TextView, hour: Int, minute: Int) {
        val timePickerStart = TimePickerDialog(
            requireContext(),
            { _, selHour, selMinute ->
                timeInput.text = formatTime(LocalTime.of(selHour, selMinute))
            },
            hour, minute, true
        )
        timePickerStart.show()
    }
}