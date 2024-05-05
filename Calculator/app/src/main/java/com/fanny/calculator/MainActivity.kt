package com.fanny.calculator

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.fanny.calculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnplus.setOnClickListener {
            binding.bil1.text = binding.bilEditText1.text
            binding.bil2.text = binding.bilEditText2.text
            binding.operator.text = "+"
            val num1 = binding.bilEditText1.text.toString()
            val num2 = binding.bilEditText2.text.toString()

            if (num1.isEmpty() || num2.isEmpty()) {
                binding.hasilNumber.text = "Masukkan angka terlebih dahulu"
                binding.hasilNumber.setTextColor(getColor(R.color.red))
            } else {
                plus(num1.toFloat(), num2.toFloat())
                binding.hasilNumber.setTextColor(getColor(R.color.black))
            }
        }

        binding.btnminus.setOnClickListener {
            binding.bil1.text = binding.bilEditText1.text
            binding.bil2.text = binding.bilEditText2.text
            binding.operator.text = "-"
            val num1 = binding.bilEditText1.text.toString()
            val num2 = binding.bilEditText2.text.toString()

            if (num1.isEmpty() || num2.isEmpty()) {
                binding.hasilNumber.text = "Masukkan angka terlebih dahulu"
                binding.hasilNumber.setTextColor(getColor(R.color.red))
            } else {
                minus(num1.toFloat(), num2.toFloat())
                binding.hasilNumber.setTextColor(getColor(R.color.black))
            }
        }

        binding.btnmultiply.setOnClickListener {
            binding.bil1.text = binding.bilEditText1.text
            binding.bil2.text = binding.bilEditText2.text
            binding.operator.text = "x"
            val num1 = binding.bilEditText1.text.toString()
            val num2 = binding.bilEditText2.text.toString()

            if (num1.isEmpty() || num2.isEmpty()) {
                binding.hasilNumber.text = "Masukkan angka terlebih dahulu"
                binding.hasilNumber.setTextColor(getColor(R.color.red))
            } else {
                multiply(num1.toFloat(), num2.toFloat())
                binding.hasilNumber.setTextColor(getColor(R.color.black))
            }
        }

        binding.btndivide.setOnClickListener {
            binding.bil1.text = binding.bilEditText1.text
            binding.bil2.text = binding.bilEditText2.text
            binding.operator.text = "/"
            val num1 = binding.bilEditText1.text.toString()
            val num2 = binding.bilEditText2.text.toString()

            if (num1.isEmpty() || num2.isEmpty()) {
                binding.hasilNumber.text = "Masukkan angka terlebih dahulu"
                binding.hasilNumber.setTextColor(getColor(R.color.red))
            }
            else if (num2.toFloat() == 0f) {
                binding.hasilNumber.text = "Tidak bisa membagi dengan 0"
                binding.hasilNumber.setTextColor(getColor(R.color.red))
            }
            else {
                divide(num1.toFloat(), num2.toFloat())
                binding.hasilNumber.setTextColor(getColor(R.color.black))
            }
        }
    }

    private fun plus(num1: Float, num2: Float) {
        val result = num1 + num2
        binding.hasilNumber.text = result.toString()
    }

    private fun minus(num1: Float, num2: Float) {
        val result = num1 - num2
        binding.hasilNumber.text = result.toString()
    }

    private fun multiply(num1: Float, num2: Float) {
        val result = num1 * num2
        binding.hasilNumber.text = result.toString()
    }

    private fun divide(num1: Float, num2: Float) {
        val result = num1 / num2
        binding.hasilNumber.text = result.toString()
    }
}