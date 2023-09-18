package com.technonext.payment.model

data class OrderResponse(
    val checkoutUrl: String,
    val paymentDetail: PaymentDetail
)