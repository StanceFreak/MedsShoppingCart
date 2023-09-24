package com.example.testing.data.api.model.request

data class UserRegisterRequest(
    val id: String? = "",
    val name: String? = "",
    val email: String? = "",
    val pass: String? = ""
)
