package com.cheezycode.randomquote.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.technonext.payment.api.ApiService
import com.technonext.payment.model.ApiError
import com.technonext.payment.model.Card
import com.technonext.payment.model.Login
import com.technonext.payment.model.OrderModel
import com.technonext.payment.model.OrderResponse
import java.io.IOException
import java.lang.Exception
import java.net.SocketException
import java.util.concurrent.TimeoutException

class HomeRepository(
    private val apiService: ApiService,
    private val applicationContext: Context
) {
    val token = MutableLiveData<String?>()
    val exception = MutableLiveData<String?>()
    val orderResponse = MutableLiveData<OrderResponse?>()
    val cardListResponse = MutableLiveData<Card?>()
    val errorResponse = MutableLiveData<List<ApiError>?>()
    suspend fun login(login: Login){
        if(NetworkUtils.isAvailableNetwork(applicationContext)){

            try {
                val result = apiService.login(login)
                Log.d("BODY", "login: ${result.body()}")

                if(result.code()==200){
                    token.postValue(result.body()?.token)
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
    suspend fun getCardList(){

        if(NetworkUtils.isAvailableNetwork(applicationContext)){
            try {
                val result = apiService.getCardList()
                Log.d("BODY", "cardlist: ${result.body()}")
                if(result.code()==200){
                    cardListResponse.postValue(result.body())
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
    suspend fun saveOrder2(orderModel: OrderModel){

        if(NetworkUtils.isAvailableNetwork(applicationContext)){
            try {
                val result = apiService.saveOrder1(orderModel)
                if(result.code()==200){
                    orderResponse.postValue(result.body())
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
        token.value=null
        orderResponse.value=null
        cardListResponse.value=null
        errorResponse.value=null
        exception.value=null

    }
    private fun exception(e: Exception){
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