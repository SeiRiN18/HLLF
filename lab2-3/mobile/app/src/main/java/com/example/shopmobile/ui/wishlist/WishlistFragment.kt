package com.example.shopmobile.ui.wishlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shopmobile.R
import com.example.shopmobile.databinding.FragmentWishlistBinding
import com.example.shopmobile.repository.CartRepository
import com.example.shopmobile.repository.WishlistRepository
import com.example.shopmobile.ui.catalog.ProductAdapter

class WishlistFragment : Fragment() {

    private var _b: FragmentWishlistBinding? = null
    private val b get() = _b!!
    private lateinit var adapter: ProductAdapter

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentWishlistBinding.inflate(i, c, false); return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ProductAdapter(
            onCardClick      = { p ->
                findNavController().navigate(
                    R.id.action_wishlist_to_detail,
                    Bundle().apply { putString("productId", p.id) }
                )
            },
            onAddToCart      = { p ->
                CartRepository.addItem(p)
                Toast.makeText(requireContext(), "Додано до кошика", Toast.LENGTH_SHORT).show()
            },
            onWishlistToggle = { p -> WishlistRepository.toggle(p.id); load() }
        )
        b.rvWishlist.layoutManager = GridLayoutManager(requireContext(), 2)
        b.rvWishlist.adapter = adapter
        load()
    }

    private fun load() {
        val items   = WishlistRepository.getItems()
        val isEmpty = items.isEmpty()
        adapter.submitList(items)
        b.tvEmpty.visibility    = if (isEmpty) View.VISIBLE else View.GONE
        b.rvWishlist.visibility = if (isEmpty) View.GONE   else View.VISIBLE
    }

    override fun onResume() { super.onResume(); load() }
    override fun onDestroyView() { super.onDestroyView(); _b = null }
}
