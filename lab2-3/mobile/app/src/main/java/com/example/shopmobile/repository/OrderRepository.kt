package com.example.shopmobile.repository

import com.example.shopmobile.data.Order
import com.example.shopmobile.data.OrderItem
import com.example.shopmobile.storage.AppStorage
import java.util.UUID

object OrderRepository {
    private const val KEY = "orders"

    fun getAll(): List<Order> = AppStorage.load(KEY, emptyList<Order>())
    private fun save(orders: List<Order>) = AppStorage.save(KEY, orders)

    fun create(items: List<OrderItem>, total: Int, address: String, phone: String, payment: String): Order {
        val order = Order(UUID.randomUUID().toString(), items, total, address, phone, payment)
        save(getAll() + order)
        return order
    }

    fun getPurchasedProductIds(): Set<String> =
        getAll().flatMap { it.items }.map { it.productId }.toSet()
}
