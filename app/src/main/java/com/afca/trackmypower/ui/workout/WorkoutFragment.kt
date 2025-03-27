package com.afca.trackmypower.ui.workout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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

        view.findViewById<TextView>(R.id.date).text = "01 Jan, 2025"
        view.findViewById<TextView>(R.id.day_position).text = "Year 1 - Month 1 - Week 1 - Day 1"
        view.findViewById<TextView>(R.id.time).text = "13:00 - 14:30 (1h30m)"

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
}