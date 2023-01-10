package com.example.testing.data.api.repository

import com.example.testing.data.api.model.medicine.MedicineDetail
import com.example.testing.data.api.model.medicine.MedicineListModel
import com.example.testing.data.api.network.ApiHelper

class MedicineRepository(private val apiHelper: ApiHelper) {

    suspend fun getPenawaranSpecialMedicine() : MedicineListModel {
        return apiHelper.getPenawaranSpecialMedicine()
    }


    suspend fun getMedicineById(slug: String) : MedicineDetail {
        return apiHelper.getMedicineById(slug)
    }

}