package com.example.testing.views.cart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.CompoundButton
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testing.R
import com.example.testing.data.api.model.shoppingCart.CartList
import com.example.testing.databinding.ActivityShoppingCartBinding
import com.example.testing.views.adapter.ShoppingCartAdapter
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import java.text.NumberFormat
import java.util.*

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
                val localeID =  Locale("in", "ID")
                val numberFormat = NumberFormat.getCurrencyInstance(localeID)
                for (ds in snapshot.children) {
                    val data = ds.getValue<CartList>()
                    if (data != null) {
                        dataList.add(data)
                    }
                }
                Log.d("yoi", dataList.toString())
                cartAdapter.apply {
                    setData(dataList)
                    val totalPrice = getTotalPrice(dataList)
                    binding.apply {
                        cartCbSelectAll.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
                            override fun onCheckedChanged(p0: CompoundButton?, isChecked: Boolean) {
                                if (isChecked) {
                                    cartAdapter.cbSelectAll()
                                    cartTotalPrice.text = numberFormat.format(totalPrice)
                                        .replace("Rp", "Rp ")
                                        .replace(",00", "")
                                }
                                else {
                                    cartAdapter.cbUnselectAll()
                                    cartTotalPrice.setText(R.string.product_price)
                                }
                            }
                        })
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("valListenerCancelled", "loadPost:onCancelled", error.toException())
            }
        })
    }
}