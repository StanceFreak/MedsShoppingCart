package com.example.testing.views.adapter

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isGone
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.testing.R
import com.example.testing.data.api.model.response.ArticlesResponseItem
import com.example.testing.data.api.model.response.MedicineList
import com.example.testing.databinding.ItemHomeArticleChildBinding
import com.example.testing.databinding.ItemListMedicineBinding
import com.example.testing.utils.CustomTypefaceSpan
import com.squareup.picasso.Picasso
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.collections.ArrayList
import kotlin.math.min

class HomeChildAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var medDataList = ArrayList<MedicineList>()
    private var artDataList = ArrayList<ArticlesResponseItem>()
    private var typeList = -1

    companion object {
        const val med_type = 1
        const val art_type = 2
    }

    inner class MedViewHolder (private val binding: ItemListMedicineBinding) : RecyclerView.ViewHolder(binding.root){
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
                        .substring(0, data.name.indexOf(" - "))
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

    inner class ArtViewHolder (private val binding: ItemHomeArticleChildBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data: ArticlesResponseItem) {
            binding.apply {
                val listCategories = ArrayList<String>()
                for (i in 0 until data.categories.size) {
                    listCategories.add(data.categories[i].name)
                }
                val categories = listCategories.joinToString(
                    prefix = "",
                    separator = ", ",
                    postfix = ""
                )
                Picasso.get()
                    .load(data.imageUrl)
                    .placeholder(R.drawable.ic_image_placeholder)
                    .into(ivThumbItemHomeChild)
                tvTitleItemHomeChild.text = data.title
                tvCategoriesItemHomeChild.text = "Kategori: ${categories}"
                tvDescItemHomeChild.text = data.metaDescription
                tvDateItemHomeChild.text = "Diunggah pada ${SimpleDateFormat("dd/MM/yyyy").format(data.publishDate)}"
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (typeList == 1) {
            med_type
        }
        else {
            art_type
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == med_type) {
            val itemBinding = ItemListMedicineBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return MedViewHolder(itemBinding)
        }
        else {
            val itemBinding = ItemHomeArticleChildBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return ArtViewHolder(itemBinding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        holder.apply {
            if (typeList == med_type) {
                val data = medDataList[position]
                (holder as MedViewHolder).bind(data)
                itemView.setOnClickListener{
                    val bundle = Bundle()
                    bundle.putString("slug", data.slug)
                    itemView.findNavController().navigate(R.id.home_to_detail, bundle)
                }
            }
            else {
                val data = artDataList[position]
                (holder as ArtViewHolder).bind(data)
            }
        }
    }

    override fun getItemCount(): Int {
        return if (typeList == med_type) {
            min(this.medDataList.size, 2)
        }
        else {
            min(this.artDataList.size, 5)
        }
//        return min(this.data.size, 5)
    }

    fun setDataMed(datalist: List<MedicineList>, type: Int) {
        this.medDataList.clear()
        this.medDataList.addAll(datalist)
        this.typeList = type
        notifyDataSetChanged()
    }

    fun setDataArt(datalist: List<ArticlesResponseItem>, type: Int) {
        this.artDataList.clear()
        this.artDataList.addAll(datalist)
        this.typeList = type
        notifyDataSetChanged()
    }
}