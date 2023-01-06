package com.example.testing.data.api.network

import com.example.testing.data.api.model.MedicineDetail
import com.example.testing.data.api.model.MedicineListModel
import retrofit2.http.GET
import retrofit2.http.Path

interface Api {

    @GET("/medicine/categories/penawaran-special/page/1")
    suspend fun getPenawaranSpecialMedicine(): MedicineListModel

    @GET("/medicine/detail/{slug}")
    suspend fun getMedicineById(
        @Path(value = "slug", encoded = true) slug: String
    ): MedicineDetail

}