package com.example.testing.views.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testing.R
import com.example.testing.data.api.model.response.HomeParentResponse
import com.example.testing.databinding.ItemHomeParentBinding
import kotlin.collections.ArrayList

class HomeParentAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var data = ArrayList<HomeParentResponse>()

    inner class MedicineViewHolder (private val binding: ItemHomeParentBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data: HomeParentResponse) {
            val homeChildAdapter = HomeChildAdapter()
            binding.apply {
                tvTitleItemHomeParent.text = data.label
                if (data.type == 1) {
                    rvItemHomeParent.apply {
                        layoutManager = GridLayoutManager(itemView.context, 2)
                        adapter = homeChildAdapter
                    }
                    data.medicineData?.let { homeChildAdapter.setDataMed(it, data.type) }
                }
                else if (data.type == 2) {
                    rvItemHomeParent.apply {
                        layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
                        adapter = homeChildAdapter
                    }
                    data.articleData?.let { homeChildAdapter.setDataArt(it, data.type) }
                }
                ivForwardItemHomeParent.setOnClickListener {
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
        val data = this.data[position]
        (holder as MedicineViewHolder).bind(data)
    }

    override fun getItemCount(): Int {
        return this.data.size
    }

    fun setData(datalist: List<HomeParentResponse>) {
        this.data.clear()
        this.data.addAll(datalist)
        notifyDataSetChanged()
    }
}