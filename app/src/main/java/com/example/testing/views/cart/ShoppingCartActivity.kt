package com.example.testing.views.cart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.CompoundButton
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testing.R
import com.example.testing.BuildConfig
import com.example.testing.data.api.model.shoppingCart.CartList
import com.example.testing.databinding.ActivityShoppingCartBinding
import com.example.testing.views.adapter.ShoppingCartAdapter
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import java.text.NumberFormat
import java.util.*

class ShoppingCartActivity : AppCompatActivity(), ShoppingCartAdapter.OnRetrieveData {
    private lateinit var binding: ActivityShoppingCartBinding
    private lateinit var cartAdapter: ShoppingCartAdapter
    private lateinit var db: FirebaseDatabase
    private lateinit var ref: DatabaseReference
    private var itemQuantity = 0
    private var itemPrice = 0
    private val dataList : MutableList<CartList> = arrayListOf()

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
        db = FirebaseDatabase.getInstance(BuildConfig.FIREBASE_URL)
        ref = db.getReference("users")
        cartAdapter = ShoppingCartAdapter(this)
        binding.cartRv.apply {
            layoutManager = LinearLayoutManager(
                this@ShoppingCartActivity,
                LinearLayoutManager.VERTICAL,
                false)
            adapter = cartAdapter
        }
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val localeID =  Locale("in", "ID")
                val numberFormat = NumberFormat.getCurrencyInstance(localeID)
                var tempQuantity = 0
                var tempPrice = 0
                for (ds in snapshot.children) {
                    val data = ds.getValue<CartList>()
                    if (data != null) {
                        dataList.add(data)
                        tempQuantity += data.quantity!!
                        tempPrice += data.minPrice?.times(data.quantity)!!
                    }
                    Log.d("temp", tempQuantity.toString())
                    itemQuantity = tempQuantity
                    itemPrice = tempPrice
                }
                cartAdapter.apply {
                    setData(dataList)
                    itemPrice = getTotalPrice(dataList)
                    binding.apply {
                        cartCbSelectAll.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
                            override fun onCheckedChanged(p0: CompoundButton?, isChecked: Boolean) {
                                if (isChecked) {
                                    cartAdapter.getSelectAllState(true)
                                    cartBtnCheckout.text = "Beli (${itemQuantity})"
                                    cartTotalPrice.text = numberFormat.format(itemPrice)
                                        .replace("Rp", "Rp ")
                                        .replace(",00", "")
                                }
                                else {
                                    cartAdapter.getSelectAllState(false)
                                    binding.cartBtnCheckout.text = "Beli (0)"
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

    override fun checkedItemsSize(size: Int, price: Int) {
        val localeID =  Locale("in", "ID")
        val numberFormat = NumberFormat.getCurrencyInstance(localeID)
        if (size > 0 && price > 0) {
            itemQuantity = size
            itemPrice = price
            binding.cartBtnCheckout.text = "Beli (${itemQuantity})"
            binding.cartTotalPrice.text = numberFormat.format(itemPrice)
                .replace("Rp", "Rp ")
                .replace(",00", "")
            Log.d("size", size.toString())
        }
        if (price > 0) {
            itemPrice = price
            binding.cartTotalPrice.text = numberFormat.format(itemPrice)
                .replace("Rp", "Rp ")
                .replace(",00", "")
        }
        else {
            binding.cartBtnCheckout.text = "Beli (0)"
            binding.cartTotalPrice.setText(R.string.product_price)
        }
    }

    private fun getTotalPrice(dataList: List<CartList>) : Int {
        var totalPrice = 0
        for (item in dataList) {
            totalPrice += item.minPrice!!
        }
        return totalPrice
    }

}