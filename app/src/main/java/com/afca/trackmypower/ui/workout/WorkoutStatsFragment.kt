package com.afca.trackmypower.ui.workout

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.database.sqlite.SQLiteException
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.afca.trackmypower.R
import com.afca.trackmypower.Utils.formatDate
import com.afca.trackmypower.Utils.formatTime
import com.afca.trackmypower.databinding.FragmentWorkoutStatsBinding
import com.afca.trackmypower.helpers.extensions.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.ZoneId

@AndroidEntryPoint
class WorkoutStatsFragment : Fragment(), WorkoutStatsListener {
    private var _binding: FragmentWorkoutStatsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<WorkoutStatsFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_workout_stats, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.listener = this

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

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    override fun goToWorkout() {
        activity?.supportFragmentManager?.popBackStack()
    }

    override fun setDate() {
        viewModel.workout.observe(viewLifecycleOwner) { value ->
            value!!

            val calendar = Calendar.getInstance()

            val datePicker = DatePickerDialog(
                requireContext(),
                { _, selYear, selMonth, selDay ->
                    calendar.set(selYear, selMonth, selDay)

                    requireView().findViewById<TextView>(R.id.date_input).text = formatDate(
                        calendar.time
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                    )
                },
                value.date.year,
                value.date.monthValue - 1,
                value.date.dayOfMonth
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
                    requireView().findViewById<TextView>(R.id.start_time_input).text =
                        formatTime(LocalTime.of(selHour, selMinute))
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
                    requireView().findViewById<TextView>(R.id.end_time_input).text =
                        formatTime(LocalTime.of(selHour, selMinute))
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