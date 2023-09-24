package com.example.testing.data.api.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryArticle(
    @SerializedName("enabled")
    val enabled: Boolean = false,
    @SerializedName("external_id")
    val externalId: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("slug")
    val slug: String = ""
) : Parcelable