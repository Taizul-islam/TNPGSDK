package com.technonext.payment.model

import java.io.Serializable

public data class Url(val successUrl: String,val errorUrl: String,val failureUrl: String,val ipnUrl:String):Serializable

