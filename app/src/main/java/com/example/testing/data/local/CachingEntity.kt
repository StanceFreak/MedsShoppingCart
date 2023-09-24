package com.example.testing.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.testing.data.api.model.response.ImagesMap

@Entity
data class CachingEntity(
    @PrimaryKey
    val externalId: String,
    val slug: String,
    val name: String,
    val imageUrl: String,
    val thumbnailUrl: String,
    val minPrice: Int,
    val basePrice: Int,
    val sellingUnit: String,
    val prescriptionRequired: Boolean,
    val consultationRequired: Boolean,
    val controlledSubstanceType: String,
    val visualCues: List<String>,
    val imagesMap: ImagesMap,
)
