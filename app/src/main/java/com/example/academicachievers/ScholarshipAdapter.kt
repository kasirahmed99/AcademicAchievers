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

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvCountry: TextView = itemView.findViewById(R.id.tvCountry)
        val tvDeadline: TextView = itemView.findViewById(R.id.tvDeadline)
        val tvAction: TextView = itemView.findViewById(R.id.tvAction)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_scholarship, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val s = items[position]

        holder.tvName.text = s.name

        holder.tvCountry.text = "${s.country}"

        holder.tvDeadline.text =
            if (!s.deadline.isNullOrEmpty()) "Deadline: ${s.deadline}"
            else "Deadline: Not specified"

        holder.tvAction.setOnClickListener {
            onClick(s)
        }

        holder.itemView.setOnClickListener {
            onClick(s)
        }
    }

    override fun getItemCount(): Int = items.size

    fun update(newList: List<Scholarship>) {
        items = newList
        notifyDataSetChanged()
    }
}
