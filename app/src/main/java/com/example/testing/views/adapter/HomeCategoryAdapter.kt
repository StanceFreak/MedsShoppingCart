package com.example.testing.views.adapter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.testing.R
import com.example.testing.data.api.model.response.CategoryListData
import com.example.testing.databinding.ItemChildHomeCategoryBinding
import kotlin.math.min

class HomeCategoryAdapter: RecyclerView.Adapter<HomeCategoryAdapter.RecyclerViewHolder>() {

    private val categoryDataList = ArrayList<CategoryListData>()
    inner class RecyclerViewHolder(private val binding: ItemChildHomeCategoryBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CategoryListData) {
            binding.apply {
                Glide.with(ivThumbItemHomeCategory.context)
                    .load(item.thumbnail)
                    .into(ivThumbItemHomeCategory)
                tvNameItemHomeCategory.text = item.title
                itemView.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putString("labelData", item.title)
                    bundle.putString("pathData", item.path)
                    itemView.findNavController().navigate(R.id.home_to_see_all, bundle)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val itemBinding = ItemChildHomeCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecyclerViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return min(this.categoryDataList.size, 5)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val data = this.categoryDataList[position]
        holder.bind(data)
    }

    fun setData(dataList: List<CategoryListData>) {
        this.categoryDataList.apply {
            clear()
            addAll(dataList)
            Log.d("tes data", this.toString())
        }
        notifyDataSetChanged()
    }
}