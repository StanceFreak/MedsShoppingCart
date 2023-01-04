package com.example.testing.views.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.testing.R
import com.example.testing.databinding.RvHomeBinding
import com.example.testing.data.api.model.Result
import com.squareup.picasso.Picasso
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.min

class MedicineListAdapter : RecyclerView.Adapter<MedicineListAdapter.RecyclerviewHolder>() {

    private var data = ArrayList<Result>()

    inner class RecyclerviewHolder (private val binding: RvHomeBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(result: Result) {
            val localeID =  Locale("in", "ID")
            val numberFormat = NumberFormat.getCurrencyInstance(localeID)
            Picasso.get()
                .load(result.thumbnailUrl)
                .placeholder(R.drawable.ic_image_placeholder)
                .fit()
                .into(binding.productImg)
            binding.productName.text = result.name
            binding.productPrice.text = numberFormat.format(result.minPrice)
                .replace("Rp", "Rp ")
                .replace(",00", "")
//            binding.productPrice.text = "%,d".format(result.minPrice)
            if (result.visualCues.contains("in_stock")) {
                binding.productStatus.text = "Tersedia"
            }
            else {
                binding.productStatus.text = "Stok Habis"
            }
            binding.productSellUnit.text = result.sellingUnit.replace("Per ", "/")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerviewHolder {
        val itemBinding = RvHomeBinding.inflate(
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
        return min(this.data.size, 8)
    }

    fun setData(datalist: List<Result>) {
        this.data.clear()
        this.data.addAll(datalist)
        notifyDataSetChanged()
    }
}