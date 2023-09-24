package com.example.testing.data.api.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.testing.data.api.model.response.ArticlesResponse
import com.example.testing.data.api.model.response.MedicineDetail
import com.example.testing.data.api.model.response.MedicineList
import com.example.testing.data.api.model.response.MedicineListModel
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class AppRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
) {

    suspend fun getMedicineByCategory(path: String, page: Int) : Response<MedicineListModel> {
        return remoteDataSource.getMedicineByCategory(path, page)
    }

    suspend fun getArticlesTrending() : Response<ArticlesResponse> {
        return remoteDataSource.getArticlesTrending()
    }

    suspend fun getMedicineById(slug: String): Response<MedicineDetail> {
        return remoteDataSource.getMedicineById(slug)
    }

    fun getLoginState(): Flow<Boolean> {
        return localDataSource.getLoginState()
    }
    fun getUserData(): Flow<String> {
        return localDataSource.getUserData()
    }

    suspend fun storeLoginState(state: Boolean) {
        return localDataSource.storeLoginState(state)
    }
    suspend fun storeUserData(uid: String) {
        return localDataSource.storeUserData(uid)
    }

    suspend fun removeSession() {
        localDataSource.removeSession()
    }

//    fun getMedicineByPath(path: String): Flow<PagingData<MedicineList>> {
    fun getMedicineByPath(path: String): LiveData<PagingData<MedicineList>> {
        return Pager(
            PagingConfig(
                20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                PagingSource(
                    remoteDataSource,
                    path
                )
            }
        ).liveData
//        ).flow
    }

}