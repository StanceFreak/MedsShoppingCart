package com.example.testing.views.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.testing.data.api.repository.AppRepository

class ShoppingCartViewModel(
    private val repo: AppRepository
): ViewModel() {

    fun getUserData(): LiveData<String> {
        return repo.getUserData().asLiveData()
    }

}