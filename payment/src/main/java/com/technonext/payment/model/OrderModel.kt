package com.technonext.payment.model

data class OrderModel(
    val amount: String,
    val cancelUrl: String,
    val cardType: Int,
    val channel: Int,
    val clientType: String,
    val currency: String,
    val customer: Customer,
    val failureUrl: String,
    val successUrl: String,
    val trackingId: String,
    val udf1: String,
    val udf2: String,
    val udf3: String
)