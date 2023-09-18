package com.technonext.payment.view

import HomeViewModel
import SessionManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cheezycode.randomquote.viewmodels.HomeViewModelFactory
import com.technonext.payment.R
import com.technonext.payment.model.Customer
import com.technonext.payment.model.Url
import com.technonext.payment.utils.App


class HomeActivity : AppCompatActivity() {
    lateinit var mainViewModel: HomeViewModel

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val toolbar: Toolbar = findViewById(com.technonext.payment.R.id.toolbar)
        setSupportActionBar(toolbar)
        val amount=intent.getStringExtra("amount")
        val customer=intent.getSerializableExtra("customer", Customer::class.java)
        val url=intent.getSerializableExtra("url",Url::class.java)
        Log.d("amount", "onCreate: $amount")
        Log.d("customer", "onCreate: ${customer?.mobileNo}")
        Log.d("url", "onCreate: ${url?.successUrl}")
        val repository = (application as App).quoteRepository

        mainViewModel = ViewModelProvider(this, HomeViewModelFactory(repository)).get(HomeViewModel::class.java)
        mainViewModel.login()
        mainViewModel.token.observe(this, Observer {
            val sessionManager=SessionManager(this)
            sessionManager.saveAuthToken(it)
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            if(it.isNotEmpty()){
                mainViewModel.makeOrder()

            }
        })

        mainViewModel.orderResponse.observe(this) {
            Log.d("URLRYY", "onCreate: ${it.checkoutUrl}")
            val i = Intent(this, WebActivity::class.java)
            i.putExtra("url",it.checkoutUrl)
            i.putExtra("allurl",url)
            i.putExtra("order_id",it.paymentDetail.paymentOrderId)
            startActivity(i)
        }

    }

    fun onClick(view: View) {
        Log.d("TAG", "onClick: ${view.id}")
        val intent =  Intent()
        val sessionManager=SessionManager(this)
        intent.putExtra("value", "${sessionManager.fetchAuthToken()}")
        setResult(RESULT_CANCELED, intent)
        finish()





    }

    @Deprecated("Deprecated in Java",
        ReplaceWith("super.onBackPressed()", "androidx.appcompat.app.AppCompatActivity")
    )
    override fun onBackPressed() {
        return
    }


}