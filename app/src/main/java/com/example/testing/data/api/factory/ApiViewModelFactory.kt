package com.example.testing.data.api.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.testing.data.api.network.ApiHelper
import com.example.testing.data.api.repository.MedicineRepository
import com.example.testing.views.detail.ItemDetailViewModel
import com.example.testing.views.home.HomeViewModel
import java.lang.IllegalArgumentException

class ApiViewModelFactory (private val apiHelper: ApiHelper) : ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(MedicineRepository(apiHelper)) as T
        }
        if (modelClass.isAssignableFrom(ItemDetailViewModel::class.java)) {
            return ItemDetailViewModel(MedicineRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}