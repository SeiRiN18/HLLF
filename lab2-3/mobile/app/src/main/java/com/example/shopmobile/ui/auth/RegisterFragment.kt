package com.example.shopmobile.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.shopmobile.databinding.FragmentRegisterBinding
import com.example.shopmobile.repository.AuthRepository

class RegisterFragment : Fragment() {

    private var _b: FragmentRegisterBinding? = null
    private val b get() = _b!!

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentRegisterBinding.inflate(i, c, false); return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        b.btnBack.setOnClickListener { findNavController().navigateUp() }

        b.btnRegister.setOnClickListener {
            val username = b.etUsername.text.toString().trim()
            val email    = b.etEmail.text.toString().trim()
            val password = b.etPassword.text.toString()
            val confirm  = b.etConfirm.text.toString()

            when {
                username.isBlank() || email.isBlank() || password.isBlank() ->
                    showError("Заповніть усі поля")
                password != confirm ->
                    showError("Паролі не збігаються")
                password.length < 6 ->
                    showError("Пароль мінімум 6 символів")
                else -> AuthRepository.register(username, email, password)
                    .onSuccess  { findNavController().navigateUp() }
                    .onFailure  { showError(it.message ?: "Помилка") }
            }
        }
    }

    private fun showError(msg: String) {
        b.tvError.text = msg; b.tvError.visibility = View.VISIBLE
    }

    override fun onDestroyView() { super.onDestroyView(); _b = null }
}
