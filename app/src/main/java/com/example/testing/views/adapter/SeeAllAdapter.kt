package com.example.testing.views.adapter

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.AbsoluteSizeSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isGone
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.testing.R
import com.example.testing.data.api.model.response.MedicineList
import com.example.testing.databinding.ItemChildHomeMedicineBinding
import com.example.testing.utils.CustomTypefaceSpan
import com.squareup.picasso.Picasso
import java.text.NumberFormat
import java.util.Locale
import kotlin.collections.ArrayList
import kotlin.math.min

class SeeAllAdapter : PagingDataAdapter<MedicineList, SeeAllAdapter.RecyclerviewHolder>(ItemComparator) {

    object ItemComparator : DiffUtil.ItemCallback<MedicineList>() {
        override fun areItemsTheSame(
            oldItem: MedicineList,
            newItem: MedicineList
        ): Boolean {
            return oldItem.slug == newItem.slug
        }

        override fun areContentsTheSame(
            oldItem: MedicineList,
            newItem: MedicineList
        ): Boolean {
            return oldItem == newItem
        }

    }

    inner class RecyclerviewHolder (private val binding: ItemChildHomeMedicineBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data: MedicineList) {
            binding.apply {
                val font1 = ResourcesCompat.getFont(itemView.context, R.font.barlow_bold)
                val font2 = ResourcesCompat.getFont(itemView.context, R.font.barlow_medium)
                val localeID =  Locale("in", "ID")
                val numberFormat = NumberFormat.getCurrencyInstance(localeID)
                val productPrice = numberFormat.format(data.minPrice)
                    .replace(",00", "")
                val productQty = data.sellingUnit?.replace("Per ", "/")
                val ss = SpannableStringBuilder(productPrice+productQty)
                ss.setSpan(CustomTypefaceSpan("", font1!!), 0, productPrice.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                ss.setSpan(CustomTypefaceSpan("", font2!!), productPrice.length, productPrice.length + productQty?.length!!, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                Picasso.get()
                    .load(data.thumbnailUrl)
                    .placeholder(R.drawable.ic_image_placeholder)
                    .into(binding.ivThumbItemProduct)
                if (data.name?.contains(" - ") == true) {
                    tvNameItemProduct.text = data.name
                        .substring(0, data.name.indexOf("-"))
                }
                else {
                    tvNameItemProduct.text = data.name
                }
                tvPriceItemProduct.text = ss
                if (data.visualCues?.contains("in_stock") == true) {
                    cvStatusItemProduct.isGone = true
                }
                else {
                    tvStatusItemProduct.text = "Stok Habis"
                    cvStatusItemProduct.isGone = false
                }
            }
        }
    }
//
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerviewHolder {
        val itemBinding = ItemChildHomeMedicineBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecyclerviewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: RecyclerviewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }

//        holder.itemView.setOnClickListener{ view ->
//            val argsData = HomeFragmentDirections.homeToDetail(data.slug)
//            view.findNavController().navigate(argsData)
//        }
    }

//
//    fun setData(datalist: List<MedicineList>) {
//        this.data.clear()
//        this.data.addAll(datalist)
//        notifyDataSetChanged()
//    }
}