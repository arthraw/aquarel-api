package com.example.model.dto.userDTO

import com.example.utils.LocalDateSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class User(
    val id: String,
    val username: String,
    val email: String,
    val password: String,
    @Serializable(with = LocalDateSerializer::class)
    val createAccountDate: LocalDate
)
@Serializable
data class PatchUser(
    val username: String? = null,
    val email: String? = null,
    val password: String? = null,
    val apiToken: String
)
@Serializable
data class UserReturns(
    val id: String,
    val username: String,
)
@Serializable
data class UserInsert(
    val username: String,
    val email: String,
    val password: String,
)
@Serializable
data class UserLogin(
    val email: String,
    val password: String,
)