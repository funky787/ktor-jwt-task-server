package org.example.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val login: String,
    val password: String
)

@Serializable
data class LoginRequest(
    val login: String,
    val password: String
)

@Serializable
data class UserResponse(
    val id: Int,
    val login: String
)

@Serializable
data class TokenResponse(
    val token: String,
    val expiresInSeconds: Long,
    val userId: Int
)

@Serializable
data class ErrorResponse(
    val message: String
)

@Serializable
data class MessageResponse(
    val message: String
)
