package com.example.testing.data.api.model.response


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class ImageUrl(
    @SerializedName("extension")
    val extension: String? = "",
    @SerializedName("url")
    val url: String? = ""
) : Parcelable