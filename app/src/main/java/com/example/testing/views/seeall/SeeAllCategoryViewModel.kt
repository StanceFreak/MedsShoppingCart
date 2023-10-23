package com.example.testing.views.seeall

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testing.data.api.model.response.CategoryListResponse
import com.example.testing.data.api.repository.AppRepository
import com.example.testing.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class SeeAllCategoryViewModel(
    private val repo: AppRepository
): ViewModel() {

    private val getCategoryListSuccess = MutableLiveData<SingleLiveEvent<Pair<Boolean?, CategoryListResponse?>>>()
    private val getCategoryListError = MutableLiveData<SingleLiveEvent<Pair<Boolean, String?>>>()
    private val getCategoryListLoading = MutableLiveData<SingleLiveEvent<Boolean>>()

    fun observeGetCategoryListSuccess(): LiveData<SingleLiveEvent<Pair<Boolean?, CategoryListResponse?>>> = getCategoryListSuccess
    fun observeGetCategoryListError(): LiveData<SingleLiveEvent<Pair<Boolean, String?>>> = getCategoryListError
    fun observeGetCategoryListLoading(): LiveData<SingleLiveEvent<Boolean>> = getCategoryListLoading

    fun getCategoryList() {
        viewModelScope.launch {
            getCategoryListLoading.postValue(SingleLiveEvent(true))
            getCategoryListError.postValue(SingleLiveEvent(Pair(false, null)))
            try {
                val response = repo.getCategoryList()
                if (response.isSuccessful.not() || response.body() == null) {
                    getCategoryListLoading.postValue(SingleLiveEvent(false))
                    getCategoryListError.postValue(SingleLiveEvent(Pair(true, "Gagal terhubung ke server, silahkan cek koneksi anda!")))
                }
                else {
                    getCategoryListLoading.postValue(SingleLiveEvent(false))
                    getCategoryListError.postValue(SingleLiveEvent(Pair(false, null)))
                    getCategoryListSuccess.postValue(SingleLiveEvent(Pair(true, response.body())))
                }
            }
            catch (e: Exception) {
                getCategoryListLoading.postValue(SingleLiveEvent(false))
                getCategoryListError.postValue(SingleLiveEvent(Pair(true, "Terjadi Kesalahan, silahkan coba beberapa saat lagi")))
            }
        }
    }

}