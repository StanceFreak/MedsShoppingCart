package com.example.testing.data.api.repository

import com.example.testing.data.api.model.response.ArticlesResponse
import com.example.testing.data.api.model.response.CategoryListResponse
import com.example.testing.data.api.model.response.MedicineDetail
import com.example.testing.data.api.model.response.MedicineListModel
import com.example.testing.data.api.network.ApiHelper
import retrofit2.Response

//class NetworkRepository @Inject constructor(
class RemoteDataSource(
    private val apiHelper: ApiHelper
) {

    suspend fun getMedicineByCategory(path: String, page: Int) : Response<MedicineListModel> {
        return apiHelper.getMedicineByCategory(path, page)
    }

    suspend fun getCategoryList(): Response<CategoryListResponse> {
        return apiHelper.getCategoryList()
    }

    suspend fun getArticlesTrending() : Response<ArticlesResponse> {
        return apiHelper.getArticlesTrending()
    }

    suspend fun getMedicineById(slug: String) : Response<MedicineDetail> {
        return apiHelper.getMedicineById(slug)
    }

}