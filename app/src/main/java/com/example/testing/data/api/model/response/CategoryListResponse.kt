package com.example.testing.data.api.model.response


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class CategoryListResponse(
    @SerializedName("results")
    val results: List<CategoryListResponseItem> = listOf()
) : Parcelable