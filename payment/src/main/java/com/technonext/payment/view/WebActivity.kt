package com.technonext.payment.view

import com.technonext.payment.viewmodel.WebViewModel
import WebViewModelFactory
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModelProvider
import com.technonext.payment.R
import com.technonext.payment.model.StatusCode
import com.technonext.payment.model.Url
import com.technonext.payment.utils.App
import com.technonext.payment.utils.Common
import java.io.Serializable
import java.util.regex.Pattern


class WebActivity : AppCompatActivity() {

    lateinit var mainViewModel: WebViewModel
    private val list = arrayListOf(
        StatusCode("4001", "APPROVED"),
        StatusCode("4002", "DECLINED"),
        StatusCode("4003", "CANCELLED"),
        StatusCode("4004", "UPSTREAM_ERROR"),
        StatusCode("4005", "CLIENT_ERROR"),
        StatusCode("4006", "GATEWAY_ERROR"),
        StatusCode("4007", "CONFLICTING_UPSTREAM_RESPONSE"),
        StatusCode("4008", "REFUNDED"),
        StatusCode("4009", "PARTIALLY_REFUNDED"),
        StatusCode("4010", "EXPIRED"),
        StatusCode("4011", "TIMEOUT"),
        StatusCode("4000", "REQUESTED"),
    )
    var orderid = 1
    private var bankPage: WebView? = null

    var bankPageProgress: ProgressBar? = null
    private var redirectUrl: String? = null
    var allurl: Url? = null

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        setStatusBarColor(R.color.white)
        redirectUrl = intent.getStringExtra("url")
        orderid = intent.getIntExtra("order_id", 0)
        Log.d("OrderId", "onCreate:")
        allurl = intent.getSerializableExtra("allurl", Url::class.java)
        val repository = (application as App).webRepository
        val back: ImageView = findViewById(R.id.ivBack)
        val tvTitle: TextView = findViewById(R.id.tvTitle)
        back.setOnClickListener {
            finish()
        }
        tvTitle.text = Common.SELECTED_TITLE
        mainViewModel =
            ViewModelProvider(this, WebViewModelFactory(repository)).get(WebViewModel::class.java)
        bankPage = findViewById(R.id.bankPage)
        bankPageProgress = findViewById(R.id.bankPageProgress)


        redirectUrl?.let { showTheWebsite(it) }

        mainViewModel.errorResponse?.observe(this) {
            if (it != null) {
                if (it.isNotEmpty()) {
                    Toast.makeText(this, it[0].message, Toast.LENGTH_LONG).show()
                }
            }
        }
        mainViewModel.execption?.observe(this) {
            if (it != null) {
                if (it.isNotEmpty()) {
                    if (it.isNotEmpty()) {
                        val builder = AlertDialog.Builder(this@WebActivity)
                        builder.setMessage(it.toString())
                        builder.setTitle("Alert")
                        builder.setPositiveButton(
                            "Ok"
                        ) { dialog, which -> dialog.dismiss() }

                        builder.create().show()
                    }
                }
            }
        }

        mainViewModel.paymentResponse?.observe(this) {
            if (it != null) {
                if (it.txnId.isNotEmpty()) {
                    Log.d("Executed", "onCreate: ${it.txnId}")
                    val intent = Intent("finish_activity")
                    intent.putExtra("response", it)
                    sendBroadcast(intent)
                    finish()


                } else {
                    val intent = Intent()
                    intent.putExtra("response", it)
                    setResult(2, intent)
                }
            }
        }
    }

    private fun showTheWebsite(url: String) {
        val webViewClient: WebViewClient = object : WebViewClient() {
            @Deprecated("Deprecated in Java")
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }


            override fun onPageFinished(view: WebView, url: String) {
                Log.e("log", "onPageFinished: $url")
                if (allurl?.let { url.contains(it.successUrl) } == true)
                    checkStatus(url)

            }

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
        bankPage!!.loadUrl(url)
        bankPage!!.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                this@WebActivity.bankPageProgress?.progress = newProgress
            }
        }
    }

    fun checkStatus(weburl: String) {
        val list = weburl.split("?")
        val statusCode = list[1].split("&")[0].split("=")[1]
        Log.d("STATUS Code", "onPageFinished:status code ${statusCode}")
        val message = message(statusCode)
        if (statusCode == "4001") {
            mainViewModel.makeOrder(orderid)
        } else {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            finish()

        }
    }

    private fun message(code: String): String {
        var value = ""
        list.forEach {
            if (it.code == code) {
                value = it.status
            }
        }
        if (value.isNotEmpty()) {
            return value
        } else {
            return "Something wrong"
        }

    }

    fun isURLString(url: String?): Boolean {
        return url?.let {
            Pattern.compile("^((http?|ftp)://|(www|ftp)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$")
                .matcher(it).find()
        } == true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home ->             //do whatever
                true

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun Activity.setStatusBarColor(colorId: Int) {
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true
        window.statusBarColor = ContextCompat.getColor(this, colorId)
        if (colorId == R.color.black)
            WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars =
                false
    }
}


