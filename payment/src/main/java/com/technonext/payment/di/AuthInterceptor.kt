package com.technonext.payment.di

import SessionManager
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Interceptor.*
import okhttp3.Request
import okhttp3.Response
import java.io.IOException


class AuthInterceptor(private val sessionManager: SessionManager): Interceptor {

    override fun intercept(chain: Chain): Response {
        val request = chain.request()
        Log.d("TOKEN", "intercept: ${sessionManager.fetchAuthToken()}")
        val builder = request.newBuilder()
            .addHeader("Accept", "application/json")
            .addHeader("content-type", "application/json")
            .addHeader("Authorization", "Bearer ${sessionManager.fetchAuthToken()}")
        val newRequest: Request = builder.build()
        return chain.proceed(newRequest)
    }
}
