package com.example.testing.views.adapter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.testing.BuildConfig
import com.example.testing.R
import com.example.testing.data.api.model.response.CartList
import com.example.testing.databinding.ItemCartBinding
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
        fun checkedItems (qty: Int, price: Int)
    }

    private var data = ArrayList<CartList>()
    private var checkedList = ArrayList<String>()
    private var isCheckedAll = false
    private var itemQuantityTotal = 0
    private var itemPriceTotal = 0
    private var cbCheckedTotal = 0
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
                Picasso.get()
                    .load(cartList.thumbnailUrl)
                    .placeholder(R.drawable.ic_image_placeholder)
                    .fit()
                    .into(itemCartThumbnail)
                // indexing the item name if the name is containing the particular string
                if (cartList.name?.contains(" - ") == true) {
                    itemCartName.text = cartList.name
                        .substring(0, cartList.name.indexOf("-"))
                }
                else {
                    itemCartName.text = cartList.name
                }
                // check the item qty to decide the state of the minus button
                // if qty is > 1, the minus button state is true
                // if qty is 1 or 0, the minus button state is false
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
                // formatting the item price using IDR currency (Rp)
                itemCartPrice.text = numberFormat.format(cartList.minPrice)
                    .replace("Rp", "Rp ")
                    .replace(",00", "")
                itemCartQuantity.setText(cartList.quantity.toString())

                if (checkedList.size != 0) {
                    // save checkbox state to prevent the auto uncheck issue
                    itemCartCb.isChecked = checkedList.contains(cartList.slug)
                    // check if total checkbox checked has same value with checked list size
                    // if yes, check all the checkbox on the rv
                    if (cbCheckedTotal == checkedList.size) {
                        itemCartCb.isChecked = isCheckedAll
                    }
                }
                // if the checked list size == 0 or the check all checkbox state is false
                // uncheck all of the checkbox
                else {
                    itemCartCb.isChecked = false
                }
                itemCartCb.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        // if the checkbox is checked
                        // the item slug will be added to the list for saving the state
                        cartList.slug?.let { checkedList.add(it) }
                        // the total checkbox checked value will be inrement by 1
                        cbCheckedTotal++
                        // the item tempQty value will be added to the itemQuantityTotal
                        itemQuantityTotal += tempQty!!
                        // the item tempPrice value will be added to the itemPriceTotal
                        itemPriceTotal += tempPrice
                        // and then the both value of itemQuantityTotal & itemPriceTotal
                        // will be passed to the HomeFragment
                        onRetrieveData.checkedItems(
                            itemQuantityTotal,
                            itemPriceTotal
                        )
                    } else {
                        // if the checkbox is unchecked
                        // the item slug will be remove from the list
                        cartList.slug?.let { checkedList.remove(it) }
                        // the total checkbox checked value will be decrement by 1
                        cbCheckedTotal--
                        // the item tempQty value will be removed from the itemQuantityTotal
                        itemQuantityTotal -= tempQty!!
                        // the item tempPrice value will be removed from the itemPriceTotal
                        itemPriceTotal -= tempPrice
                        // and then the both value of itemQuantityTotal & itemPriceTotal
                        // will be passed to the HomeFragment
                        onRetrieveData.checkedItems(
                            itemQuantityTotal,
                            itemPriceTotal
                        )
                    }
                }

                itemCartAdd.setOnClickListener {
                    // create new value for storing the quantity addition by 1 to the firebase
                    val plusValue = tempQty + 1
                    // if the initial checkbox state is false, change the state to true
                    itemCartCb.isChecked = true
                    Log.d("tes value cb +", itemCartCb.isChecked.toString())
                    value = binding.itemCartQuantity.text.toString().toInt().coerceAtLeast(1)
                    // add itemQuantityTotal value by 1
                    itemQuantityTotal += 1
                    // add itemPriceTotal value by 1 item price
                    itemPriceTotal += cartList.minPrice
                    Log.d("tes label plus", itemCartQuantity.text.toString())
                    // function for displaying the updated quantity after addition
                    displayValue(plusValue)
                    // the both value of itemQuantityTotal & itemPriceTotal
                    // will be passed to the HomeFragment
                    onRetrieveData.checkedItems(itemQuantityTotal, itemPriceTotal)
                    // and then the quantity value will be send to the firebase realtime database
                    // to update the current quantity value
                    cartList.slug?.let { slug ->
                        ref.child(uid).child("cart").child(slug).updateChildren(hashMapOf<String, Any>(
                            "quantity" to plusValue
                        ))
                    }
                }
                itemCartReduce.setOnClickListener {
                    // create new value for storing the quantity subtraction by 1 to the firebase
                    val minusValue = tempQty - 1
                    // if the initial checkbox state is false, change the state to true
                    itemCartCb.isChecked = true
                    Log.d("tes value cb -", itemCartCb.isChecked.toString())
                    value = binding.itemCartQuantity.text.toString().toInt().coerceAtLeast(1)
                    // subtract itemQuantityTotal value by 1
                    itemQuantityTotal -= 1
                    // subtract itemPriceTotal value by 1 item price
                    itemPriceTotal -= cartList.minPrice
                    Log.d("tes label minus", itemCartQuantity.text.toString())
                    // function for displaying the updated quantity after subtraction
                    displayValue(minusValue)
                    // the both value of itemQuantityTotal & itemPriceTotal
                    // will be passed to the HomeFragment
                    onRetrieveData.checkedItems(itemQuantityTotal, itemPriceTotal)
                    // and then the quantity value will be send to the firebase realtime database
                    // to update the current quantity value
                    cartList.slug?.let { slug ->
                        ref.child(uid).child("cart").child(slug).updateChildren(hashMapOf<String, Any>(
                            "quantity" to minusValue
                        ))
                    }
                }
                itemCartDelete.setOnClickListener {
                    // check if the item is exist in firebase realtime database based on the item slug
                    ref.child(uid).child("cart").orderByChild("slug").equalTo(cartList.slug)
                        .addListenerForSingleValueEvent(object: ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                // if exist, look for the item ref and delete form database
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

    override fun onViewRecycled(holder: RecyclerviewHolder) {
        val cb = holder.itemView.findViewById<CheckBox>(R.id.item_cart_cb)
        cb.setOnCheckedChangeListener(null)
        super.onViewRecycled(holder)
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

    fun getSelectAllState(state: Boolean, checkedList: List<String>, totalQty: Int, totalPrice: Int) {
        isCheckedAll = state
        this.itemQuantityTotal = totalQty
        this.itemPriceTotal = totalPrice
        this.checkedList.apply {
            clear()
            addAll(checkedList)
        }
        notifyDataSetChanged()
    }

    fun setData(dataList: List<CartList>) {
        this.data.clear()
        this.data.addAll(dataList)
        Log.d("tes size data", dataList.size.toString())
        notifyDataSetChanged()
    }

    fun setUid(uid: String) {
        this.uid = uid
        notifyDataSetChanged()
    }
}