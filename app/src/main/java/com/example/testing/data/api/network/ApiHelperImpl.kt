package com.example.testing.data.api.network

import com.example.testing.data.api.model.medicine.MedicineDetail
import com.example.testing.data.api.model.medicine.MedicineListModel
import retrofit2.Response
import javax.inject.Inject

//class ApiHelperImpl @Inject constructor(
class ApiHelperImpl(
    private val apiService: ApiService
): ApiHelper {

    override suspend fun getPenawaranSpecialMedicine(): Response<MedicineListModel> {
        return apiService.getPenawaranSpecialMedicine()
    }

    override suspend fun getMedicineById(slug: String): Response<MedicineDetail> {
        return apiService.getMedicineById(slug)
    }

}