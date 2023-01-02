package com.example.testing.data.api.network

import com.example.testing.data.api.model.MedicineListModel
import retrofit2.http.GET

interface Api {

    @GET("/medicine/categories/penawaran-special/page/1")
    suspend fun getPenawaranSpecialMedicine(): MedicineListModel

}