package com.example.testing.views.adapter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.testing.BuildConfig
import com.example.testing.R
import com.example.testing.data.api.model.response.CartList
import com.example.testing.databinding.ItemCartBinding
import com.example.testing.views.cart.ShoppingCartFragmentDirections
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class ShoppingCartAdapter(
    private val onRetrieveData: OnRetrieveData
): RecyclerView.Adapter<ShoppingCartAdapter.RecyclerviewHolder>() {

    interface OnRetrieveData {
        fun checkedItemsSize (size: Int, price: Int)
    }

    private var data = ArrayList<CartList>()
    private var isCheckedAll = false
    private var itemQuantityTotal = 0
    private var itemPriceTotal = 0
    private var uid = ""
    private var db = FirebaseDatabase.getInstance(BuildConfig.FIREBASE_URL)
    private var ref = db.getReference("users")

    inner class RecyclerviewHolder(
        private val binding: ItemCartBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var firebaseAuth: FirebaseAuth
        fun bind(cartList: CartList) {
            val localeID =  Locale("in", "ID")
            val numberFormat = NumberFormat.getCurrencyInstance(localeID)

            val tempQty = cartList.quantity
            val tempPrice = tempQty!! * cartList.minPrice!!
            var value: Int
            firebaseAuth = FirebaseAuth.getInstance()
            binding.apply {
                Log.d("tes qty awal", itemCartQuantity.text.toString())
                Picasso.get()
                    .load(cartList.thumbnailUrl)
                    .placeholder(R.drawable.ic_image_placeholder)
                    .fit()
                    .into(itemCartThumbnail)
                if (cartList.name?.contains(" - ") == true) {
                    itemCartName.text = cartList.name
                        .substring(0, cartList.name.indexOf("-"))
                }
                else {
                    itemCartName.text = cartList.name
                }
                if (cartList.quantity > 1) {
                    Glide.with(itemCartReduce.context)
                        .load(R.drawable.ic_minus)
                        .into(itemCartReduce)
                    itemCartReduce.isEnabled = true
                }
                else {
                    Glide.with(itemCartReduce.context)
                        .load(R.drawable.ic_minus_grey)
                        .into(itemCartReduce)
                    itemCartReduce.isEnabled = false
                }
                itemCartPrice.text = numberFormat.format(cartList.minPrice)
                    .replace("Rp", "Rp ")
                    .replace(",00", "")
                itemCartQuantity.setText(cartList.quantity.toString())
                itemCartCb.isChecked = isCheckedAll
                itemCartCb.setOnCheckedChangeListener { p0, isChecked ->

                    if (isChecked) {
                        itemQuantityTotal += tempQty!!
                        itemPriceTotal += tempPrice
                        onRetrieveData.checkedItemsSize(
                            itemQuantityTotal,
                            itemPriceTotal
                        )
                    } else {
                        itemQuantityTotal -= tempQty!!
                        itemPriceTotal -= tempPrice
                        onRetrieveData.checkedItemsSize(
                            itemQuantityTotal,
                            itemPriceTotal
                        )
                    }
//                    ref.child(cartList.slug!!)
//                        .addListenerForSingleValueEvent(object : ValueEventListener {
//                            override fun onDataChange(snapshot: DataSnapshot) {
//                                val itemData = snapshot.getValue(CartList::class.java)
//                                Log.d("shoppingData", itemData.toString())
//                                value = binding.itemCartQuantity.text.toString().toInt()
//                                if (itemData != null) {
//                                    val tempPrice = itemData.quantity!! * itemData.minPrice!!
////                                    val tempPrice = itemData.quantity?.times(itemData.minPrice!!)!!
//                                    if (isChecked) {
//                                        itemQuantityTotal += itemData.quantity
//                                        itemPriceTotal += tempPrice
//                                        onRetrieveData.checkedItemsSize(
//                                            itemQuantityTotal,
//                                            itemPriceTotal
//                                        )
//                                    } else {
//                                        itemQuantityTotal -= itemData.quantity
//                                        itemPriceTotal -= tempPrice
//                                        onRetrieveData.checkedItemsSize(
//                                            itemQuantityTotal,
//                                            itemPriceTotal
//                                        )
//                                    }
//                                }
//                            }
//
//                            override fun onCancelled(error: DatabaseError) {
//                                TODO("Not yet implemented")
//                            }
//
//                        })
                }

                itemCartAdd.setOnClickListener {
                    val plusValue = tempQty + 1
                    itemCartCb.isChecked = true
                    value = binding.itemCartQuantity.text.toString().toInt().coerceAtLeast(1)
                    itemQuantityTotal += 1
                    itemPriceTotal += cartList.minPrice
                    Log.d("tes label plus", itemCartQuantity.text.toString())
                    displayValue(plusValue)
                    onRetrieveData.checkedItemsSize(itemQuantityTotal, itemPriceTotal)
                    cartList.slug?.let { slug ->
                        ref.child(uid).child("cart").child(slug).updateChildren(hashMapOf<String, Any>(
                            "quantity" to plusValue
                        ))
                    }
                }
                itemCartReduce.setOnClickListener {
                    val minusValue = tempQty - 1
                    itemCartCb.isChecked = true
                    value = binding.itemCartQuantity.text.toString().toInt().coerceAtLeast(1)
                    itemQuantityTotal -= 1
                    itemPriceTotal -= cartList.minPrice
                    Log.d("tes label minus", itemCartQuantity.text.toString())
                    displayValue(minusValue)
                    onRetrieveData.checkedItemsSize(itemQuantityTotal, itemPriceTotal)
                    cartList.slug?.let { slug ->
                        ref.child(uid).child("cart").child(slug).updateChildren(hashMapOf<String, Any>(
                            "quantity" to minusValue
                        ))
                    }
                }
                itemCartDelete.setOnClickListener {
                    ref.child(uid).child("cart").orderByChild("slug").equalTo(cartList.slug)
                        .addListenerForSingleValueEvent(object: ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                for (item in snapshot.children) {
                                    item.ref.removeValue()
                                    this@ShoppingCartAdapter.data.removeAt(adapterPosition)
                                }
                                notifyItemRemoved(adapterPosition)
                                notifyItemRangeChanged(adapterPosition, this@ShoppingCartAdapter.data.size)
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Log.e("onCancelled", "onCancelled", error.toException())
                            }

                        })
                }
                itemCartContainer.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putString("slug", cartList.slug)
                    itemView.findNavController().navigate(R.id.shopping_to_detail, bundle)
                }
            }
        }

        private fun displayValue(number: Int) {
            binding.itemCartQuantity.setText("$number")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerviewHolder {
        val itemBinding = ItemCartBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecyclerviewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: RecyclerviewHolder, position: Int) {
        val data = this.data[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int {
        return this.data.size
    }

    fun getSelectAllState(state: Boolean) {
        isCheckedAll = state
        notifyDataSetChanged()
    }

    fun setData(dataList: List<CartList>) {
        this.data.clear()
        this.data.addAll(dataList)
        notifyDataSetChanged()
    }

    fun setUid(uid: String) {
        this.uid = uid
        notifyDataSetChanged()
    }
}