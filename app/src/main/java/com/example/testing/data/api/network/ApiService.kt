package com.example.testing.data.api.network

import com.example.testing.data.api.model.medicine.MedicineDetail
import com.example.testing.data.api.model.medicine.MedicineListModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("/medicine/categories/penawaran-special/page/1")
    suspend fun getPenawaranSpecialMedicine(): Response<MedicineListModel>

    @GET("/medicine/detail/{slug}")
    suspend fun getMedicineById(
        @Path(value = "slug", encoded = true) slug: String
    ): Response<MedicineDetail>

}