package com.example.shopmobile

import android.app.Application
import com.example.shopmobile.storage.AppStorage

class ShopApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppStorage.init(this)
    }
}
