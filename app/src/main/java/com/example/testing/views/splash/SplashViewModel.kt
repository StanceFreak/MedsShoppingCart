package com.example.testing.views.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.testing.data.api.repository.AppRepository

class SplashViewModel(
    private val repo: AppRepository
): ViewModel() {

    fun getLoginState(): LiveData<Boolean> {
        return repo.getLoginState().asLiveData()
    }

}