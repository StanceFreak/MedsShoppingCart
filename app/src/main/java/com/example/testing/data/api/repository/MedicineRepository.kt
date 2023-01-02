package com.example.testing.data.api.repository

import com.example.testing.data.api.network.ApiHelper

class MedicineRepository(private val apiHelper: ApiHelper) {

    suspend fun getPenawaranSpecialMedicine() = apiHelper.getPenawaranSpecialMedicine()

}