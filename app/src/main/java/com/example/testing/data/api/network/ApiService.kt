package com.example.testing.data.api.network

import com.example.testing.data.api.model.response.ArticlesResponse
import com.example.testing.data.api.model.response.MedicineDetail
import com.example.testing.data.api.model.response.MedicineListModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("/medicine/categories/{path}/page/{page}")
    suspend fun getMedicineByCategory(
        @Path(value = "path", encoded = true) path: String,
        @Path(value = "page", encoded = true) page: Int
    ): Response<MedicineListModel>

    @GET("/articles-trending")
    suspend fun getArticlesTrending(): Response<ArticlesResponse>

    @GET("/medicine/detail/{slug}")
    suspend fun getMedicineById(
        @Path(value = "slug", encoded = true) slug: String
    ): Response<MedicineDetail>

}