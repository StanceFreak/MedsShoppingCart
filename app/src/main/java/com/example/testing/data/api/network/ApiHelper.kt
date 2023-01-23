package com.example.testing.data.api.network

import com.example.testing.data.api.model.medicine.MedicineDetail
import com.example.testing.data.api.model.medicine.MedicineListModel
import retrofit2.Response

interface ApiHelper {

    suspend fun getPenawaranSpecialMedicine() : Response<MedicineListModel>

    suspend fun getMedicineById(slug: String) : Response<MedicineDetail>

}