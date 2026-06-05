package com.example.shopmobile.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopmobile.R
import com.example.shopmobile.databinding.FragmentCartBinding
import com.example.shopmobile.repository.CartRepository

class CartFragment : Fragment() {

    private var _b: FragmentCartBinding? = null
    private val b get() = _b!!
    private lateinit var adapter: CartAdapter

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentCartBinding.inflate(i, c, false); return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = CartAdapter(
            onQtyChange = { id, qty -> CartRepository.changeQty(id, qty); refresh() },
            onRemove    = { id -> CartRepository.removeItem(id); refresh() }
        )
        b.rvCart.layoutManager = LinearLayoutManager(requireContext())
        b.rvCart.adapter = adapter

        b.btnCheckout.setOnClickListener { findNavController().navigate(R.id.action_cart_to_checkout) }
        b.btnClear.setOnClickListener   { CartRepository.clear(); refresh() }

        refresh()
    }

    private fun refresh() {
        val items   = CartRepository.getItems()
        val isEmpty = items.isEmpty()
        adapter.submitList(items)
        b.tvTotal.text = "Разом: ${CartRepository.getTotal().fmt()} грн"
        b.tvEmpty.visibility      = if (isEmpty) View.VISIBLE else View.GONE
        b.rvCart.visibility       = if (isEmpty) View.GONE   else View.VISIBLE
        b.layoutBottom.visibility = if (isEmpty) View.GONE   else View.VISIBLE
    }

    private fun Int.fmt() = toString().reversed().chunked(3).joinToString(" ").reversed()

    override fun onResume() { super.onResume(); refresh() }
    override fun onDestroyView() { super.onDestroyView(); _b = null }
}
