package com.example.testing.di

import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.testing.views.detail.ItemDetailViewModel
import com.example.testing.views.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        HomeViewModel(get())
    }
    viewModel {
        ItemDetailViewModel(get())
    }
}