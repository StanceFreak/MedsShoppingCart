package com.example.testing.data.api.model.response


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class ImagesMap(
    @SerializedName("image_url")
    val imageUrl: List<ImageUrl>? = null,
    @SerializedName("thumbnail_url")
    val thumbnailUrl: List<ThumbnailUrl>? = null
) : Parcelable