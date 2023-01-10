package com.example.testing.data.api.model.medicine


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Category(
    @SerializedName("external_id")
    val externalId: String,
    @SerializedName("slug")
    val slug: String,
    @SerializedName("name")
    val name: String
) : Parcelable