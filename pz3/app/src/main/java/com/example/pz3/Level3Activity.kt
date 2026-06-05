package com.example.pz3

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pz3.databinding.ActivityLevel3Binding
import com.example.pz3.databinding.DialogAddMovieBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Level3Activity : AppCompatActivity() {
    private lateinit var binding: ActivityLevel3Binding
    private val adapter = MovieAdapter { movie -> deleteMovie(movie) }
    private val movies = mutableListOf<Movie>()
    private val gson = Gson()
    private val PREFS_KEY = "movies_list"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLevel3Binding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Рівень 3 — Фільми"

        binding.rvMovies.layoutManager = LinearLayoutManager(this)
        binding.rvMovies.adapter = adapter

        binding.btnAddMovie.setOnClickListener { showAddDialog() }

        loadMovies()
        refreshList()
    }

    private fun showAddDialog() {
        val dialogBinding = DialogAddMovieBinding.inflate(LayoutInflater.from(this))
        AlertDialog.Builder(this)
            .setTitle("Додати фільм")
            .setView(dialogBinding.root)
            .setPositiveButton("Додати") { _, _ ->
                val title  = dialogBinding.etTitle.text.toString().trim()
                val genre  = dialogBinding.etGenre.text.toString().trim()
                val rating = dialogBinding.etRating.text.toString().toFloatOrNull()

                when {
                    title.isEmpty() ->
                        Toast.makeText(this, "Введіть назву", Toast.LENGTH_SHORT).show()
                    genre.isEmpty() ->
                        Toast.makeText(this, "Введіть жанр", Toast.LENGTH_SHORT).show()
                    rating == null || rating < 0f || rating > 10f ->
                        Toast.makeText(this, "Рейтинг: число від 0 до 10", Toast.LENGTH_SHORT).show()
                    else -> {
                        movies.add(Movie(title = title, genre = genre, rating = rating))
                        saveMovies()
                        refreshList()
                    }
                }
            }
            .setNegativeButton("Скасувати", null)
            .show()
    }

    private fun deleteMovie(movie: Movie) {
        AlertDialog.Builder(this)
            .setTitle("Видалити фільм?")
            .setMessage("\"${movie.title}\" буде видалено зі списку.")
            .setPositiveButton("Видалити") { _, _ ->
                movies.remove(movie)
                saveMovies()
                refreshList()
            }
            .setNegativeButton("Скасувати", null)
            .show()
    }

    private fun refreshList() {
        adapter.submitList(movies.toList())
        binding.tvEmpty.visibility =
            if (movies.isEmpty()) android.view.View.VISIBLE else android.view.View.GONE
    }

    private fun saveMovies() {
        getSharedPreferences("pz3_prefs", MODE_PRIVATE)
            .edit()
            .putString(PREFS_KEY, gson.toJson(movies))
            .apply()
    }

    private fun loadMovies() {
        val prefs = getSharedPreferences("pz3_prefs", MODE_PRIVATE)
        val json = prefs.getString(PREFS_KEY, null)
        if (json != null) {
            val type = object : TypeToken<List<Movie>>() {}.type
            val loaded: List<Movie> = gson.fromJson(json, type)
            movies.addAll(loaded)
        } else {
            movies.addAll(
                listOf(
                    Movie(title = "Початок",         genre = "Фантастика",  rating = 8.8f),
                    Movie(title = "Темний лицар",    genre = "Екшн",        rating = 9.0f),
                    Movie(title = "Інтерстеллар",    genre = "Фантастика",  rating = 8.6f),
                    Movie(title = "Список Шиндлера", genre = "Драма",       rating = 9.0f),
                    Movie(title = "Форрест Гамп",    genre = "Драма",       rating = 8.8f)
                )
            )
        }
    }
}
