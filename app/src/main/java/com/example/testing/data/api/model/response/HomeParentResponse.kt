package com.example.testing.data.api.model.response

data class HomeParentResponse(
    val label: String,
    val path: String?,
    val type: Int,
    val medicineData: List<MedicineList>?,
    val articleData: ArticlesResponse?,
)
