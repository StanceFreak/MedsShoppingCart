package com.example.testing.data.api.network

import com.example.testing.data.api.model.response.ArticlesResponse
import com.example.testing.data.api.model.response.CategoryListResponse
import com.example.testing.data.api.model.response.MedicineDetail
import com.example.testing.data.api.model.response.MedicineListModel
import retrofit2.Response

class ApiHelper (
    private val service: ApiService
): ApiService {

    override suspend fun getMedicineByCategory(path: String, page: Int) : Response<MedicineListModel> {
        return service.getMedicineByCategory(path, page)
    }

    override suspend fun getCategoryList(): Response<CategoryListResponse> {
        return service.getCategoryList()
    }

    override suspend fun getArticlesTrending(): Response<ArticlesResponse> {
        return service.getArticlesTrending()
    }

    override suspend fun getMedicineById(slug: String) : Response<MedicineDetail> {
        return service.getMedicineById(slug)
    }

}