package com.example.testing.views.cart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testing.R
import com.example.testing.data.api.model.shoppingCart.CartList
import com.example.testing.databinding.ActivityShoppingCartBinding
import com.example.testing.views.adapter.ShoppingCartAdapter
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue

class ShoppingCartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShoppingCartBinding
    private lateinit var cartAdapter: ShoppingCartAdapter
    private lateinit var db: FirebaseDatabase
    private lateinit var ref: DatabaseReference

    companion object {
        const val FIREBASE_URL = "https://medsshoppingcart-default-rtdb.asia-southeast1.firebasedatabase.app/"
    }

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
        db = FirebaseDatabase.getInstance(FIREBASE_URL)
        ref = db.getReference("users")
        cartAdapter = ShoppingCartAdapter()
        binding.cartRv.apply {
            layoutManager = LinearLayoutManager(
                this@ShoppingCartActivity,
                LinearLayoutManager.VERTICAL,
                false)
            adapter = cartAdapter
        }
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataList : MutableList<CartList> = arrayListOf()
                for (ds in snapshot.children) {
                    val data = ds.getValue<CartList>()
                    if (data != null) {
                        dataList.add(data)
                    }
                }
//                val data = snapshot.getValue<List<CartList>>()
                Log.d("yoi", dataList.toString())
                cartAdapter.setData(dataList)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
//        Log.d("test2", )
//        cartAdapter.setData(data)
    }
}