package com.technonext.payment.view

import WebViewModel
import WebViewModelFactory
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.technonext.payment.R
import com.technonext.payment.model.StatusCode
import com.technonext.payment.model.Url
import com.technonext.payment.utils.App
import java.util.regex.Pattern

class WebActivity : AppCompatActivity() {

    lateinit var mainViewModel: WebViewModel
    private val list= arrayListOf(
        StatusCode("4001","APPROVED"),
        StatusCode("4002","DECLINED"),
        StatusCode("4003","CANCELLED"),
        StatusCode("4004","UPSTREAM_ERROR"),
        StatusCode("4005","CLIENT_ERROR"),
        StatusCode("4006","GATEWAY_ERROR"),
        StatusCode("4007","CONFLICTING_UPSTREAM_RESPONSE"),
        StatusCode("4008","REFUNDED"),
        StatusCode("4009","PARTIALLY_REFUNDED"),
        StatusCode("4010","EXPIRED"),
        StatusCode("4011","TIMEOUT"),
        StatusCode("4000","REQUESTED"),
    )
    var orderid=""
    private val OTP_RESEND_TIMER = 30
    private var bankPage: WebView? = null

    /* access modifiers changed from: private */
    var bankPageProgress: ProgressBar? = null

    private val mainLyout1: LinearLayout? = null
    private val merchantName: String? = null
    var motoEnable = false
    private var redirectUrl: String? = null
    var sessionKey: String? = null
    private val timeOutValue: String? = null
    private val timer: CountDownTimer? = null
    private val timerText: TextView? = null
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        redirectUrl=intent.getStringExtra("url")
        orderid= intent.getStringExtra("order_id").toString()
        Log.d("OrderId", "onCreate:")
        val allurl=intent.getSerializableExtra("allurl", Url::class.java)
        val repository = (application as App).webRepository

        mainViewModel = ViewModelProvider(this, WebViewModelFactory(repository)).get(WebViewModel::class.java)
        bankPage = findViewById(R.id.bankPage) as WebView
        bankPageProgress = findViewById(R.id.bankPageProgress) as ProgressBar
        setSupportActionBar(findViewById(R.id.toolbar))
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setHomeButtonEnabled(true)
            supportActionBar!!.setTitle(merchantName)
        }

        redirectUrl?.let { showTheWebsite(it) }



        mainViewModel.paymentResponse.observe(this,Observer{
            if(it.txnId.isNotEmpty()){
                val intent =  Intent()
                intent.putExtra("response", it)
                setResult(RESULT_OK, intent)
                finish()
                finish()
            }else{
                val intent =  Intent()
                intent.putExtra("response", it)
                setResult(2, intent)
                finish()
                finish()
            }
        })
    }
     fun showTheWebsite(url: String) {
        val webViewClient: WebViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }

            override fun onPageFinished(view: WebView, url: String) {
                Log.e("log", "onPageFinished: $url")

            }

            /* JADX WARNING: type inference failed for: r2v0, types: [android.content.Context, com.sslwireless.sslcommerzlibrary.view.activity.WebViewActivity] */
            @SuppressLint("NewApi")
            override fun onReceivedSslError(
                view: WebView,
                handler: SslErrorHandler,
                error: SslError
            ) {
                val builder = AlertDialog.Builder(this@WebActivity)
                builder.setMessage("R.string.notification_error_ssl_cert_invalid")
                builder.setPositiveButton(
                    "Ok"
                ) { dialog, which -> handler.proceed() }
                builder.setNegativeButton(
                    "Cancel"
                ) { dialog, which -> handler.cancel() }
                builder.create().show()
            }
        }
        bankPage!!.settings.loadsImagesAutomatically = true
        bankPage!!.settings.javaScriptEnabled = true
        bankPage!!.settings.domStorageEnabled = true
        bankPage!!.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        bankPage!!.webViewClient = webViewClient
        if (isURLString(redirectUrl)) {
            bankPage!!.loadUrl(url)
        } else {
            bankPage!!.loadData(url, "text/html;", "UTF-8")
        }
        bankPage!!.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                this@WebActivity.bankPageProgress?.progress = newProgress
            }
        }
    }
    fun checkStatus(weburl:String){
        val list=weburl.split("?")
        val statusCode=list[1].split("&")[0].split("=")[1]
        Log.d("STATUS Code", "onPageFinished:status code ${statusCode}")
        val message=message(statusCode)

        if(statusCode=="4001"){
            mainViewModel.makeOrder(orderid.toInt())
        }else{
            Toast.makeText(this,message,Toast.LENGTH_LONG).show()
            finish();

        }
    }

    private fun message(code:String):String{
        var value=""
       list.forEach {
           if(it.code == code){
             value=it.status
           }
       }
        if(value.isNotEmpty()){
            return  value
        }else{
            return "Something wrong"
        }

    }
    fun isURLString(url: String?): Boolean {
        return if (Pattern.compile("^((https?|ftp)://|(www|ftp)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$")
                .matcher(url).find()
        ) {
            true
        } else false
    }
}