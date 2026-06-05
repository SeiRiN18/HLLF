package com.example.pz3

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pz3.databinding.ItemMovieBinding

class MovieAdapter(
    private val onDelete: (Movie) -> Unit
) : ListAdapter<Movie, MovieAdapter.VH>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(a: Movie, b: Movie) = a.id == b.id
            override fun areContentsTheSame(a: Movie, b: Movie) = a == b
        }
    }

    inner class VH(val b: ItemMovieBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) {
        val movie = getItem(position)
        with(holder.b) {
            tvTitle.text  = movie.title
            tvGenre.text  = movie.genre
            tvRating.text = "★ ${"%.1f".format(movie.rating)}"
            btnDelete.setOnClickListener { onDelete(movie) }
        }
    }
}
