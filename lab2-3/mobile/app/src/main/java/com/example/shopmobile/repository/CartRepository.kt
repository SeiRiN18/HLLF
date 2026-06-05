package com.example.shopmobile.repository

import com.example.shopmobile.data.CartItem
import com.example.shopmobile.data.Product
import com.example.shopmobile.storage.AppStorage

object CartRepository {
    private const val KEY = "cart"

    fun getItems(): List<CartItem> = AppStorage.load(KEY, emptyList<CartItem>())

    private fun save(items: List<CartItem>) = AppStorage.save(KEY, items)

    fun addItem(product: Product) {
        val items = getItems().toMutableList()
        val idx   = items.indexOfFirst { it.product.id == product.id }
        if (idx >= 0) items[idx] = items[idx].copy(qty = items[idx].qty + 1)
        else          items.add(CartItem(product))
        save(items)
    }

    fun removeItem(productId: String) = save(getItems().filter { it.product.id != productId })

    fun changeQty(productId: String, qty: Int) {
        if (qty < 1) { removeItem(productId); return }
        save(getItems().map { if (it.product.id == productId) it.copy(qty = qty) else it })
    }

    fun clear() = save(emptyList())

    fun getTotal(): Int = getItems().sumOf { it.product.price * it.qty }
    fun getCount(): Int = getItems().sumOf { it.qty }
}
