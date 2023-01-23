package com.example.testing.data.api.repository

import com.example.testing.data.api.model.medicine.MedicineDetail
import com.example.testing.data.api.model.medicine.MedicineListModel
import com.example.testing.data.api.network.ApiHelper
import retrofit2.Response
import javax.inject.Inject

class MedicineRepository @Inject constructor(private val apiHelper: ApiHelper) {

    suspend fun getPenawaranSpecialMedicine() : Response<MedicineListModel> {
        return apiHelper.getPenawaranSpecialMedicine()
    }

    suspend fun getMedicineById(slug: String) : Response<MedicineDetail> {
        return apiHelper.getMedicineById(slug)
    }

}