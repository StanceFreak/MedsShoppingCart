package com.example.testing.data.api.model.response


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class CategoryListResponseItem(
    @SerializedName("attributes")
    val attributes: AttributesCategory = AttributesCategory(),
    @SerializedName("canon_slug")
    val canonSlug: String? = null,
    @SerializedName("external_id")
    val externalId: String = "",
    @SerializedName("image_url")
    val imageUrl: String = "",
    @SerializedName("images_map")
    val imagesMap: ImagesMap = ImagesMap(),
    @SerializedName("slug")
    val slug: String = ""
) : Parcelable