package com.example.testing.data.api.model


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class ThumbnailUrl(
    @SerializedName("extension")
    val extension: String,
    @SerializedName("url")
    val url: String
) : Parcelable