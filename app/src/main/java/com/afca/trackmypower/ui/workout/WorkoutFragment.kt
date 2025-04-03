package com.afca.trackmypower.ui.workout

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afca.trackmypower.R
import com.afca.trackmypower.Utils
import com.afca.trackmypower.adapters.ExerciseAdapter
import com.afca.trackmypower.data.models.Workout
import com.afca.trackmypower.data.repositories.WorkoutRepository
import com.afca.trackmypower.models.Exercise
import com.afca.trackmypower.models.MuscleGroup
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class WorkoutFragment : Fragment() {
    private var workout: Workout? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_workout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getWorkout(view)

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
        val action = WorkoutFragmentDirections.actionWorkoutFragmentToWorkoutStatsFragment()

        findNavController().navigate(action)
    }

    private fun setStats(view: View, workout: Workout) {
        view.findViewById<TextView>(R.id.date).text = workout.date.toString()
        view.findViewById<TextView>(R.id.day_position).text = String.format(
            Locale.getDefault(),
            "Year %d - Month %d - Week %d - Day %d",
            workout.year, workout.month, workout.week, workout.day
        )
        view.findViewById<TextView>(R.id.time).text = String.format(
            Locale.getDefault(),
            "%s - %s (%s)",
            workout.startTime.let { Utils.formatTime(it) },
            workout.endTime.let { Utils.formatTime(it) },
            Duration.between(workout.startTime, workout.endTime)?.let { Utils.formatDuration(it) }
        )
    }

    private fun getWorkout(view: View) {
        WorkoutRepository.get(1) { storedWorkout ->
            if (storedWorkout != null)
                workout = storedWorkout
            else {
                val newWorkout = Workout(
                    date = LocalDate.now(),
                    year = 1,
                    month = 2,
                    week = 1,
                    day = 3,
                    startTime = LocalTime.now(),
                    endTime = LocalTime.now().plusHours(1).plusMinutes(30),
                )

                WorkoutRepository.insert(newWorkout) {
                    workout = newWorkout
                    setStats(view, newWorkout)
                }

                workout?.let { setStats(view, it) }
            }
        }
    }
}