package com.afca.trackmypower.ui.workout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afca.trackmypower.R
import com.afca.trackmypower.adapters.ExerciseAdapter
import com.afca.trackmypower.models.Exercise
import com.afca.trackmypower.models.MuscleGroup

class WorkoutFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_workout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_exercises)
        val exercises = arrayListOf(
            Exercise(0, "Dumbbell Chest Press", MuscleGroup.Chest),
            Exercise(1, "Seated Leg Extensions", MuscleGroup.Legs),
            Exercise(2, "Weighted Crunches", MuscleGroup.Abs)
        )

        val adapter = ExerciseAdapter(exercises)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        recyclerView.apply {
            this.adapter = adapter
            this.layoutManager = layoutManager
        }
    }
}