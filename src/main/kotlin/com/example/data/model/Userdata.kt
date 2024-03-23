package com.example.data.model

import io.ktor.server.auth.*
import kotlinx.serialization.Serializable

@Serializable
data class Userdata(
    val email: String,
    val name: String,
    val hashpassword: String
):Principal