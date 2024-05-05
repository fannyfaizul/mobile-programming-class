package com.fanny.jsonparser

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fanny.jsonparser.databinding.ActivityMainBinding
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mAdapter: KontakAdapter

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAdapter = KontakAdapter()
        binding.rvKontak.adapter = mAdapter

        GlobalScope.launch {
            val data =
                httpHandler("https://gist.githubusercontent.com/Baalmart/8414268/raw/43b0e25711472de37319d870cb4f4b35b1ec9d26/contacts")

            runOnUiThread {
                mAdapter.setList(data)
            }
        }

    }
}