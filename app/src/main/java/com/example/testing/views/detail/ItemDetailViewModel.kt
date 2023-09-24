package com.example.testing.views.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.testing.data.api.model.response.MedicineDetail
import com.example.testing.data.api.model.response.MedicineListModel
import com.example.testing.data.api.repository.AppRepository
import com.example.testing.data.api.repository.RemoteDataSource
import com.example.testing.utils.Resource
import com.example.testing.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

//@HiltViewModel
//class ItemDetailViewModel @Inject constructor(
class ItemDetailViewModel(
        private val repo: AppRepository
) : ViewModel() {

    private val getItemDetailSuccess = MutableLiveData<SingleLiveEvent<MedicineDetail?>>()
    private val getItemDetailError = MutableLiveData<SingleLiveEvent<Pair<Boolean, String?>>>()
    private val getItemDetailLoading = MutableLiveData<SingleLiveEvent<Boolean>>()
    fun observeGetItemDetailSuccess(): LiveData<SingleLiveEvent<MedicineDetail?>> = getItemDetailSuccess
    fun observeGetItemDetailError(): LiveData<SingleLiveEvent<Pair<Boolean, String?>>> = getItemDetailError
    fun observeGetItemDetailLoading(): LiveData<SingleLiveEvent<Boolean>> = getItemDetailLoading

    fun getMedicineById(slug: String) {
        viewModelScope.launch {
            getItemDetailLoading.postValue(SingleLiveEvent(true))
            getItemDetailError.postValue(SingleLiveEvent(Pair(false, null)))
            try {
                val itemDetailResponse = repo.getMedicineById(slug)
                if (!itemDetailResponse.isSuccessful || itemDetailResponse.body() == null) {
                    getItemDetailLoading.postValue(SingleLiveEvent(false))
                    getItemDetailError.postValue(SingleLiveEvent(Pair(true, "Gagal terhubung ke server, silahkan cek koneksi anda!")))
                }
                else {
                    getItemDetailLoading.postValue(SingleLiveEvent(false))
                    getItemDetailError.postValue(SingleLiveEvent(Pair(false, null)))
                    getItemDetailSuccess.postValue(SingleLiveEvent(itemDetailResponse.body()))
                }
            }
            catch (e: Exception) {
                getItemDetailLoading.postValue(SingleLiveEvent(false))
                getItemDetailError.postValue(SingleLiveEvent(Pair(true, "Terjadi kesalahan, silahkan coba beberapa saat lagi")))
            }
        }
    }

    fun getUserData(): LiveData<String> {
        return repo.getUserData().asLiveData()
    }

}