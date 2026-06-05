package com.example.shopmobile.data

data class OrderItem(
    val productId: String,
    val name: String,
    val price: Int,
    val qty: Int
)

data class Order(
    val id: String,
    val items: List<OrderItem>,
    val total: Int,
    val address: String,
    val phone: String,
    val payment: String,
    val status: String = "pending",
    val createdAt: Long = System.currentTimeMillis()
)
