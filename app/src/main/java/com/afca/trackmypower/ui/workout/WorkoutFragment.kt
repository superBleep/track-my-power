package com.afca.trackmypower.ui.workout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afca.trackmypower.R
import com.afca.trackmypower.Utils
import com.afca.trackmypower.adapters.ExerciseAdapter
import com.afca.trackmypower.models.Exercise
import com.afca.trackmypower.models.MuscleGroup
import dagger.hilt.android.AndroidEntryPoint
import java.time.Duration
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

@AndroidEntryPoint
class WorkoutFragment : Fragment() {
    private val viewModel by viewModels<WorkoutFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_workout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.workoutLiveData.observe(viewLifecycleOwner) { workout ->
            workout!!

            view.findViewById<TextView>(R.id.date).text = workout.date.format(
                DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
            )
            view.findViewById<TextView>(R.id.day_position).text = getString(R.string.workout_position,
                workout.year, workout.month, workout.week, workout.day)
            view.findViewById<TextView>(R.id.time).text = String.format(
                Locale.getDefault(),
                "%s - %s (%s)",
                workout.startTime.let { Utils.formatTime(it) },
                workout.endTime.let { Utils.formatTime(it) },
                Duration.between(workout.startTime, workout.endTime)?.let { Utils.formatDuration(it) }
            )
        }

        view.findViewById<LinearLayout>(R.id.workout_stats).setOnLongClickListener {
            goToWorkoutStatsFragment()
            true
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_exercises)
        val exercises = arrayListOf(
            Exercise(0, "Dumbbell Chest Press", MuscleGroup.Chest),
            Exercise(2, "Weighted Crunches", MuscleGroup.Abs)
        )

        val adapter = ExerciseAdapter(exercises)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        recyclerView.apply {
            this.adapter = adapter
            this.layoutManager = layoutManager
        }
    }

    private fun goToWorkoutStatsFragment() {
        val action = WorkoutFragmentDirections.actionWorkoutFragmentToWorkoutStatsFragment(viewModel.id)

        findNavController().navigate(action)
    }
}