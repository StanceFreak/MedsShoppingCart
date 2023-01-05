package com.example.testing.views.cart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.testing.R
import com.example.testing.databinding.ActivityShoppingCartBinding

class ShoppingCartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShoppingCartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShoppingCartBinding.inflate(layoutInflater)
        setupToolbar()
        setContentView(binding.root)
    }

    private fun setupToolbar() {
        binding.cartToolbar.apply {
            setNavigationIcon(R.drawable.ic_back)
        }
        setSupportActionBar(binding.cartToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.cartToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}