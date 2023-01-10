package com.example.testing.views.cart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testing.R
import com.example.testing.data.api.model.shoppingCart.CartList
import com.example.testing.data.api.model.shoppingCart.UserCart
import com.example.testing.databinding.ActivityShoppingCartBinding
import com.example.testing.views.adapter.ShoppingCartAdapter

class ShoppingCartActivity : AppCompatActivity() {

//    companion object {
//        const val ITEM_SLUG = "item_slug"
//    }
    private lateinit var binding: ActivityShoppingCartBinding
    private lateinit var cartAdapter: ShoppingCartAdapter
    private var data: List<CartList> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShoppingCartBinding.inflate(layoutInflater)
        setupToolbar()
        setupRecycler()
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

    private fun setupRecycler() {
        cartAdapter = ShoppingCartAdapter()

        binding.cartRv.apply {
            layoutManager = LinearLayoutManager(
                this@ShoppingCartActivity,
                LinearLayoutManager.VERTICAL,
                false)
            adapter = cartAdapter
        }
        Log.d("test2", data.toString())
//        cartAdapter.setData(data)
    }
}