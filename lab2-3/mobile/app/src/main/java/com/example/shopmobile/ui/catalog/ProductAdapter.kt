package com.example.shopmobile.ui.catalog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shopmobile.data.Product
import com.example.shopmobile.databinding.ItemProductBinding
import com.example.shopmobile.repository.WishlistRepository

class ProductAdapter(
    private val onCardClick:      (Product) -> Unit,
    private val onAddToCart:      (Product) -> Unit,
    private val onWishlistToggle: (Product) -> Unit
) : ListAdapter<Product, ProductAdapter.VH>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(a: Product, b: Product)    = a.id == b.id
            override fun areContentsTheSame(a: Product, b: Product) = a == b
        }
    }

    inner class VH(val b: ItemProductBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) {
        val p = getItem(position)
        with(holder.b) {
            tvName.text     = p.name
            tvCategory.text = p.category
            tvPrice.text    = "${p.price.fmt()} грн"
            tvRating.text   = "★ ${"%.1f".format(p.rating)}"
            updateWishBtn(this, p.id)

            root.setOnClickListener        { onCardClick(p) }
            btnAddToCart.setOnClickListener { onAddToCart(p) }
            btnWishlist.setOnClickListener  {
                onWishlistToggle(p)
                updateWishBtn(this, p.id)
            }
        }
    }

    private fun updateWishBtn(b: ItemProductBinding, id: String) {
        b.btnWishlist.text = if (WishlistRepository.isWishlisted(id)) "♥" else "♡"
    }

    private fun Int.fmt() = toString().reversed().chunked(3).joinToString(" ").reversed()
}
