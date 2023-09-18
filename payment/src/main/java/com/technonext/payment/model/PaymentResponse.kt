package com.technonext.payment.model

import java.io.Serializable

data class PaymentResponse(
    val amount: String,
    val approveDatetime: String,
    val cardType: Int,
    val channel: Int,
    val currencyCode: String,
    val currencyId: Int,
    val currencyNumber: String,
    val email: String,
    val firstName: Any,
    val lastName: Any,
    val merchantId: Int,
    val merchantName: Any,
    val merchantRedirected: Boolean,
    val mobileNo: String,
    val paymentId: String,
    val paymentOrderId: Int,
    val statusCode: Int,
    val statusCodeTxt: String,
    val trackingId: String,
    val txnId: String,
    val udf1: String,
    val udf2: String,
    val udf3: String
):Serializable