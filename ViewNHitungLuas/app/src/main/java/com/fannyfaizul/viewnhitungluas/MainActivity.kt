package com.fannyfaizul.viewnhitungluas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var input_one: EditText
    private lateinit var btnEqual: Button
    private lateinit var input_two: EditText
    private lateinit var resultTv: TextView
    private var result = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        input_one = findViewById(R.id.input_number1)
        input_two = findViewById(R.id.input_number2)
        btnEqual = findViewById(R.id.count_button)
        resultTv = findViewById(R.id.result_num)

        input_one.setText("5")
        input_two.setText("4")
        btnEqual.setOnClickListener(this)
    }
    override fun onClick(v: View?){
        var a = input_one.text.toString().toInt()
        var b = input_two.text.toString().toInt()


        when(v?.id){
            R.id.count_button -> {
                result = a + b
            }
        }
        resultTv.text = result.toString()
    }
}