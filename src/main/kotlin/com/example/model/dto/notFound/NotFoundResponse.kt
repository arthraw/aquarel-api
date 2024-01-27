package com.example.model.dto.notFound

import kotlinx.serialization.Serializable

@Serializable
data class NotFoundResponse(
    val message: String,
    val status: Int
)
