package com.example.testing.data.api.model


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class MedicineListModel(
    @SerializedName("result")
    val result: List<Result>,
    @SerializedName("next_page")
    val nextPage: Boolean,
) : Parcelable