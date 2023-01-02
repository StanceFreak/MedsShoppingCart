package com.example.testing.data.api.model


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Result(
    @SerializedName("external_id")
    val externalId: String,
    @SerializedName("slug")
    val slug: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("thumbnail_url")
    val thumbnailUrl: String,
    @SerializedName("min_price")
    val minPrice: Int,
    @SerializedName("base_price")
    val basePrice: Int,
    @SerializedName("selling_unit")
    val sellingUnit: String,
    @SerializedName("prescription_required")
    val prescriptionRequired: Boolean,
    @SerializedName("consultation_required")
    val consultationRequired: Boolean,
    @SerializedName("controlled_substance_type")
    val controlledSubstanceType: String,
    @SerializedName("visual_cues")
    val visualCues: List<String>,
    @SerializedName("images_map")
    val imagesMap: ImagesMap,
) : Parcelable