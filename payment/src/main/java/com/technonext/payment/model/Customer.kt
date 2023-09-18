package com.technonext.payment.model

import java.io.Serializable

data class Customer(
    val billAddressCity: String,
    val billAddressCountry: String,
    val billAddressLine: String,
    val billAddressPostalCode: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val mobileNo: String
):Serializable