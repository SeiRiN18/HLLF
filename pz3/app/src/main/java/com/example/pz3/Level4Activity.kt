package com.example.pz3

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pz3.databinding.ActivityLevel4Binding
import com.example.pz3.databinding.DialogAddWorkoutBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.*

class Level4Activity : AppCompatActivity() {
    private lateinit var binding: ActivityLevel4Binding
    private val adapter = WorkoutAdapter { workout -> deleteWorkout(workout) }
    private val workouts = mutableListOf<Workout>()
    private val gson = Gson()
    private val PREFS_KEY = "workouts_list"
    private val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    private val workoutTypes = listOf("Біг", "Ходьба", "Велосипед", "Плавання", "Йога", "Силові вправи", "Інше")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLevel4Binding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Рівень 4 — Фітнес-трекер"

        binding.rvWorkouts.layoutManager = LinearLayoutManager(this)
        binding.rvWorkouts.adapter = adapter

        binding.btnAddWorkout.setOnClickListener { showAddDialog() }

        loadWorkouts()
        refreshAll()
    }

    private fun showAddDialog() {
        val db = DialogAddWorkoutBinding.inflate(LayoutInflater.from(this))

        val typeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, workoutTypes)
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        db.spinnerType.adapter = typeAdapter

        val cal = Calendar.getInstance()
        db.etDate.setText(sdf.format(cal.time))
        db.etDate.setOnClickListener {
            DatePickerDialog(this, { _, y, m, d ->
                cal.set(y, m, d)
                db.etDate.setText(sdf.format(cal.time))
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        AlertDialog.Builder(this)
            .setTitle("Додати тренування")
            .setView(db.root)
            .setPositiveButton("Додати") { _, _ ->
                val type     = workoutTypes[db.spinnerType.selectedItemPosition]
                val duration = db.etDuration.text.toString().toIntOrNull()
                val calories = db.etCalories.text.toString().toIntOrNull()
                val date     = db.etDate.text.toString().trim()

                when {
                    duration == null || duration <= 0 ->
                        Toast.makeText(this, "Введіть тривалість (хвилини)", Toast.LENGTH_SHORT).show()
                    calories == null || calories <= 0 ->
                        Toast.makeText(this, "Введіть кількість калорій", Toast.LENGTH_SHORT).show()
                    date.isEmpty() ->
                        Toast.makeText(this, "Оберіть дату", Toast.LENGTH_SHORT).show()
                    else -> {
                        workouts.add(Workout(type = type, duration = duration, calories = calories, date = date))
                        saveWorkouts()
                        refreshAll()
                    }
                }
            }
            .setNegativeButton("Скасувати", null)
            .show()
    }

    private fun deleteWorkout(workout: Workout) {
        AlertDialog.Builder(this)
            .setTitle("Видалити запис?")
            .setMessage("${workout.type} (${workout.date}) буде видалено.")
            .setPositiveButton("Видалити") { _, _ ->
                workouts.remove(workout)
                saveWorkouts()
                refreshAll()
            }
            .setNegativeButton("Скасувати", null)
            .show()
    }

    private fun refreshAll() {
        adapter.submitList(workouts.toList())
        binding.tvEmpty.visibility = if (workouts.isEmpty()) View.VISIBLE else View.GONE
        updateStats()
    }

    private fun updateStats() {
        val total     = workouts.size
        val totalMin  = workouts.sumOf { it.duration }
        val totalCal  = workouts.sumOf { it.calories }
        val hours     = totalMin / 60
        val minutes   = totalMin % 60

        binding.tvStatCount.text    = "Тренувань: $total"
        binding.tvStatTime.text     = "Час: ${hours}г ${minutes}хв"
        binding.tvStatCalories.text = "Калорії: $totalCal ккал"

        val breakdown = workouts.groupBy { it.type }
            .entries.sortedByDescending { it.value.size }
            .joinToString("\n") { (type, list) ->
                "$type: ${list.size} раз, ${list.sumOf { it.calories }} ккал"
            }
        binding.tvStatBreakdown.text = if (breakdown.isEmpty()) "—" else breakdown
    }

    private fun saveWorkouts() {
        getSharedPreferences("pz3_prefs", MODE_PRIVATE)
            .edit()
            .putString(PREFS_KEY, gson.toJson(workouts))
            .apply()
    }

    private fun loadWorkouts() {
        val json = getSharedPreferences("pz3_prefs", MODE_PRIVATE).getString(PREFS_KEY, null)
        if (json != null) {
            val type = object : TypeToken<List<Workout>>() {}.type
            val loaded: List<Workout> = gson.fromJson(json, type)
            workouts.addAll(loaded)
        }
    }
}
