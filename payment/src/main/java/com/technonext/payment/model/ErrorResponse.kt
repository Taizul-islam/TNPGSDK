package com.technonext.payment.model

data class ErrorResponse(
    val apiErrors: List<ApiError>
)