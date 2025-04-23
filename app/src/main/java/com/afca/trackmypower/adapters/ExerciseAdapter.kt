package com.afca.trackmypower.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isGone
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afca.trackmypower.R
import com.afca.trackmypower.data.models.ExerciseWithWorkSets

class ExerciseAdapter(
    private val exercises: List<ExerciseWithWorkSets>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_exercise, parent, false)

        return ExerciseViewHolder(view)
    }

    override fun getItemCount(): Int {
        return exercises.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val exercise = exercises.getOrNull(position) ?: return

        if (holder is ExerciseViewHolder)
            holder.bind(exercise)
    }

    fun toggleWorkSetsVisibility(exerciseView: View, workSetsView: View) {
        exerciseView.setOnClickListener {
            if (workSetsView.isGone) {
                workSetsView.visibility = View.VISIBLE
            } else {
                workSetsView.visibility = View.GONE
            }
        }
    }

    inner class ExerciseViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        private val workSetRV = view.findViewById<RecyclerView>(R.id.rv_worksets)

        fun bind(exerciseWithWorkSets: ExerciseWithWorkSets) {
            toggleWorkSetsVisibility(view, workSetRV)

            view.findViewById<TextView>(R.id.tv_name).text = exerciseWithWorkSets.exercise.name
            view.findViewById<TextView>(R.id.tv_group).text = exerciseWithWorkSets.exercise.group.name

            workSetRV.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
            workSetRV.adapter = WorkSetAdapter(exerciseWithWorkSets.workSets)
        }
    }
}