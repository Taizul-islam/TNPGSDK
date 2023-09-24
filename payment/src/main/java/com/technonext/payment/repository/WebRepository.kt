package com.cheezycode.randomquote.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.cheezycode.randomquote.utils.NetworkUtils
import com.technonext.payment.api.ApiService
import com.technonext.payment.model.Login
import com.technonext.payment.model.OrderModel
import com.technonext.payment.model.OrderResponse
import com.technonext.payment.model.PaymentResponse
import com.technonext.payment.model.VerifyPaymentModel

class WebRepository(
    private val apiService: ApiService,
    private val applicationContext: Context
) {
    public var paymentResponse = MutableLiveData<PaymentResponse?>()
    suspend fun verifyPayment(paymentModel: VerifyPaymentModel){

        if(NetworkUtils.isInternetAvailable(applicationContext)){
            val result = apiService.verifyOrder(paymentModel)
            Log.d("BODY", "login: ${result.body()}")
            if(result.body() != null){
                paymentResponse.postValue(result.body())
            }else{

            }
        }
        else{
            Toast.makeText(applicationContext,"No Internet",Toast.LENGTH_LONG).show()
        }

    }
    fun clear(){
        paymentResponse.value=null
    }

}