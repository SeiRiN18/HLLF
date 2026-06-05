package com.example.shopmobile.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.shopmobile.databinding.FragmentProductDetailBinding
import com.example.shopmobile.repository.CartRepository
import com.example.shopmobile.repository.ProductRepository
import com.example.shopmobile.repository.WishlistRepository

class ProductDetailFragment : Fragment() {

    private var _b: FragmentProductDetailBinding? = null
    private val b get() = _b!!

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentProductDetailBinding.inflate(i, c, false); return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val product = ProductRepository.getById(arguments?.getString("productId") ?: "") ?: return

        b.tvName.text        = product.name
        b.tvCategory.text    = product.category
        b.tvPrice.text       = "${product.price.fmt()} грн"
        b.tvRating.text      = "★ ${"%.1f".format(product.rating)}"
        b.tvStock.text       = "В наявності: ${product.stock} шт."
        b.tvDescription.text = product.description

        refreshWishBtn(product.id)

        b.btnAddToCart.setOnClickListener {
            CartRepository.addItem(product)
            Toast.makeText(requireContext(), "Додано до кошика", Toast.LENGTH_SHORT).show()
        }
        b.btnWishlist.setOnClickListener {
            WishlistRepository.toggle(product.id)
            refreshWishBtn(product.id)
        }
        b.btnBack.setOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }
    }

    private fun refreshWishBtn(id: String) {
        b.btnWishlist.text = if (WishlistRepository.isWishlisted(id)) "♥ В бажаних" else "♡ В бажані"
    }

    private fun Int.fmt() = toString().reversed().chunked(3).joinToString(" ").reversed()

    override fun onDestroyView() { super.onDestroyView(); _b = null }
}
