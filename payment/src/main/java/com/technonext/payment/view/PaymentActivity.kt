package com.technonext.payment.view

import HomeViewModel
import SessionManager
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.cheezycode.randomquote.viewmodels.HomeViewModelFactory
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.technonext.payment.R
import com.technonext.payment.adapter.AdapterTabPager
import com.technonext.payment.fragment.CardFragment
import com.technonext.payment.fragment.MobileFragment
import com.technonext.payment.fragment.NetFragment
import com.technonext.payment.model.Customer
import com.technonext.payment.model.PaymentResponse
import com.technonext.payment.model.Url
import com.technonext.payment.utils.App
import com.technonext.payment.utils.Common
import com.technonext.payment.utils.SDKType


class PaymentActivity : AppCompatActivity() {
    private lateinit var mainViewModel: HomeViewModel
    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        override fun onReceive(arg0: Context, intent: Intent) {
            val action = intent.action
            if (action == "finish_activity") {
                val response=intent.getSerializableExtra("response", PaymentResponse::class.java)
                val i=Intent()
                i.putExtra("response",response)
                viewModelStore.clear()
                setResult(RESULT_OK,i)
                this@PaymentActivity.finish()

            }

        }
    }
    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setStatusBarColor(R.color.white)
        val progressBar:ProgressBar=findViewById(R.id.progressBar)
        val tab: TabLayout = findViewById(R.id.tabs)
        val tvPayAmount: TextView = findViewById(R.id.tvPayAmount)
        val ivBack: ImageView = findViewById(R.id.ivBack)
        val tvLiveStatus: TextView = findViewById(R.id.tvLiveStatus)
        val btnPayNow: LinearLayout = findViewById(R.id.btnPayNow)
        val viewPager: ViewPager2 = findViewById(com.technonext.payment.R.id.viewPager)
        val repository = (application as App).quoteRepository

        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setHomeButtonEnabled(true)
        }

        mainViewModel = ViewModelProvider(this, HomeViewModelFactory(repository))[HomeViewModel::class.java]
        val amount=intent.getStringExtra("amount")
        val customer=intent.getSerializableExtra("customer", Customer::class.java)
        val url=intent.getSerializableExtra("url",Url::class.java)
        Common.sdk= intent.getSerializableExtra("sdk_type",SDKType::class.java)!!
        tvLiveStatus.text=if(Common.sdk==SDKType.TEST) "Test Mode" else ""
        Log.d("MAINACTIVITY", "onCreate: ${Common.sdk}")
        Log.d("amount", "onCreate: $amount")
        Log.d("customer", "onCreate: ${customer?.mobileNo}")
        Log.d("url", "onCreate: ${url?.successUrl}")
        if (url != null) {
            if(url.errorUrl.isEmpty()||url.successUrl.isEmpty()||url.failureUrl.isEmpty()){
               Toast.makeText(this,"Please enter all valid URLS",Toast.LENGTH_LONG).show()
                finish()
            }
        }
        if (customer != null) {
            if(customer.billAddressCountry.isEmpty()){
                Toast.makeText(this,"Please enter customer country code",Toast.LENGTH_LONG).show()
                finish()
            }
        }
        val adapter = AdapterTabPager(this)
        adapter.addFragment(CardFragment(), "Card")
        adapter.addFragment(MobileFragment(), "Mobile Banking")
        adapter.addFragment(NetFragment(), "Net Banking")
        viewPager.adapter = adapter
        viewPager.currentItem = 0
        TabLayoutMediator(tab, viewPager) { tabs, position ->
            tabs.text = adapter.getTabTitle(position)
        }.attach()



        tvPayAmount.text="Pay $amount"+" TK"

        btnPayNow.setOnClickListener {
            if(Common.SELECTED_ID==0){
                Toast.makeText(this,"Please select a merchant",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (customer != null) {
                if (url != null) {
                    if (amount != null) {
                        mainViewModel.makeOrder(Common.SELECTED_ID,0,customer,url,amount)
                    }
                }
            }
        }
        ivBack.setOnClickListener {
            finish()
        }
        mainViewModel.login()
        mainViewModel.token?.observe(this, Observer {
            Log.d("TOKEN RETURN", "onCreate: $it")
            val sessionManager=SessionManager(this)
            if (it != null) {

                sessionManager.saveAuthToken(it)
            }
            if (it != null) {
                if(it.isNotEmpty()){
                    mainViewModel.getCardList()

                }
            }
        })
        mainViewModel.errorResponse?.observe(this){
            if (it != null) {
                if(it.isNotEmpty()){
                    progressBar.visibility=View.GONE
                    Toast.makeText(this,it[0].message,Toast.LENGTH_LONG).show()
                }
            }
        }
        mainViewModel.execption?.observe(this){
            if (it != null) {
                if(it.isNotEmpty()){
                    progressBar.visibility=View.GONE
                    if(it.isNotEmpty()){
                        val builder = AlertDialog.Builder(this@PaymentActivity)
                        builder.setMessage(it.toString())
                        builder.setTitle("Alert")
                        builder.setPositiveButton(
                            "Ok"
                        ) { dialog, which -> dialog.dismiss()
                        finish()}

                        builder.create().show()
                    }
                }
            }
        }

        mainViewModel.orderResponse?.observe(this) {
            if (it != null) {
                if(it.checkoutUrl.isNotEmpty()){

                    Log.d("URLRYY", "onCreate: ${it.checkoutUrl}")
                    val i = Intent(this, WebActivity::class.java)
                    i.putExtra("url",it.checkoutUrl)
                    i.putExtra("allurl",url)
                    i.putExtra("order_id",it.paymentDetail.paymentOrderId)
                    startActivity(i)
                }else{
                    Toast.makeText(this,"data have ", Toast.LENGTH_LONG).show()
                    Log.d("TAG", "onCreate: data have")
                }
            }

        }
        mainViewModel.cardListResponse?.observe(this){
            if (it != null) {
                if(it.cardTypeList.isNotEmpty()){
                  progressBar.visibility=View.GONE
                }
            }
        }




        registerReceiver(broadcastReceiver, IntentFilter("finish_activity"))

    }








    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        return
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcastReceiver)
    }

    private fun Activity.setStatusBarColor(colorId: Int) {
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true
        window.statusBarColor = ContextCompat.getColor(this, colorId)

        if (colorId == R.color.black)
            WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars =
                false
    }





}