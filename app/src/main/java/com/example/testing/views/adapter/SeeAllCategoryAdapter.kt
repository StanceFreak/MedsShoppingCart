package com.example.testing.views.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.testing.R
import com.example.testing.data.api.model.response.CategoryListData
import com.example.testing.databinding.ItemSeeAllCategoryBinding

class SeeAllCategoryAdapter(
    private val categoryDataList: List<CategoryListData>
): RecyclerView.Adapter<SeeAllCategoryAdapter.RecyclerViewHolder>() {

    inner class RecyclerViewHolder(private val binding: ItemSeeAllCategoryBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CategoryListData) {
            binding.apply {
                Glide.with(ivThumbSeeAllCategory.context)
                    .load(item.thumbnail)
                    .into(ivThumbSeeAllCategory)
                tvNameSeeAllCategory.text = item.title
                itemView.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putString("labelData", item.title)
                    bundle.putString("pathData", item.path)
                    itemView.findNavController().navigate(R.id.see_all_category_to_see_all, bundle)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val itemBinding = ItemSeeAllCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecyclerViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return this.categoryDataList.size
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val data = this.categoryDataList[position]
        holder.bind(data)
    }
}