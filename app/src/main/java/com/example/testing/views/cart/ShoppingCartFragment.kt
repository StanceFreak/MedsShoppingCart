package com.example.testing.views.cart

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testing.BuildConfig
import com.example.testing.R
import com.example.testing.data.api.model.response.CartList
import com.example.testing.databinding.FragmentShoppingCartBinding
import com.example.testing.views.adapter.ShoppingCartAdapter
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import org.koin.android.ext.android.inject
import java.text.NumberFormat
import java.util.*

class ShoppingCartFragment : Fragment(), ShoppingCartAdapter.OnRetrieveData {
    private lateinit var binding: FragmentShoppingCartBinding
    private lateinit var cartAdapter: ShoppingCartAdapter
    private lateinit var db: FirebaseDatabase
    private lateinit var ref: DatabaseReference
    private val viewModel: ShoppingCartViewModel by inject()
    private var totalQty = 0
    private var totalPrice = 0
    private var checkedList = ArrayList<String>()
    private val dataList : MutableList<CartList> = arrayListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupToolbar()
        setupRecycler()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShoppingCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setupToolbar() {
        binding.apply {
            cartToolbar.setNavigationIcon(R.drawable.ic_back)
            (activity as AppCompatActivity).apply {
                setSupportActionBar(binding.cartToolbar)
                title = ""
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                cartToolbar.setNavigationOnClickListener {
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        }
    }

    private fun setupRecycler() {
        var tempQuantity = 0
        var tempPrice = 0
        db = FirebaseDatabase.getInstance(BuildConfig.FIREBASE_URL)
        ref = db.getReference("users")
        cartAdapter = ShoppingCartAdapter(this)
        binding.apply {
            cartRv.apply {
                layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.VERTICAL,
                    false)
                adapter = cartAdapter
                isNestedScrollingEnabled = false
            }
            viewModel.getUserData().observe(viewLifecycleOwner) { uid ->
                sfCartContentLoading.isGone = false
                clCartContentContainer.isGone = true
                ref.child(uid).child("cart").addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        sfCartContentLoading.isGone = true
                        clCartContentContainer.isGone = false
                        if (snapshot.exists()) {
                            val localeID =  Locale("in", "ID")
                            val numberFormat = NumberFormat.getCurrencyInstance(localeID)
                            cartAdapter.setUid(uid)
                            dataList.clear()
                            for (ds in snapshot.children) {
                                val data = ds.getValue<CartList>()
                                if (data != null) {
                                    dataList.add(data)
                                    tempQuantity += data.quantity!!
                                    tempPrice += data.minPrice?.times(data.quantity)!!
                                }
                                Log.d("temp", tempQuantity.toString())
                                totalQty = tempQuantity
                                totalPrice = tempPrice
                            }
                            cartAdapter.apply {
                                Log.d("tes fb", dataList.toString())
                                Log.d("tes fb size", dataList.size.toString())
                                setData(dataList)
                                binding.apply {
                                    cartCbSelectAll.setOnCheckedChangeListener { _, isChecked ->
                                        if (isChecked) {
                                            for (i in 0 until dataList.size) {
                                                checkedList.add(dataList[i].slug!!)
                                            }
                                            Log.d("tes value list add all", checkedList.toString())
                                            totalQty = dataList.sumOf { it.quantity!! }
                                            totalPrice = getTotalPrice(dataList)
                                            Log.d("tes value", "$totalQty & $totalPrice")
                                            cartAdapter.getSelectAllState(true, checkedList, totalQty, totalPrice)
                                            cartBtnCheckout.text = "Beli ($totalQty)"
                                            cartTotalPrice.text = numberFormat.format(totalPrice)
                                                .replace("Rp", "Rp ")
                                                .replace(",00", "")
                                        } else {
                                            checkedList.clear()
                                            Log.d("tes value list remove all", checkedList.toString())
                                            cartAdapter.getSelectAllState(false, checkedList, 0, 0)
                                            binding.cartBtnCheckout.text = "Beli (0)"
                                            binding.cartTotalPrice.text = "-"
                                        }
                                    }
                                }
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.d("valListenerCancelled", "loadPost:onCancelled", error.toException())
                    }
                })
            }
        }

    }

    override fun checkedItems(qty: Int, price: Int) {
        val localeID =  Locale("in", "ID")
        val numberFormat = NumberFormat.getCurrencyInstance(localeID)
        if (qty > 0 && price > 0) {
            totalQty = qty
            totalPrice = price
            binding.cartBtnCheckout.text = "Beli (${totalQty})"
            binding.cartTotalPrice.text = numberFormat.format(totalPrice)
                .replace("Rp", "Rp ")
                .replace(",00", "")
        }
        else {
            binding.cartBtnCheckout.text = "Beli (0)"
            binding.cartTotalPrice.text = "-"
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