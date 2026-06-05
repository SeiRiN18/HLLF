package com.example.shopmobile.ui.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shopmobile.data.Order
import com.example.shopmobile.databinding.ItemOrderBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrderAdapter : ListAdapter<Order, OrderAdapter.VH>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Order>() {
            override fun areItemsTheSame(a: Order, b: Order)    = a.id == b.id
            override fun areContentsTheSame(a: Order, b: Order) = a == b
        }
        private val STATUS = mapOf(
            "pending"   to "Очікує",
            "confirmed" to "Підтверджено",
            "shipped"   to "Відправлено",
            "delivered" to "Доставлено",
            "cancelled" to "Скасовано"
        )
        private val DATE_FMT = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    }

    inner class VH(val b: ItemOrderBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) {
        val order = getItem(position)
        with(holder.b) {
            tvOrderId.text = "#${order.id.take(8)}"
            tvDate.text    = DATE_FMT.format(Date(order.createdAt))
            tvStatus.text  = STATUS[order.status] ?: order.status
            tvItems.text   = order.items.joinToString(", ") { "${it.name} × ${it.qty}" }
            tvTotal.text   = "${order.total.fmt()} грн"
            tvAddress.text = order.address
        }
    }

    private fun Int.fmt() = toString().reversed().chunked(3).joinToString(" ").reversed()
}
