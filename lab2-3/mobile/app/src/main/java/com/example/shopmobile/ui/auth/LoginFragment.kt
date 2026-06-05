package com.example.shopmobile.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.shopmobile.R
import com.example.shopmobile.databinding.FragmentLoginBinding
import com.example.shopmobile.repository.AuthRepository

class LoginFragment : Fragment() {

    private var _b: FragmentLoginBinding? = null
    private val b get() = _b!!

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentLoginBinding.inflate(i, c, false); return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        render()
    }

    private fun render() {
        val user = AuthRepository.currentUser()
        if (user != null) {
            b.layoutLoggedIn.visibility  = View.VISIBLE
            b.layoutLoggedOut.visibility = View.GONE
            b.tvWelcome.text = "Вітаємо, ${user.username}!"
            b.tvEmail.text   = user.email
            b.btnLogout.setOnClickListener { AuthRepository.logout(); render() }
        } else {
            b.layoutLoggedIn.visibility  = View.GONE
            b.layoutLoggedOut.visibility = View.VISIBLE
            b.tvError.visibility         = View.GONE

            b.btnLogin.setOnClickListener {
                val username = b.etUsername.text.toString().trim()
                val password = b.etPassword.text.toString()
                if (username.isBlank() || password.isBlank()) {
                    showError("Заповніть усі поля"); return@setOnClickListener
                }
                AuthRepository.login(username, password)
                    .onSuccess  { render() }
                    .onFailure  { showError(it.message ?: "Помилка") }
            }

            b.btnToRegister.setOnClickListener {
                findNavController().navigate(R.id.action_login_to_register)
            }
        }
    }

    private fun showError(msg: String) {
        b.tvError.text = msg; b.tvError.visibility = View.VISIBLE
    }

    override fun onDestroyView() { super.onDestroyView(); _b = null }
}
