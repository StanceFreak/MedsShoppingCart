package com.example.testing.data.api.model.response


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class MedicineDetail(
    @SerializedName("external_id")
    val externalId: String,
    @SerializedName("canon_slug")
    val canonSlug: String,
    @SerializedName("general_indication")
    val generalIndication: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("composition")
    val composition: String,
    @SerializedName("dosage")
    val dosage: String,
    @SerializedName("how_to_use")
    val howToUse: String,
    @SerializedName("packaging")
    val packaging: String,
    @SerializedName("side_effects")
    val sideEffects: String,
    @SerializedName("contraindication")
    val contraindication: String,
    @SerializedName("warning")
    val warning: String,
    @SerializedName("segmentation")
    val segmentation: String,
    @SerializedName("manufacturer_name")
    val manufacturerName: String,
    @SerializedName("meta_keywords")
    val metaKeywords: String,
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("thumbnail_url")
    val thumbnailUrl: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("bpom_number")
    val bpomNumber: String,
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
    @SerializedName("categories")
    val categories: List<CategoryMeds>
) : Parcelable