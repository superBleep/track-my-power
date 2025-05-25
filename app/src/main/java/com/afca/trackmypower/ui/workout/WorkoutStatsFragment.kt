package com.afca.trackmypower.ui.workout

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.database.sqlite.SQLiteException
import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.afca.trackmypower.DataBindingFragment
import com.afca.trackmypower.R
import com.afca.trackmypower.databinding.FragmentWorkoutStatsBinding
import com.afca.trackmypower.exceptions.WorkoutStatsException
import com.afca.trackmypower.helpers.Constants
import com.afca.trackmypower.helpers.extensions.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.ZoneId

@AndroidEntryPoint
class WorkoutStatsFragment: DataBindingFragment<FragmentWorkoutStatsBinding>(), WorkoutStatsListener {
    private val viewModel by viewModels<WorkoutStatsFragmentViewModel>()

    override val layoutId: Int
        get() = R.layout.fragment_workout_stats


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.listener = this

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.updateState.collect { res ->
                    res.onSuccess {
                        goToWorkout()
                    }.onFailure { e ->
                        showError(e)
                    }
                }
            }
        }
    }

    private fun showError(e: Throwable) {
        val context = requireContext()
        val message: String = when (e) {
            is SQLiteException -> context.getString(R.string.workout_stats_err)
            is WorkoutStatsException -> {
                val code = e.message?.toIntOrNull()
                val notNull = context.getString(R.string.workout_stats_err_null)

                when (code) {
                    Constants.WORKOUT_STATS_ERR_YEAR -> context.getString(R.string.workout_stats_err_year) + notNull
                    Constants.WORKOUT_STATS_ERR_MONTH -> context.getString(R.string.workout_stats_err_month) + notNull
                    Constants.WORKOUT_STATS_ERR_WEEK -> context.getString(R.string.workout_stats_err_week) + notNull
                    Constants.WORKOUT_STATS_ERR_DAY -> context.getString(R.string.workout_stats_err_day) + notNull
                    Constants.WORKOUT_STATS_ERR_DATE_FUTURE -> context.getString(R.string.workout_stats_err_date_future)
                    Constants.WORKOUT_STATS_ERR_YEAR_RANGE -> context.getString(R.string.workout_stats_err_year_range)
                    Constants.WORKOUT_STATS_ERR_MONTH_RANGE -> context.getString(R.string.workout_stats_err_month_range)
                    Constants.WORKOUT_STATS_ERR_WEEK_RANGE -> context.getString(R.string.workout_stats_err_week_range)
                    Constants.WORKOUT_STATS_ERR_DAY_RANGE -> context.getString(R.string.workout_stats_err_day_range)
                    else -> context.getString(R.string.workout_stats_err)
                }

            }
            else -> context.getString(R.string.workout_stats_err)
        }

        context.showToast(message)
    }

    override fun goToWorkout() {
        activity?.supportFragmentManager?.popBackStack()
    }

    override fun setDate() {
        viewModel.workout.observe(viewLifecycleOwner) { workout ->
            workout!!

            val calendar = Calendar.getInstance()

            val datePicker = DatePickerDialog(
                requireContext(),
                { _, selYear, selMonth, selDay ->
                    calendar.set(selYear, selMonth, selDay)

                    viewModel.date.value = calendar.time.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                },
                workout.date.year,
                workout.date.monthValue - 1,
                workout.date.dayOfMonth
            )
            datePicker.show()
        }
    }

    override fun setStartTime() {
        viewModel.workout.observe(viewLifecycleOwner) { value ->
            value!!

            val timePicker = TimePickerDialog(
                requireContext(),
                { _, selHour, selMinute ->
                    viewModel.startTime.value = LocalTime.of(selHour, selMinute)
                },
                value.startTime.hour,
                value.startTime.minute,
                true
            )
            timePicker.show()
        }
    }

    override fun setEndTime() {
        viewModel.workout.observe(viewLifecycleOwner) { value ->
            value!!

            val timePicker = TimePickerDialog(
                requireContext(),
                { _, selHour, selMinute ->
                    viewModel.startTime.value = LocalTime.of(selHour, selMinute)
                },
                value.endTime.hour,
                value.endTime.minute,
                true
            )
            timePicker.show()
        }
    }
}

interface WorkoutStatsListener {
    fun goToWorkout()
    fun setDate()
    fun setStartTime()
    fun setEndTime()
}