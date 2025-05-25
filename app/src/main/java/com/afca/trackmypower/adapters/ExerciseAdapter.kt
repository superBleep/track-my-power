package com.afca.trackmypower.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.afca.trackmypower.data.models.ExerciseWithWorkSets
import com.afca.trackmypower.databinding.ItemExerciseBinding

class ExerciseAdapter(
    private val exercises: MutableList<ExerciseWithWorkSets>
) : RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemExerciseBinding.inflate(inflater, parent, false)

        return ExerciseViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return exercises.size
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        exercises.getOrNull(position)?.let { exerciseAndWorkSets ->
            holder.bind(exerciseAndWorkSets)
        }
    }

    inner class ExerciseViewHolder(private val binding: ItemExerciseBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(exerciseAndWorkSets: ExerciseWithWorkSets) {
            binding.exercise = exerciseAndWorkSets.exercise
            binding.workSetAdapter = WorkSetAdapter(exerciseAndWorkSets.workSets)

            binding.root.setOnClickListener {
                val visibility = binding.rvWorkSets.visibility

                if (visibility == View.GONE)
                    binding.rvWorkSets.visibility = View.VISIBLE
                else
                    binding.rvWorkSets.visibility = View.GONE
            }
        }
    }
}
