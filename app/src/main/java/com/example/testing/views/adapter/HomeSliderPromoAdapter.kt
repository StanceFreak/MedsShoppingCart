package com.example.testing.views.adapter

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.example.testing.R
import com.example.testing.data.api.model.response.MedicineList
import com.example.testing.databinding.ItemSliderHomeBinding
import com.example.testing.utils.CustomTypefaceSpan
import com.smarteist.autoimageslider.SliderViewAdapter
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.min

class HomeSliderPromoAdapter: SliderViewAdapter<HomeSliderPromoAdapter.SliderViewholder>() {

    private var promoList = ArrayList<MedicineList>()
    inner class SliderViewholder(private val binding: ItemSliderHomeBinding): SliderViewAdapter.ViewHolder(binding.root) {
        fun bind(medicine: MedicineList) {
            binding.apply {
                val font1 = ResourcesCompat.getFont(itemView.context, R.font.barlow_bold)
                val font2 = ResourcesCompat.getFont(itemView.context, R.font.barlow_medium)
                val localeID =  Locale("in", "ID")
                val numberFormat = NumberFormat.getCurrencyInstance(localeID)
                val productPrice = numberFormat.format(medicine.minPrice)
                    .replace(",00", "")
                val productQty = medicine.sellingUnit?.replace("Per ", "/")
                val ss = SpannableStringBuilder(productPrice+productQty)
                ss.setSpan(CustomTypefaceSpan("", font1!!), 0, productPrice.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                ss.setSpan(CustomTypefaceSpan("", font2!!), productPrice.length, productPrice.length + productQty?.length!!, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                Glide.with(ivThumbPromoSlider.context)
                    .load(medicine.thumbnailUrl)
                    .into(ivThumbPromoSlider)
                if (medicine.name?.contains(" - ") == true) {
                    tvNamePromoSlider.text = medicine.name
                        .substring(0, medicine.name.indexOf(" - "))
                }
                else {
                    tvNamePromoSlider.text = medicine.name
                }
                tvPricePromoSlider.text = ss
            }
        }
    }

    override fun getCount(): Int {
        return min(this.promoList.size, 5)
    }

    override fun onCreateViewHolder(parent: ViewGroup?): SliderViewholder {
        val itemBinding = ItemSliderHomeBinding.inflate(
            LayoutInflater.from(parent?.context),
            parent,
            false
        )
        return SliderViewholder(itemBinding)
    }

    override fun onBindViewHolder(viewHolder: SliderViewholder?, position: Int) {
        val data = this.promoList[position]
        viewHolder?.bind(data)
    }

    fun setData(dataList: List<MedicineList>) {
        this.promoList.apply {
            clear()
            addAll(dataList)
        }
        notifyDataSetChanged()
    }
}