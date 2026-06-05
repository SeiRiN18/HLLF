package com.example.shopmobile.ui.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopmobile.R
import com.example.shopmobile.databinding.FragmentOrdersBinding
import com.example.shopmobile.repository.AuthRepository
import com.example.shopmobile.repository.CartRepository
import com.example.shopmobile.repository.OrderRepository
import com.example.shopmobile.repository.ProductRepository
import com.example.shopmobile.repository.WishlistRepository
import com.example.shopmobile.ui.catalog.ProductAdapter

class OrdersFragment : Fragment() {

    private var _b: FragmentOrdersBinding? = null
    private val b get() = _b!!
    private lateinit var orderAdapter: OrderAdapter
    private lateinit var recAdapter: ProductAdapter

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentOrdersBinding.inflate(i, c, false); return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        orderAdapter = OrderAdapter()
        b.rvOrders.layoutManager = LinearLayoutManager(requireContext())
        b.rvOrders.adapter = orderAdapter

        recAdapter = ProductAdapter(
            onCardClick      = { p ->
                findNavController().navigate(
                    R.id.action_orders_to_detail,
                    Bundle().apply { putString("productId", p.id) }
                )
            },
            onAddToCart      = { p ->
                CartRepository.addItem(p)
                Toast.makeText(requireContext(), "Додано до кошика", Toast.LENGTH_SHORT).show()
            },
            onWishlistToggle = { p -> WishlistRepository.toggle(p.id) }
        )
        b.rvRecommendations.layoutManager = GridLayoutManager(requireContext(), 2)
        b.rvRecommendations.adapter = recAdapter

        load()
    }

    private fun load() {
        if (!AuthRepository.isLoggedIn()) {
            b.layoutGuest.visibility   = View.VISIBLE
            b.layoutContent.visibility = View.GONE
            b.btnLogin.setOnClickListener { findNavController().navigate(R.id.loginFragment) }
            return
        }

        b.layoutGuest.visibility   = View.GONE
        b.layoutContent.visibility = View.VISIBLE

        val orders  = OrderRepository.getAll().sortedByDescending { it.createdAt }
        orderAdapter.submitList(orders)
        b.tvEmptyOrders.visibility = if (orders.isEmpty()) View.VISIBLE else View.GONE
        b.rvOrders.visibility      = if (orders.isEmpty()) View.GONE   else View.VISIBLE

        // Level 4: рекомендації за категоріями придбаних товарів
        val purchasedIds   = OrderRepository.getPurchasedProductIds()
        val categories     = ProductRepository.getAll()
            .filter { it.id in purchasedIds }.map { it.category }.toSet()
        val recs = if (categories.isEmpty()) emptyList()
        else ProductRepository.getAll()
            .filter { it.id !in purchasedIds && it.category in categories }
            .take(4)

        b.tvRecsTitle.visibility      = if (recs.isNotEmpty()) View.VISIBLE else View.GONE
        b.rvRecommendations.visibility = if (recs.isNotEmpty()) View.VISIBLE else View.GONE
        recAdapter.submitList(recs)
    }

    override fun onResume() { super.onResume(); load() }
    override fun onDestroyView() { super.onDestroyView(); _b = null }
}
