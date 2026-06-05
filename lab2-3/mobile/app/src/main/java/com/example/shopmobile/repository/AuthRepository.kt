package com.example.shopmobile.repository

import com.example.shopmobile.data.User
import com.example.shopmobile.storage.AppStorage
import java.util.UUID

object AuthRepository {
    private const val KEY_USERS   = "users"
    private const val KEY_SESSION = "session_user"

    private fun getUsers(): MutableList<User> = AppStorage.load(KEY_USERS, mutableListOf<User>())
    private fun saveUsers(u: List<User>) = AppStorage.save(KEY_USERS, u)

    fun currentUser(): User? = AppStorage.load(KEY_SESSION, null as User?)
    fun isLoggedIn(): Boolean = currentUser() != null

    fun register(username: String, email: String, password: String): Result<User> {
        if (getUsers().any { it.username == username })
            return Result.failure(Exception("Логін вже зайнятий"))
        val user = User(UUID.randomUUID().toString(), username, email, password.hashCode().toString())
        saveUsers(getUsers() + user)
        AppStorage.save(KEY_SESSION, user)
        return Result.success(user)
    }

    fun login(username: String, password: String): Result<User> {
        val user = getUsers().find { it.username == username }
            ?: return Result.failure(Exception("Користувача не знайдено"))
        if (user.passwordHash != password.hashCode().toString())
            return Result.failure(Exception("Невірний пароль"))
        AppStorage.save(KEY_SESSION, user)
        return Result.success(user)
    }

    fun logout() = AppStorage.save(KEY_SESSION, null as User?)
}
