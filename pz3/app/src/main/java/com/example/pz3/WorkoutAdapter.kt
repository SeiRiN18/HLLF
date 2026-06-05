package com.example.pz3

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pz3.databinding.ItemWorkoutBinding

class WorkoutAdapter(
    private val onDelete: (Workout) -> Unit
) : ListAdapter<Workout, WorkoutAdapter.VH>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Workout>() {
            override fun areItemsTheSame(a: Workout, b: Workout) = a.id == b.id
            override fun areContentsTheSame(a: Workout, b: Workout) = a == b
        }
    }

    inner class VH(val b: ItemWorkoutBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemWorkoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) {
        val w = getItem(position)
        with(holder.b) {
            tvType.text     = w.type
            tvDuration.text = "${w.duration} хв"
            tvCalories.text = "${w.calories} ккал"
            tvDate.text     = w.date
            btnDelete.setOnClickListener { onDelete(w) }
        }
    }
}
