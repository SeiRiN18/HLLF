package com.example.pz3

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pz3.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "ПЗ 3 — Kotlin Android"

        binding.btnLevel1.setOnClickListener { go(Level1Activity::class.java) }
        binding.btnLevel2.setOnClickListener { go(Level2Activity::class.java) }
        binding.btnLevel3.setOnClickListener { go(Level3Activity::class.java) }
        binding.btnLevel4.setOnClickListener { go(Level4Activity::class.java) }
    }

    private fun go(cls: Class<*>) = startActivity(Intent(this, cls))
}
