package com.technonext.payment.utils

import SessionManager
import android.app.Application
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
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
        val applicationInfo: ApplicationInfo = applicationContext.packageManager
        .getApplicationInfo(applicationContext.packageName, PackageManager.GET_META_DATA)
        Common.LIVE_URL = applicationInfo.metaData["LIVE_API_BASE_URL"].toString()
        Common.TEST_URL= applicationInfo.metaData["TEST_API_BASE_URL"].toString()
        Common.LOGIN_URL= applicationInfo.metaData["LOGIN_URL"].toString()
        Common.VERIFY_BASE_URL= applicationInfo.metaData["VERIFY_BASE_URL"].toString()
        Common.SAVE_URL= applicationInfo.metaData["SAVE_URL"].toString()
        Common.ALL_URL= applicationInfo.metaData["ALL_URL"].toString()
        val sessionManager=SessionManager(applicationContext)
        val quoteService = RetrofitHelper.getInstance(sessionManager).create(ApiService::class.java)
        quoteRepository = HomeRepository(quoteService, applicationContext)
        webRepository = WebRepository(quoteService, applicationContext)
    }
}
