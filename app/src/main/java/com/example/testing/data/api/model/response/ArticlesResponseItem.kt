package com.example.testing.data.api.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ArticlesResponseItem(
    @SerializedName("attributes")
    val attributes: Attributes = Attributes(),
    @SerializedName("categories")
    val categories: List<CategoryArticle> = listOf(),
    @SerializedName("channel")
    val channel: String = "",
    @SerializedName("display_order")
    val displayOrder: Int = 0,
    @SerializedName("external_id")
    val externalId: String = "",
    @SerializedName("headline")
    val headline: String = "",
    @SerializedName("image_url")
    val imageUrl: String = "",
    @SerializedName("language")
    val language: String = "",
    @SerializedName("meta_description")
    val metaDescription: String = "",
    @SerializedName("meta_keywords")
    val metaKeywords: String = "",
    @SerializedName("publish_date")
    val publishDate: Long = 0,
    @SerializedName("reading_time")
    val readingTime: String = "",
    @SerializedName("slug")
    val slug: String = "",
    @SerializedName("source_url")
    val sourceUrl: String = "",
    @SerializedName("summary")
    val summary: String = "",
    @SerializedName("thumbnail_url")
    val thumbnailUrl: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("type")
    val type: String = "",
    @SerializedName("updated_at")
    val updatedAt: Long = 0
) : Parcelable