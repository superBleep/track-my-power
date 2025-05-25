package com.afca.trackmypower.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.afca.trackmypower.data.models.WorkSet
import com.afca.trackmypower.databinding.ItemWorksetBinding

class WorkSetAdapter(
    private val workSets: List<WorkSet>
) : RecyclerView.Adapter<WorkSetAdapter.WorkSetViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkSetViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemWorksetBinding.inflate(inflater, parent, false)

        return WorkSetViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return workSets.size
    }

    override fun onBindViewHolder(holder: WorkSetViewHolder, position: Int) {
        workSets.getOrNull(position)?.let { workSet ->
            holder.bind(workSet)
        }
    }

    inner class WorkSetViewHolder(private val binding: ItemWorksetBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(workSet: WorkSet) {
            binding.workSet = workSet
        }
    }
}
