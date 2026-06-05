package com.example.shopmobile.data

data class Product(
    val id: String,
    val name: String,
    val category: String,
    val price: Int,
    val description: String,
    val rating: Double,
    val stock: Int
)
