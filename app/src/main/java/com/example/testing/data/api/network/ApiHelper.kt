package com.example.testing.data.api.network

import com.example.testing.data.api.model.MedicineDetail
import com.example.testing.data.api.model.MedicineListModel

class ApiHelper(private val api: Api) {

    suspend fun getPenawaranSpecialMedicine() : MedicineListModel {
        return api.getPenawaranSpecialMedicine()
    }

    suspend fun getMedicineById(slug: String) : MedicineDetail {
        return api.getMedicineById(slug)
    }

}