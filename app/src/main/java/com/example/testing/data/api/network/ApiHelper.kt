package com.example.testing.data.api.network

class ApiHelper(private val api: Api) {

    suspend fun getPenawaranSpecialMedicine() = api.getPenawaranSpecialMedicine()

}