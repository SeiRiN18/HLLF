package com.example.pz3

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pz3.databinding.ActivityLevel2Binding

class Level2Activity : AppCompatActivity() {
    private lateinit var binding: ActivityLevel2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLevel2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Рівень 2 — Калькулятор"

        binding.btnPlus.setOnClickListener  { calculate('+') }
        binding.btnMinus.setOnClickListener { calculate('-') }
        binding.btnMul.setOnClickListener   { calculate('*') }
        binding.btnDiv.setOnClickListener   { calculate('/') }
    }

    private fun calculate(op: Char) {
        val a = binding.etNum1.text.toString().toDoubleOrNull()
        val b = binding.etNum2.text.toString().toDoubleOrNull()

        if (a == null || b == null) {
            Toast.makeText(this, "Введіть коректні числа", Toast.LENGTH_SHORT).show()
            return
        }
        if (op == '/' && b == 0.0) {
            Toast.makeText(this, "Помилка: ділення на нуль!", Toast.LENGTH_SHORT).show()
            return
        }

        val result = when (op) {
            '+' -> a + b
            '-' -> a - b
            '*' -> a * b
            '/' -> a / b
            else -> return
        }

        val opStr = when (op) { '+' -> "+" ; '-' -> "−" ; '*' -> "×" ; '/' -> "÷" ; else -> "$op" }
        val fmt   = if (result % 1.0 == 0.0) result.toLong().toString() else "%.6f".format(result).trimEnd('0')
        binding.tvResult.text = "${fmt(a)} $opStr ${fmt(b)} = $fmt"
    }

    private fun fmt(d: Double) =
        if (d % 1.0 == 0.0) d.toLong().toString() else d.toString()
}
