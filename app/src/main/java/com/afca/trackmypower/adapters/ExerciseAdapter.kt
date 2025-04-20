package com.afca.trackmypower.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.afca.trackmypower.R
import com.afca.trackmypower.data.models.Exercise

class ExerciseAdapter(
    private val exercises: List<Exercise>
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

        Log.i("CHECK", "onBindViewHolder $position")
        if (holder is ExerciseViewHolder)
            holder.bind(exercise)
    }

    inner class ExerciseViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
        fun bind(exercise: Exercise) {
            view.findViewById<TextView>(R.id.tv_name).text = exercise.name
            view.findViewById<TextView>(R.id.tv_group).text = exercise.group.name
        }
    }
}