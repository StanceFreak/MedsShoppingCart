package com.example.testing.views.seeall

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.testing.data.api.model.response.MedicineList
import com.example.testing.data.api.repository.AppRepository
import com.example.testing.utils.SingleLiveEvent
import kotlinx.coroutines.flow.Flow

class SeeAllViewModel(
    private val repo: AppRepository
): ViewModel() {

    private val getMedicineSuccess = MutableLiveData<PagingData<MedicineList>?>()
    private val getMedicineError = MutableLiveData<SingleLiveEvent<Pair<Boolean, String?>>>()
    private val getMedicineLoading = MutableLiveData<SingleLiveEvent<Boolean>>()
    fun observeGetMedicineSuccess(): LiveData<PagingData<MedicineList>?> = getMedicineSuccess
    fun observeGetMedicineError(): LiveData<SingleLiveEvent<Pair<Boolean, String?>>> = getMedicineError
    fun observeGetMedicineLoading(): LiveData<SingleLiveEvent<Boolean>> = getMedicineLoading

//    fun getMedicineByPath(path: String): Flow<PagingData<MedicineList>> {
    fun getMedicineByPath(path: String): LiveData<PagingData<MedicineList>> {
        return repo.getMedicineByPath(path).cachedIn(viewModelScope)
//        viewModelScope.launch {
//            getMedicineLoading.postValue(SingleLiveEvent(true))
//            getMedicineError.postValue(SingleLiveEvent(Pair(false, null)))
//            try {
//                val response = repo.getMedicineByCategory(path, page)
//                if (!response.isSuccessful || response.body() == null) {
//                    getMedicineLoading.postValue(SingleLiveEvent(false))
//                    getMedicineError.postValue(SingleLiveEvent(Pair(true, "Gagal terhubung ke server, silahkan ulangi beberapa saat lagi!")))
//                }
//                else {
//                    getMedicineLoading.postValue(SingleLiveEvent(false))
//                    getMedicineError.postValue(SingleLiveEvent(Pair(false, null)))
//                    getMedicineSuccess.postValue(SingleLiveEvent(response.body()))
//                }
//            }
//            catch (e: Exception) {
//                e.printStackTrace()
//                getMedicineLoading.postValue(SingleLiveEvent(false))
//                getMedicineError.postValue(SingleLiveEvent(Pair(true, "Terjadi Kesalahan, silahkan coba beberapa saat lagi")))
//            }
//        }
    }

    fun getUserData(): LiveData<String> {
        return repo.getUserData().asLiveData()
    }

}