package com.cheezycode.randomquote.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.technonext.payment.api.ApiService
import com.technonext.payment.model.ApiError
import com.technonext.payment.model.Login
import com.technonext.payment.model.OrderModel
import com.technonext.payment.model.OrderResponse
import com.technonext.payment.model.PaymentResponse
import com.technonext.payment.model.VerifyPaymentModel
import java.io.IOException
import java.net.SocketException
import java.util.concurrent.TimeoutException

class WebRepository(
    private val apiService: ApiService,
    private val applicationContext: Context
) {
    public var paymentResponse = MutableLiveData<PaymentResponse?>()
    val errorResponse = MutableLiveData<List<ApiError>?>()
    val exception = MutableLiveData<String?>()
    suspend fun verifyPayment(paymentModel: VerifyPaymentModel){

        if(NetworkUtils.isAvailableNetwork(applicationContext)){
            try {
                val result = apiService.verifyOrder(paymentModel)
                Log.d("BODY", "login: ${result.body()}")
                if(result.code()==200){
                    paymentResponse.postValue(result.body())
                }else{
                    errorResponse.postValue(result.body()?.apiErrors)
                }
            }catch (e:Exception){
                exception(e)
            }

        }
        else{
            Toast.makeText(applicationContext,"No Internet",Toast.LENGTH_LONG).show()
        }

    }
    fun clear(){
        paymentResponse.value=null
        errorResponse.value=null
    }
    private fun exception(e: java.lang.Exception){
        Log.d("ERROROFLOGIN", "login: ${e}")
        when (e) {
            is SocketException -> {
                exception.postValue("Server down")
            }

            is TimeoutException -> {
                exception.postValue("Time out Please try again")
            }

            is IOException -> {
                exception.postValue("Time out Please try again")
            }

            else -> {
                exception.postValue("Something went wrong")
            }
        }
    }

}