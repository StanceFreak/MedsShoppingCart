package com.example.testing.data.api.model.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryListData(
    val id: Int,
    val title: String,
    val thumbnail: Int,
    val path: String
): Parcelable
