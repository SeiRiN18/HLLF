package com.example.shopmobile.ui.catalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shopmobile.R
import com.example.shopmobile.databinding.FragmentCatalogBinding
import com.example.shopmobile.repository.CartRepository
import com.example.shopmobile.repository.ProductRepository
import com.example.shopmobile.repository.WishlistRepository

class CatalogFragment : Fragment() {

    private var _b: FragmentCatalogBinding? = null
    private val b get() = _b!!

    private lateinit var adapter: ProductAdapter
    private var selectedCategory = ""
    private var searchQuery = ""

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentCatalogBinding.inflate(i, c, false); return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ProductAdapter(
            onCardClick      = { p ->
                findNavController().navigate(
                    R.id.action_catalog_to_detail,
                    Bundle().apply { putString("productId", p.id) }
                )
            },
            onAddToCart      = { p ->
                CartRepository.addItem(p)
                Toast.makeText(requireContext(), "Додано до кошика", Toast.LENGTH_SHORT).show()
            },
            onWishlistToggle = { p -> WishlistRepository.toggle(p.id) }
        )

        b.rvProducts.layoutManager = GridLayoutManager(requireContext(), 2)
        b.rvProducts.adapter = adapter

        val categories = listOf("Всі категорії") + ProductRepository.getCategories()
        b.spinnerCategory.adapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_item, categories
        ).also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        b.spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p: AdapterView<*>?, v: View?, pos: Int, id: Long) {
                selectedCategory = if (pos == 0) "" else categories[pos]
                loadProducts()
            }
            override fun onNothingSelected(p: AdapterView<*>?) {}
        }

        b.etSearch.doAfterTextChanged { searchQuery = it.toString(); loadProducts() }
        loadProducts()
    }

    private fun loadProducts() =
        adapter.submitList(ProductRepository.filter(searchQuery, selectedCategory))

    override fun onResume() { super.onResume(); loadProducts() }

    override fun onDestroyView() { super.onDestroyView(); _b = null }
}
