package com.example.testing.data.api.model.response


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class MedicineList(
    @SerializedName("external_id")
    val externalId: String? = "",
    @SerializedName("slug")
    val slug: String? = "",
    @SerializedName("name")
    val name: String? = "",
    @SerializedName("image_url")
    val imageUrl: String? = "",
    @SerializedName("thumbnail_url")
    val thumbnailUrl: String? = "",
    @SerializedName("min_price")
    val minPrice: Int? = 0,
    @SerializedName("base_price")
    val basePrice: Int? = 0,
    @SerializedName("selling_unit")
    val sellingUnit: String? = "",
    @SerializedName("prescription_required")
    val prescriptionRequired: Boolean? = false,
    @SerializedName("consultation_required")
    val consultationRequired: Boolean? = false,
    @SerializedName("controlled_substance_type")
    val controlledSubstanceType: String? = "",
    @SerializedName("visual_cues")
    val visualCues: List<String>? = null,
    @SerializedName("images_map")
    val imagesMap: ImagesMap? = null,
) : Parcelable