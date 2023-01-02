package com.example.testing.views.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.testing.databinding.RvHomeBinding

class MedicineListAdapter : RecyclerView.Adapter<MedicineListAdapter.RecyclerviewHolder>() {
    inner class RecyclerviewHolder (private val binding: RvHomeBinding) : RecyclerView.ViewHolder(binding.root){

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
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}