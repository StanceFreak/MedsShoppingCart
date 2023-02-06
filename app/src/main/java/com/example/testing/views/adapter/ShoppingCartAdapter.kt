package com.example.testing.views.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.testing.BuildConfig
import com.example.testing.R
import com.example.testing.data.api.model.shoppingCart.CartList
import com.example.testing.databinding.RvCartBinding
import com.example.testing.views.detail.ItemDetailActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
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
    private var itemQuantity = 0
    private var itemPrice = 0
    private var tempPrice = 0
    private var tempQuantity = 0
    private var value = 0
    private var db = FirebaseDatabase.getInstance(BuildConfig.FIREBASE_URL)
    private var ref = db.getReference("users")

    inner class RecyclerviewHolder(
        private val binding: RvCartBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var firebaseAuth: FirebaseAuth
        fun bind(cartList: CartList) {
            val localeID =  Locale("in", "ID")
            val numberFormat = NumberFormat.getCurrencyInstance(localeID)
            val etValue = binding.itemCartQuantity.text.toString().toInt()
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
                itemCartCb.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
                    override fun onCheckedChanged(p0: CompoundButton?, isChecked: Boolean) {
                        tempPrice = cartList.quantity?.let { cartList.minPrice?.times(it) }!!
                        if (isChecked) {
                            itemQuantity += cartList.quantity
                            itemPrice += tempPrice
                            onRetrieveData.checkedItemsSize(itemQuantity, itemPrice)
                        }
                        else {
                            itemQuantity -= cartList.quantity
                            itemPrice -= tempPrice
                            onRetrieveData.checkedItemsSize(itemQuantity, itemPrice)
                        }
                    }
                })

                itemCartAdd.setOnClickListener {
                    value = binding.itemCartQuantity.text.toString().toInt().plus(1).coerceAtLeast(1)
                    displayValue(value)
                    cartList.slug?.let { it1 ->
                        ref.child(cartList.slug).updateChildren(hashMapOf<String, Any>(
                            "quantity" to value
                        ))
                    }
                }
                itemCartReduce.setOnClickListener {
                    value = binding.itemCartQuantity.text.toString().toInt().minus(1).coerceAtLeast(1)
                    displayValue(value)
                    cartList.slug?.let { it1 ->
                        ref.child(cartList.slug).updateChildren(hashMapOf<String, Any>(
                            "quantity" to value
                        ))
                    }
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
        db = FirebaseDatabase.getInstance(BuildConfig.FIREBASE_URL)
        ref = db.getReference("users")
        val data = this.data[position]
        holder.bind(data)

        holder.itemView.apply {
            findViewById<ImageView>(R.id.item_cart_delete).setOnClickListener {
                ref.orderByChild("slug").equalTo(data.slug)
                    .addListenerForSingleValueEvent(object: ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (item in snapshot.children) {
                                item.ref.removeValue()
                                this@ShoppingCartAdapter.data.removeAt(holder.adapterPosition)
                            }
                            notifyItemRemoved(holder.adapterPosition)
                            notifyItemRangeChanged(holder.adapterPosition, this@ShoppingCartAdapter.data.size)
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Log.e("onCancelled", "onCancelled", error.toException())
                        }

                    })
            }
            findViewById<LinearLayout>(R.id.item_cart_container).setOnClickListener {
                val i = Intent(holder.itemView.context, ItemDetailActivity::class.java)
                i.putExtra(ItemDetailActivity.ITEM_SLUG, data.slug)
                holder.itemView.context.startActivity(i)
            }
        }
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
}