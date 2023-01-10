package com.example.testing.views.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.testing.R
import com.example.testing.data.api.model.shoppingCart.CartList
import com.example.testing.databinding.RvCartBinding
import com.example.testing.views.detail.ItemDetailActivity
import com.squareup.picasso.Picasso
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class ShoppingCartAdapter: RecyclerView.Adapter<ShoppingCartAdapter.RecyclerviewHolder>() {

    private var data = ArrayList<CartList>()

    inner class RecyclerviewHolder(private val binding: RvCartBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cartList: CartList) {
            val localeID =  Locale("in", "ID")
            val numberFormat = NumberFormat.getCurrencyInstance(localeID)
            binding.apply {
                Picasso.get()
                    .load(cartList.thumbnailUrl)
                    .placeholder(R.drawable.ic_image_placeholder)
                    .fit()
                    .into(itemCartThumbnail)
                if (cartList.name.contains(" - ")) {
                    itemCartName.text = cartList.name
                        .substring(0, cartList.name.indexOf("-"))
                }
                else {
                    itemCartName.text = cartList.name
                }
                itemCartPrice.text = numberFormat.format(cartList.minPrice)
                    .replace("Rp", "Rp ")
                    .replace(",00", "")
                itemCartQuantity.setText(cartList.quantity)
            }
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

        holder.itemView.setOnClickListener{
            val i = Intent(holder.itemView.context, ItemDetailActivity::class.java)
            i.putExtra(ItemDetailActivity.ITEM_SLUG, data.slug)
            holder.itemView.context.startActivity(i)
        }
    }

    override fun getItemCount(): Int {
        return this.data.size
    }

    fun setData(dataList: List<CartList>) {
        this.data.clear()
        this.data.addAll(dataList)
        notifyDataSetChanged()
    }
}