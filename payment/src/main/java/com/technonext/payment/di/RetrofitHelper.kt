package com.cheezycode.randomquote.api

import SessionManager
import com.technonext.payment.di.AuthInterceptor
import com.technonext.payment.utils.Common
import com.technonext.payment.utils.Constants
import com.technonext.payment.utils.SDKType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitHelper {
    private val BASE_URL = if(Common.sdk==SDKType.TEST) Constants.testUrl else Constants.liveURL

    fun getInstance(sessionManager: SessionManager) : Retrofit {
        val httpClient = OkHttpClient.Builder()
            .readTimeout(21, TimeUnit.SECONDS)
            .writeTimeout(21, TimeUnit.SECONDS)
            .connectTimeout(21, TimeUnit.SECONDS)
            .addInterceptor(AuthInterceptor(sessionManager))
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })

        val okHttpClient = httpClient.build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }
}