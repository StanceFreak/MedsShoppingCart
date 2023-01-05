package com.example.testing.views.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.testing.R
import com.example.testing.databinding.ActivityItemDetailBinding

class ItemDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityItemDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemDetailBinding.inflate(layoutInflater)
        setupToolbar()
        setContentView(binding.root)
    }

    private fun setupToolbar() {
        binding.itemDetailToolbar.apply {
            setNavigationIcon(R.drawable.ic_back)
        }
        setSupportActionBar(binding.itemDetailToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.itemDetailToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}