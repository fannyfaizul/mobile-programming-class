package com.fanny.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.fanny.dialog.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnDialog.setOnClickListener {
            tampil_input_dialog()
        }
    }

    private fun tampil_input_dialog() {
        val li = LayoutInflater.from(this);
        val inputnya = li.inflate(R.layout.input_dialog, null);

        val dialognya = AlertDialog.Builder(this);
        dialognya.setView(inputnya);

        dialognya
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, id ->
                val inputan = inputnya.findViewById<android.widget.EditText>(R.id.etInput).text.toString()

                binding.tvResult.text = inputan
            }
            .setNegativeButton("Batal") { dialog, id ->
                dialog.cancel()
            }
        dialognya.show();
    }
}