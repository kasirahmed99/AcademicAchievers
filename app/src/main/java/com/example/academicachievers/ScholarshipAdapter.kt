package com.example.academicachievers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ScholarshipAdapter(
    private var items: List<Scholarship>,
    private val onClick: (Scholarship) -> Unit
) : RecyclerView.Adapter<ScholarshipAdapter.ViewHolder>() {

    fun updateData(newItems: List<Scholarship>) {
        items = newItems
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvCountry: TextView = view.findViewById(R.id.tvCountry)
        val tvDeadline: TextView = view.findViewById(R.id.tvDeadline)
        val tvAction: TextView = view.findViewById(R.id.tvAction)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_scholarship, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val s = items[position]
        holder.tvName.text = s.name
        holder.tvCountry.text = s.country
        holder.tvDeadline.text = if (s.deadline.isNullOrBlank()) {
            "Deadline: Not specified"
        } else {
            "Deadline: ${s.deadline}"
        }

        holder.itemView.setOnClickListener { onClick(s) }
        holder.tvAction.setOnClickListener { onClick(s) }
    }
}
