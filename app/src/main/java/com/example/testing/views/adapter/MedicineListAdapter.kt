package com.example.testing.views.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.testing.R
import com.example.testing.databinding.RvHomeBinding
import com.example.testing.data.api.model.medicine.MedicineList
import com.example.testing.views.detail.ItemDetailActivity
import com.squareup.picasso.Picasso
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.min

class MedicineListAdapter : RecyclerView.Adapter<MedicineListAdapter.RecyclerviewHolder>() {

    private var data = ArrayList<MedicineList>()

    inner class RecyclerviewHolder (private val binding: RvHomeBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(medicineList: MedicineList) {
            val localeID =  Locale("in", "ID")
            val numberFormat = NumberFormat.getCurrencyInstance(localeID)
            Picasso.get()
                .load(medicineList.thumbnailUrl)
                .placeholder(R.drawable.ic_image_placeholder)
                .fit()
                .into(binding.productImg)
            if (medicineList.name.contains(" - ")) {
                binding.productName.text = medicineList.name
                    .substring(0, medicineList.name.indexOf("-"))
            }
            else {
                binding.productName.text = medicineList.name
            }
            binding.productPrice.text = numberFormat.format(medicineList.minPrice)
                .replace("Rp", "Rp ")
                .replace(",00", "")
//            binding.productPrice.text = "%,d".format(result.minPrice)
            if (medicineList.visualCues.contains("in_stock")) {
                binding.productStatus.text = "Tersedia"
            }
            else {
                binding.productStatus.text = "Stok Habis"
            }
            binding.productSellUnit.text = medicineList.sellingUnit.replace("Per ", "/")
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

        holder.itemView.setOnClickListener{
            val i = Intent(holder.itemView.context, ItemDetailActivity::class.java)
            i.putExtra(ItemDetailActivity.ITEM_SLUG, data.slug)
            holder.itemView.context.startActivity(i)
        }
    }

    override fun getItemCount(): Int {
        return min(this.data.size, 8)
    }

    fun setData(datalist: List<MedicineList>) {
        this.data.clear()
        this.data.addAll(datalist)
        notifyDataSetChanged()
    }
}