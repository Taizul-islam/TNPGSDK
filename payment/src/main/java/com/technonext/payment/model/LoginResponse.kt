package com.technonext.payment.model

data class LoginResponse(
    val expirationTime: String,
    val merchantId: String,
    val token: String,
    val type: String,
    val apiErrors: List<ApiError>
)

