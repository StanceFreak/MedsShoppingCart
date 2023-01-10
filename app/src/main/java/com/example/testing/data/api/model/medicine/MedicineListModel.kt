package com.example.testing.data.api.model.medicine


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class MedicineListModel(
    @SerializedName("result")
    val medicineList: List<MedicineList>,
    @SerializedName("next_page")
    val nextPage: Boolean,
) : Parcelable