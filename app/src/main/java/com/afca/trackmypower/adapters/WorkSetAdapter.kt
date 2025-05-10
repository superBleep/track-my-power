package com.afca.trackmypower.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.afca.trackmypower.R
import com.afca.trackmypower.data.models.WorkSet

class WorkSetAdapter(
    private val workSets: List<WorkSet>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_workset, parent, false)

        return WorkSetViewHolder(view)
    }

    override fun getItemCount(): Int {
        return workSets.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val workSet = workSets.getOrNull(position) ?: return

        if (holder is WorkSetViewHolder)
            holder.bind(workSet)
    }

    inner class WorkSetViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
        fun bind(workSet: WorkSet) {
            view.findViewById<TextView>(R.id.tv_nr).text =
                view.context.getString(R.string.workset_set, workSet.number)
            view.findViewById<TextView>(R.id.tv_work_string).text = workSet.workString
            view.findViewById<TextView>(R.id.tv_details).text = workSet.score.toString()
        }
    }
}