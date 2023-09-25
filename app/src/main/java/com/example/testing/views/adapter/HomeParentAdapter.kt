package com.example.testing.views.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testing.R
import com.example.testing.data.api.model.response.HomeParentResponse
import com.example.testing.databinding.ItemHomeParentBinding
import com.smarteist.autoimageslider.SliderView
import kotlin.collections.ArrayList

class HomeParentAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var parentData = ArrayList<HomeParentResponse>()
    private var typeList = -1

    companion object {
        const val med_type = 1
        const val art_type = 2
        const val slider_type = 3
    }

    inner class MedicineViewHolder (private val binding: ItemHomeParentBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data: HomeParentResponse) {
            val homeChildAdapter = HomeChildAdapter()
            binding.apply {
                tvTitleItemHomeParent.text = data.label
                if (data.type == 1) {
                    cvSliderHomeContainer.isGone = true
                    rvItemHomeParent.apply {
                        isGone = false
                        layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
                        adapter = homeChildAdapter
                    }
                    data.medicineData?.let { homeChildAdapter.setDataMed(it, data.type) }
                }
                else if (data.type == 2) {
                    cvSliderHomeContainer.isGone = true
                    rvItemHomeParent.apply {
                        isGone = false
                        layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
                        adapter = homeChildAdapter
                    }
                    data.articleData?.let { homeChildAdapter.setDataArt(it, data.type) }
                }
                else if (data.type == 3) {
                    val homeSliderPromoAdapter = HomeSliderPromoAdapter()
                    cvSliderHomeContainer.isGone = false
                    rvItemHomeParent.isGone = true
                    tvTitleItemHomeParent.text = data.label
                    data.medicineData?.let {
                        homeSliderPromoAdapter.setData(it)
                    }
                    svSliderHome.apply {
                        setInfiniteAdapterEnabled(true)
                        autoCycleDirection = SliderView.LAYOUT_DIRECTION_LTR
                        setIndicatorEnabled(true)
                        indicatorSelectedColor = resources.getColor(R.color.dark_gray)
                        indicatorUnselectedColor = resources.getColor(R.color.french_gray)
                        setSliderAdapter(homeSliderPromoAdapter)
                        scrollTimeInSec = 3
                        isAutoCycle = true
                        startAutoCycle()
                    }
                }
                tvSeeAllHomeParent.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putString("pathData", data.path)
                    bundle.putString("labelData", data.label)
                    itemView.findNavController().navigate(R.id.home_to_see_all, bundle)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemBinding = ItemHomeParentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MedicineViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = this.parentData[position]
        (holder as MedicineViewHolder).bind(data)
    }

    override fun getItemViewType(position: Int): Int {
        return when (typeList) {
            1 -> med_type
            2 -> art_type
            else -> slider_type
        }
    }

    override fun getItemCount(): Int {
        return this.parentData.size
    }

    fun setData(datalist: List<HomeParentResponse>, type: Int) {
        this.parentData.clear()
        this.parentData.addAll(datalist)
        this.typeList = type
        notifyDataSetChanged()
    }
}