package com.example.testing.views.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.testing.BuildConfig
import com.example.testing.R
import com.example.testing.data.api.model.shoppingCart.CartList
import com.example.testing.databinding.RvCartBinding
import com.example.testing.views.detail.ItemDetailActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class ShoppingCartAdapter: RecyclerView.Adapter<ShoppingCartAdapter.RecyclerviewHolder>() {

    private var data = ArrayList<CartList>()
    private var checkedItemList = ArrayList<String>()
    private var isCheckedAll = false

    inner class RecyclerviewHolder(private val binding: RvCartBinding) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var firebaseAuth: FirebaseAuth
        private lateinit var db: FirebaseDatabase
        private lateinit var ref: DatabaseReference
        fun bind(cartList: CartList) {
            val localeID =  Locale("in", "ID")
            val numberFormat = NumberFormat.getCurrencyInstance(localeID)
            db = FirebaseDatabase.getInstance(BuildConfig.FIREBASE_URL)
            ref = db.getReference("users")
            firebaseAuth = FirebaseAuth.getInstance()
            binding.apply {
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
                itemCartPrice.text = numberFormat.format(cartList.minPrice)
                    .replace("Rp", "Rp ")
                    .replace(",00", "")
                itemCartQuantity.setText(cartList.quantity.toString())
                itemCartCb.isChecked = isCheckedAll

                itemCartAdd.setOnClickListener {
                    displayValue(binding.itemCartQuantity.text.toString().toInt() + 1)
                }
                itemCartReduce.setOnClickListener {
                    if (binding.itemCartQuantity.text.toString().toInt() > 1) {
                        displayValue(binding.itemCartQuantity.text.toString().toInt() - 1)
                    }
                    else {
                        displayValue(binding.itemCartQuantity.text.toString().toInt())
                    }
                }
                itemCartDelete.setOnClickListener {
                    ref.orderByChild("slug").equalTo(cartList.slug)
                        .addValueEventListener(object: ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                for (item in snapshot.children) {
                                    item.ref.removeValue()
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Log.e("onCancelled", "onCancelled", error.toException());
                            }

                        })
                }
            }
        }

        private fun displayValue(number: Int) {
            binding.itemCartQuantity.setText("$number")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerviewHolder {
        val itemBinding = RvCartBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecyclerviewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: RecyclerviewHolder, position: Int) {
        val data = this.data[position]
        holder.bind(data)

        holder.itemView.findViewById<LinearLayout>(R.id.item_cart_container).setOnClickListener {
            val i = Intent(holder.itemView.context, ItemDetailActivity::class.java)
            i.putExtra(ItemDetailActivity.ITEM_SLUG, data.slug)
            holder.itemView.context.startActivity(i)
        }
    }

    override fun getItemCount(): Int {
        return this.data.size
    }

    fun getTotalPrice(dataList: List<CartList>) : Int {
        var totalPrice = 0
        for (item in dataList) {
            totalPrice += item.minPrice!!
        }
        return totalPrice
    }

    fun cbSelectAll() {
        isCheckedAll = true
        notifyDataSetChanged()
    }

    fun cbUnselectAll() {
        isCheckedAll = false
        notifyDataSetChanged()
    }

    fun setData(dataList: List<CartList>) {
        this.data.clear()
        this.data.addAll(dataList)
        notifyDataSetChanged()
    }
}