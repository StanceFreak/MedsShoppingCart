package com.example.testing.views.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testing.R
import com.example.testing.data.api.model.response.CategoryListData
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
        const val category_type = 4
    }

    inner class MedicineViewHolder (private val binding: ItemHomeParentBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data: HomeParentResponse) {
            val homeChildAdapter = HomeChildAdapter()
            binding.apply {
                tvTitleItemHomeParent.text = data.label
                if (data.type == 1) {
                    val bundle = Bundle()
                    bundle.putString("pathData", data.path)
                    bundle.putString("labelData", data.label)
                    cvSliderHomeContainer.isGone = true
                    rvItemHomeParent.apply {
                        isGone = false
                        layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
                        adapter = homeChildAdapter
                    }
                    data.medicineData?.let { homeChildAdapter.setDataMed(it, data.type) }
                    setDestination(R.id.home_to_see_all, bundle)
                }
                else if (data.type == 2) {
                    val bundle = Bundle()
                    bundle.putString("pathData", data.path)
                    bundle.putString("labelData", data.label)
                    cvSliderHomeContainer.isGone = true
                    rvItemHomeParent.apply {
                        isGone = false
                        layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
                        adapter = homeChildAdapter
                    }
                    data.articleData?.let { homeChildAdapter.setDataArt(it, data.type) }
                    setDestination(R.id.home_to_see_all, bundle)
                }
                else if (data.type == 3) {
                    val bundle = Bundle()
                    bundle.putString("pathData", data.path)
                    bundle.putString("labelData", data.label)
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
                    setDestination(R.id.home_to_see_all, bundle)
                }
                else if (data.type == 4) {
                    val homeCategoryAdapter = HomeCategoryAdapter()
                    val categoryData = listOf(
                        CategoryListData(
                            0,
                            "Kontrasepsi & Hormon",
                            R.drawable.ic_kontrasepsi,
                            "kontrasepsi-dan-hormon"
                        ),
                        CategoryListData(
                            1,
                            "Susu",
                            R.drawable.ic_susu,
                            "susu"
                        ),
                        CategoryListData(
                            2,
                            "Asma",
                            R.drawable.ic_asma,
                            "asma"
                        ),
                        CategoryListData(
                            3,
                            "Kebutuhan Covid-19",
                            R.drawable.ic_covid,
                            "kebutuhan-covid-19"
                        ),
                        CategoryListData(
                            4,
                            "Obat & Perawatan",
                            R.drawable.ic_obatperawatan,
                            "obat-dan-perawatan"
                        ),
                        CategoryListData(
                            5,
                            "Kecantikan & Perawatan Diri",
                            R.drawable.ic_kecantikan,
                            "kecantikan-dan-perawatan-diri"
                        ),
                        CategoryListData(
                            6,
                            "Vitamin & Suplemen",
                            R.drawable.ic_vitamin,
                            "vitamin-dan-suplemen-1"
                        ),
                        CategoryListData(
                            7,
                            "Nasal Care",
                            R.drawable.ic_nasal,
                            "nasal-care"
                        ),
                        CategoryListData(
                            8,
                            "Ibu & Anak",
                            R.drawable.ic_ibuanak,
                            "ibu-dan-anak"
                        ),
                        CategoryListData(
                            9,
                            "Obat Rutin",
                            R.drawable.ic_obatrutin,
                            "obat-rutin"
                        ),
                        CategoryListData(
                            10,
                            "Perangkat & Peralatan",
                            R.drawable.ic_peralatan,
                            "perangkat-dan-peralatan"
                        ),
                        CategoryListData(
                            11,
                            "Diabetes",
                            R.drawable.ic_diabetes,
                            "diabetes"
                        ),
                        CategoryListData(
                            12,
                            "Kesehatan Jantung",
                            R.drawable.ic_jantung,
                            "kesehatan-jantung"
                        ),
                        CategoryListData(13,
                            "Lensa Kontak",
                            R.drawable.ic_kontakmata,
                            "lensa-kontak"
                        ),
                    )
                    cvSliderHomeContainer.isGone = true
                    rvItemHomeParent.apply {
                        isGone = false
                        layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
                        adapter = homeCategoryAdapter
                    }
                    tvTitleItemHomeParent.text = data.label
                    homeCategoryAdapter.setData(categoryData)
                    val bundle = Bundle()
                    val listCategory = ArrayList(categoryData)
                    bundle.putString("labelData", data.label)
                    bundle.putParcelableArrayList("categoryData", listCategory)
                    setDestination(R.id.home_to_see_all_category, bundle)
                }
            }
        }

        private fun setDestination(destination: Int, bundle: Bundle) {
            binding.tvSeeAllHomeParent.setOnClickListener {
                itemView.findNavController().navigate(destination, bundle)
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
            3 -> slider_type
            else -> category_type
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