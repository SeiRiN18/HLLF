package com.example.shopmobile.storage

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object AppStorage {
    private lateinit var prefs: SharedPreferences
    private val gson = Gson()

    fun init(context: Context) {
        prefs = context.getSharedPreferences("shop_data", Context.MODE_PRIVATE)
    }

    fun <T> save(key: String, value: T) {
        prefs.edit().putString(key, gson.toJson(value)).apply()
    }

    inline fun <reified T> load(key: String, default: T): T {
        val json = prefs.getString(key, null) ?: return default
        return try {
            gson.fromJson(json, object : TypeToken<T>() {}.type) ?: default
        } catch (e: Exception) {
            default
        }
    }
}
