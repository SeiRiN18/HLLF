package com.example.shopmobile.ui.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.shopmobile.R
import com.example.shopmobile.data.OrderItem
import com.example.shopmobile.databinding.FragmentCheckoutBinding
import com.example.shopmobile.repository.AuthRepository
import com.example.shopmobile.repository.CartRepository
import com.example.shopmobile.repository.OrderRepository

class CheckoutFragment : Fragment() {

    private var _b: FragmentCheckoutBinding? = null
    private val b get() = _b!!

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentCheckoutBinding.inflate(i, c, false); return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!AuthRepository.isLoggedIn()) {
            findNavController().navigate(R.id.loginFragment)
            return
        }

        val items = CartRepository.getItems()
        val total = CartRepository.getTotal()

        b.tvSummary.text = items.joinToString("\n") {
            "${it.product.name} × ${it.qty}  —  ${(it.product.price * it.qty).fmt()} грн"
        }
        b.tvTotal.text = "Разом: ${total.fmt()} грн"

        b.btnBack.setOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }

        b.btnConfirm.setOnClickListener {
            val address = b.etAddress.text.toString().trim()
            val phone   = b.etPhone.text.toString().trim()
            if (address.isBlank() || phone.isBlank()) {
                Toast.makeText(requireContext(), "Заповніть усі поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val payment = when (b.rgPayment.checkedRadioButtonId) {
                R.id.rbCard     -> "card"
                R.id.rbCash     -> "cash"
                R.id.rbTransfer -> "transfer"
                else            -> "card"
            }
            val orderItems = items.map { OrderItem(it.product.id, it.product.name, it.product.price, it.qty) }
            OrderRepository.create(orderItems, total, address, phone, payment)
            CartRepository.clear()
            Toast.makeText(requireContext(), "Замовлення оформлено!", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.ordersFragment)
        }
    }

    private fun Int.fmt() = toString().reversed().chunked(3).joinToString(" ").reversed()

    override fun onDestroyView() { super.onDestroyView(); _b = null }
}
