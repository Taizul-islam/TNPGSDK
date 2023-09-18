package com.technonext.payment.api

import com.technonext.payment.model.Login
import com.technonext.payment.model.LoginResponse
import com.technonext.payment.model.OrderModel
import com.technonext.payment.model.OrderResponse
import com.technonext.payment.model.PaymentResponse
import com.technonext.payment.model.VerifyPaymentModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @POST("/processing/service/p/api/auth/signin")
    suspend fun login(@Body login:Login): Response<LoginResponse>

    @POST("/processing/service/api/payment/order/save")
    suspend fun saveOrder1(@Body orderModel: OrderModel): Response<OrderResponse>

    @POST("/processing/service/api/payment/order/verify")
    suspend fun verifyOrder(@Body verify: VerifyPaymentModel): Response<PaymentResponse>

}