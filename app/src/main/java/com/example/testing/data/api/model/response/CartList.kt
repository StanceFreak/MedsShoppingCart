package com.example.testing.data.api.model.response

data class CartList(
    val slug: String? = "",
    val name: String? = "",
    val thumbnailUrl: String? = "",
    val minPrice: Int? = 0,
    val quantity: Int? = 0
)
