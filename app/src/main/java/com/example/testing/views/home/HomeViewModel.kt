package com.example.testing.views.home
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testing.data.api.model.response.ArticlesResponse
import com.example.testing.data.api.model.response.MedicineListModel
import com.example.testing.data.api.repository.AppRepository
import com.example.testing.utils.SingleLiveEvent
import kotlinx.coroutines.launch

//@HiltViewModel
//class HomeViewModel @Inject constructor (
class HomeViewModel(
    private val repo: AppRepository
) : ViewModel() {

    private val getOfferSuccess = MutableLiveData<SingleLiveEvent<MedicineListModel?>>()
    private val getOfferError = MutableLiveData<SingleLiveEvent<Pair<Boolean, String?>>>()
    private val getOfferLoading = MutableLiveData<SingleLiveEvent<Boolean>>()
    private val getParentingSuccess = MutableLiveData<SingleLiveEvent<MedicineListModel?>>()
    private val getParentingError = MutableLiveData<SingleLiveEvent<Pair<Boolean, String?>>>()
    private val getParentingLoading = MutableLiveData<SingleLiveEvent<Boolean>>()
    private val getDiabetSuccess = MutableLiveData<SingleLiveEvent<MedicineListModel?>>()
    private val getDiabetError = MutableLiveData<SingleLiveEvent<Pair<Boolean, String?>>>()
    private val getDiabetLoading = MutableLiveData<SingleLiveEvent<Boolean>>()
    private val getTrendingArticlesSuccess = MutableLiveData<SingleLiveEvent<ArticlesResponse?>>()
    private val getTrendingArticlesError = MutableLiveData<SingleLiveEvent<Pair<Boolean, String?>>>()
    private val getTrendingArticlesLoading = MutableLiveData<SingleLiveEvent<Boolean>>()
    fun observeGetTrendingArticlesSuccess(): LiveData<SingleLiveEvent<ArticlesResponse?>> = getTrendingArticlesSuccess
    fun observeGetOfferSuccess(): LiveData<SingleLiveEvent<MedicineListModel?>> = getOfferSuccess
    fun observeGetOfferError(): LiveData<SingleLiveEvent<Pair<Boolean, String?>>> = getOfferError
    fun observeGetParentingSuccess(): LiveData<SingleLiveEvent<MedicineListModel?>> = getParentingSuccess
    fun observeGetParentingError(): LiveData<SingleLiveEvent<Pair<Boolean, String?>>> = getParentingError
    fun observeGetDiabetSuccess(): LiveData<SingleLiveEvent<MedicineListModel?>> = getDiabetSuccess
    fun observeGetDiabetError(): LiveData<SingleLiveEvent<Pair<Boolean, String?>>> = getDiabetError
    fun observeGetTrendingArticlesError(): LiveData<SingleLiveEvent<Pair<Boolean, String?>>> = getTrendingArticlesError
    fun observeGetOfferLoading(): LiveData<SingleLiveEvent<Boolean>> = getOfferLoading
    fun observeGetParentingLoading(): LiveData<SingleLiveEvent<Boolean>> = getParentingLoading
    fun observeGetDiabetLoading(): LiveData<SingleLiveEvent<Boolean>> = getDiabetLoading
    fun observeGetTrendingArticlesLoading(): LiveData<SingleLiveEvent<Boolean>> = getTrendingArticlesLoading

    fun getHomeData() {
        viewModelScope.launch {
            getOfferLoading.postValue(SingleLiveEvent(true))
            getParentingLoading.postValue(SingleLiveEvent(true))
            getDiabetLoading.postValue(SingleLiveEvent(true))
            getTrendingArticlesLoading.postValue(SingleLiveEvent(true))
            getOfferError.postValue(SingleLiveEvent(Pair(false, null)))
            getParentingError.postValue(SingleLiveEvent(Pair(false, null)))
            getDiabetError.postValue(SingleLiveEvent(Pair(false, null)))
            getTrendingArticlesError.postValue(SingleLiveEvent(Pair(false, null)))
            try {
                val offerResponse = repo.getMedicineByCategory("penawaran-special", 1)
                val parentingResponse = repo.getMedicineByCategory("ibu-dan-anak", 1)
                val diabetResponse = repo.getMedicineByCategory("diabetes-medicine", 1)
                val articleResponse = repo.getArticlesTrending()
                if (offerResponse.isSuccessful.not() || offerResponse.body() == null) {
                    getOfferLoading.postValue(SingleLiveEvent(false))
                    getOfferError.postValue(SingleLiveEvent(Pair(true, "Gagal terhubung ke server, silahkan cek koneksi anda!")))
                }
                else if (parentingResponse.isSuccessful.not() || parentingResponse.body() == null) {
                    getParentingLoading.postValue(SingleLiveEvent(false))
                    getParentingError.postValue(SingleLiveEvent(Pair(true, "Gagal terhubung ke server, silahkan cek koneksi anda!")))
                }
                else if (diabetResponse.isSuccessful.not() || diabetResponse.body() == null) {
                    getDiabetLoading.postValue(SingleLiveEvent(false))
                    getDiabetError.postValue(SingleLiveEvent(Pair(true, "Gagal terhubung ke server, silahkan cek koneksi anda!")))
                }
                else if (articleResponse.isSuccessful.not() || articleResponse.body() == null) {
                    getTrendingArticlesLoading.postValue(SingleLiveEvent(false))
                    getTrendingArticlesError.postValue(SingleLiveEvent(Pair(true, "Gagal terhubung ke server, silahkan cek koneksi anda!")))
                }
                else {
                    getOfferLoading.postValue(SingleLiveEvent(false))
                    getOfferError.postValue(SingleLiveEvent(Pair(false, null)))
                    getOfferSuccess.postValue(SingleLiveEvent(offerResponse.body()))
                    getParentingLoading.postValue(SingleLiveEvent(false))
                    getParentingError.postValue(SingleLiveEvent(Pair(false, null)))
                    getParentingSuccess.postValue(SingleLiveEvent(parentingResponse.body()))
                    getDiabetLoading.postValue(SingleLiveEvent(false))
                    getDiabetError.postValue(SingleLiveEvent(Pair(false, null)))
                    getDiabetSuccess.postValue(SingleLiveEvent(diabetResponse.body()))
                    getTrendingArticlesLoading.postValue(SingleLiveEvent(false))
                    getTrendingArticlesError.postValue(SingleLiveEvent(Pair(false, null)))
                    getTrendingArticlesSuccess.postValue(SingleLiveEvent(articleResponse.body()))
                }
            }
            catch (e: Exception) {
                e.printStackTrace()
                getOfferLoading.postValue(SingleLiveEvent(false))
                getOfferError.postValue(SingleLiveEvent(Pair(true, "Terjadi Kesalahan, silahkan coba beberapa saat lagi")))
                getParentingLoading.postValue(SingleLiveEvent(false))
                getParentingError.postValue(SingleLiveEvent(Pair(true, "Terjadi Kesalahan, silahkan coba beberapa saat lagi")))
                getDiabetLoading.postValue(SingleLiveEvent(false))
                getDiabetError.postValue(SingleLiveEvent(Pair(true, "Terjadi Kesalahan, silahkan coba beberapa saat lagi")))
                getTrendingArticlesLoading.postValue(SingleLiveEvent(false))
                getTrendingArticlesError.postValue(SingleLiveEvent(Pair(true, "Terjadi Kesalahan, silahkan coba beberapa saat lagi")))
            }
        }
    }

//    fun getPenawaranSpecialMedicine() = liveData(Dispatchers.IO) {
//        emit(Resource.loading(data = null))
//        try {
//            emit(Resource.success(data = repo.getPenawaranSpecialMedicine()))
//        }
//        catch (e: Exception) {
//            emit(Resource.error(data = null, message = e.message ?: "Error Occured!"))
//        }
//    }

}