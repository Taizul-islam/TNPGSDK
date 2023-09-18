package com.technonext.payment.model

data class PaymentDetail(
    val amount: String,
    val approveDatetime: Any,
    val authCode: Any,
    val cardNumber: Any,
    val cardType: Any,
    val channel: Any,
    val currencyCode: String,
    val currencyId: Any,
    val currencyNumber: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val merchantId: Int,
    val merchantName: String,
    val merchantRedirected: Boolean,
    val mobileNo: String,
    val paymentId: Any,
    val paymentOrderId: Int,
    val statusCode: Int,
    val statusCodeTxt: String,
    val trackingId: String,
    val txnId: Any,
    val udf1: String,
    val udf2: String,
    val udf3: String
)