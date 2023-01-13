package com.example.testing.data.api.model.shoppingCart

data class CartList(
    val slug: String? = "",
    val name: String? = "",
    val thumbnailUrl: String? = "",
    val minPrice: Int? = 0,
    val quantity: Int? = 0
)
