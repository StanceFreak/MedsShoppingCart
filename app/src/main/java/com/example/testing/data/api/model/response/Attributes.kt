package com.example.testing.data.api.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Attributes(
    @SerializedName("alt_text")
    val altText: String = "",
    @SerializedName("image_url")
    val imageUrl: String = "",
    @SerializedName("image_url_webp")
    val imageUrlWebp: String = "",
    @SerializedName("thumbnail_url")
    val thumbnailUrl: String = "",
    @SerializedName("thumbnail_url_webp")
    val thumbnailUrlWebp: String = ""
) : Parcelable