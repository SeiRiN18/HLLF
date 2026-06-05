package com.example.pz3

import java.io.Serializable

data class Workout(
    val id: Long = System.currentTimeMillis(),
    val type: String,
    val duration: Int,
    val calories: Int,
    val date: String
) : Serializable
