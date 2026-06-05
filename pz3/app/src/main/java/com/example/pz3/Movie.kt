package com.example.pz3

import java.io.Serializable

data class Movie(
    val id: Long = System.currentTimeMillis(),
    val title: String,
    val genre: String,
    val rating: Float
) : Serializable
