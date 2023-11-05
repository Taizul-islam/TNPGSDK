package com.technonext.payment.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat.startActivityForResult
import com.technonext.payment.model.Customer
import com.technonext.payment.model.Url
import com.technonext.payment.view.PaymentActivity


class TechnoNextPaymentGateway{




    companion object{
        val URL="url"
        val AMOUNT="amount"
        val CUSTOMER="customer"
        val SDKTYPE="sdk_type"
       fun pay(context: Context, amount:String, customer: Customer, url: Url) {
           val activity = context as Activity
           Log.d("paybtn", "pay: ${customer.mobileNo}")
           val intent = Intent(context, PaymentActivity::class.java)
           intent.putExtra("amount",amount)
           intent.putExtra("customer",customer)
           intent.putExtra("url",url)
           val bundle = Bundle()
           startActivityForResult(activity,intent,1,bundle)
       }
    }



}