package com.example.testing.data.api.network

import com.example.testing.data.api.model.medicine.MedicineDetail
import com.example.testing.data.api.model.medicine.MedicineListModel

class ApiHelper(private val api: Api) {

    suspend fun getPenawaranSpecialMedicine() : MedicineListModel {
        return api.getPenawaranSpecialMedicine()
    }

    suspend fun getMedicineById(slug: String) : MedicineDetail {
        return api.getMedicineById(slug)
    }

}