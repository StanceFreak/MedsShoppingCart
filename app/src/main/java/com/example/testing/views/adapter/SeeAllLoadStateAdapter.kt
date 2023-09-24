package com.example.testing.views.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.testing.databinding.ItemLoadPageBinding

class SeeAllLoadStateAdapter(
    private val adapter: SeeAllAdapter
): LoadStateAdapter<SeeAllLoadStateAdapter.LoadStateViewholder>() {
    inner class LoadStateViewholder(
        private val binding: ItemLoadPageBinding,
        private val retryCb: () -> Unit
    ): RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnErrorLoad.setOnClickListener {
                retryCb()
            }
        }
        fun bind(state: LoadState) {
            binding.apply {
                pbLoadingLoad.isVisible = state is LoadState.Loading
                btnErrorLoad.isVisible = state is LoadState.Error
                tvErrorLoad.isVisible = state is LoadState.Error
            }
        }
    }

    override fun onBindViewHolder(holder: LoadStateViewholder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewholder {
        val loadBinding = ItemLoadPageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LoadStateViewholder(loadBinding) { adapter.retry() }
    }
}