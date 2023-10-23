package com.example.testing.data.api.model.response


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class AttributesCategory(
    @SerializedName("blibli_category_ids")
    val blibliCategoryIds: String = "",
    @SerializedName("meta_description")
    val metaDescription: String = "",
    @SerializedName("meta_keywords")
    val metaKeywords: String = "",
    @SerializedName("meta_title")
    val metaTitle: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("seo_url")
    val seoUrl: String = "",
    @SerializedName("tokopedia_category_ids")
    val tokopediaCategoryIds: String = ""
) : Parcelable