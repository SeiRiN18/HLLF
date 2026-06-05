package com.example.shopmobile.ui.wishlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shopmobile.data.Product
import com.example.shopmobile.databinding.ItemProductBinding
import com.example.shopmobile.repository.WishlistRepository

class WishlistAdapter(
    private val onCardClick: (Product) -> Unit,
    private val onAddToCart: (Product) -> Unit,
    private val onRemove:    (Product) -> Unit
) : ListAdapter<Product, WishlistAdapter.VH>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(a: Product, b: Product) = a.id == b.id
            override fun areContentsTheSame(a: Product, b: Product) = a == b
        }
    }

    inner class VH(val b: ItemProductBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) {
        val product = getItem(position)
        with(holder.b) {
            tvName.text     = product.name
            tvCategory.text = product.category
            tvPrice.text    = "${product.price.fmt()} грн"
            tvRating.text   = "★ ${"%.1f".format(product.rating)}"
            btnWishlist.text = "♥"

            root.setOnClickListener         { onCardClick(product) }
            btnAddToCart.setOnClickListener  { onAddToCart(product) }
            btnWishlist.setOnClickListener   { onRemove(product) }
        }
    }

    private fun Int.fmt() = toString().reversed().chunked(3).joinToString(" ").reversed()
}
