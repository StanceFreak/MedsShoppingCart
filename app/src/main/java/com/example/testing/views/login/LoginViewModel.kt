package com.example.testing.views.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testing.data.api.repository.AppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repo: AppRepository
): ViewModel() {

    fun storeLoginState(state: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.storeLoginState(state)
        }
    }

    fun storeUserData(uid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.storeUserData(uid)
        }
    }

}