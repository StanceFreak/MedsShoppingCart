package com.example.testing.views.home
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.testing.data.api.repository.MedicineRepository
import com.example.testing.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

//@HiltViewModel
//class HomeViewModel @Inject constructor (
class HomeViewModel(
        private val medicineRepository: MedicineRepository
    ) : ViewModel() {

    fun getPenawaranSpecialMedicine() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = medicineRepository.getPenawaranSpecialMedicine()))
        }
        catch (e: Exception) {
            emit(Resource.error(data = null, message = e.message ?: "Error Occured!"))
        }
    }

}