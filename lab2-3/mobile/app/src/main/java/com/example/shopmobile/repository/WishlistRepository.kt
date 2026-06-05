package com.example.shopmobile.repository

import com.example.shopmobile.data.Product
import com.example.shopmobile.storage.AppStorage

object WishlistRepository {
    private const val KEY = "wishlist"

    private fun getIds(): MutableSet<String> = AppStorage.load(KEY, mutableSetOf<String>())
    private fun save(ids: Set<String>) = AppStorage.save(KEY, ids)

    fun getItems(): List<Product> {
        val ids = getIds()
        return ProductRepository.getAll().filter { it.id in ids }
    }

    fun toggle(productId: String) {
        val ids = getIds()
        if (productId in ids) ids.remove(productId) else ids.add(productId)
        save(ids)
    }

    fun isWishlisted(productId: String): Boolean = productId in getIds()
}
