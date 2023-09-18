package com.technonext.payment.utils

import SessionManager
import android.app.Application
import com.cheezycode.randomquote.api.RetrofitHelper
import com.cheezycode.randomquote.repository.HomeRepository
import com.cheezycode.randomquote.repository.WebRepository
import com.technonext.payment.api.ApiService

class App :Application(){
    lateinit var quoteRepository: HomeRepository
    lateinit var webRepository: WebRepository

    override fun onCreate() {
        super.onCreate()
        initialize()
    }

    private fun initialize() {
        val sessionManager=SessionManager(applicationContext)
        val quoteService = RetrofitHelper.getInstance(sessionManager).create(ApiService::class.java)
        quoteRepository = HomeRepository(quoteService, applicationContext)
        webRepository = WebRepository(quoteService, applicationContext)
    }
}
