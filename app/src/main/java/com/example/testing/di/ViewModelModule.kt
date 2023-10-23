package com.example.testing.di

import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.testing.views.cart.ShoppingCartViewModel
import com.example.testing.views.detail.ItemDetailViewModel
import com.example.testing.views.home.HomeViewModel
import com.example.testing.views.login.LoginViewModel
import com.example.testing.views.seeall.SeeAllCategoryViewModel
import com.example.testing.views.seeall.SeeAllViewModel
import com.example.testing.views.splash.SplashViewModel
import com.example.testing.views.wishlist.WishlistViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { ItemDetailViewModel(get()) }
    viewModel { SplashViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { ShoppingCartViewModel(get()) }
    viewModel { WishlistViewModel(get()) }
    viewModel { SeeAllViewModel(get()) }
    viewModel { SeeAllCategoryViewModel(get()) }
}