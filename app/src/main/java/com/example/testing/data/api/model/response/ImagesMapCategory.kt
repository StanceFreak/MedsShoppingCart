package com.example.testing.data.api.model.response


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class ImagesMapCategory(
    @SerializedName("image_url")
    val imageUrl: List<ImageUrl> = listOf()
) : Parcelable