package com.example.shopmobile.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shopmobile.data.CartItem
import com.example.shopmobile.databinding.ItemCartBinding

class CartAdapter(
    private val onQtyChange: (String, Int) -> Unit,
    private val onRemove:    (String) -> Unit
) : ListAdapter<CartItem, CartAdapter.VH>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<CartItem>() {
            override fun areItemsTheSame(a: CartItem, b: CartItem)    = a.product.id == b.product.id
            override fun areContentsTheSame(a: CartItem, b: CartItem) = a == b
        }
    }

    inner class VH(val b: ItemCartBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        with(holder.b) {
            tvName.text     = item.product.name
            tvCategory.text = item.product.category
            tvPrice.text    = "${item.product.price.fmt()} грн"
            tvQty.text      = item.qty.toString()
            tvSubtotal.text = "${(item.product.price * item.qty).fmt()} грн"

            btnMinus.setOnClickListener  { onQtyChange(item.product.id, item.qty - 1) }
            btnPlus.setOnClickListener   { onQtyChange(item.product.id, item.qty + 1) }
            btnRemove.setOnClickListener { onRemove(item.product.id) }
        }
    }

    private fun Int.fmt() = toString().reversed().chunked(3).joinToString(" ").reversed()
}
