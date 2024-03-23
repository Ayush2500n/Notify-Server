package com.example.data.model

import kotlinx.serialization.Serializable

@Serializable
data class RegistrationRequest(
    val name: String,
    val email: String,
    val password: String
)
