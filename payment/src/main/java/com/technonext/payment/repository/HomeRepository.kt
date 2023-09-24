package com.cheezycode.randomquote.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.cheezycode.randomquote.utils.NetworkUtils
import com.technonext.payment.api.ApiService
import com.technonext.payment.model.Card
import com.technonext.payment.model.Login
import com.technonext.payment.model.OrderModel
import com.technonext.payment.model.OrderResponse

class HomeRepository(
    private val apiService: ApiService,
    private val applicationContext: Context
) {
    public val token = MutableLiveData<String?>()
    public val orderResponse = MutableLiveData<OrderResponse?>()
    public val cardListResponse = MutableLiveData<Card?>()
    suspend fun login(login: Login){

        if(NetworkUtils.isInternetAvailable(applicationContext)){
            val result = apiService.login(login)
            Log.d("BODY", "login: ${result.body()}")
            if(result.body() != null){
                token.postValue(result.body()?.token)
            }else{
                token.postValue("")
            }
        }
        else{
            Toast.makeText(applicationContext,"No Internet",Toast.LENGTH_LONG).show()
        }

    }
    suspend fun getCardList(){

        if(NetworkUtils.isInternetAvailable(applicationContext)){
            val result = apiService.getCardList()
            Log.d("BODY", "cardlist: ${result.body()}")
            if(result.body() != null){
                cardListResponse.postValue(result.body())
            }else{
            }
        }
        else{
            Toast.makeText(applicationContext,"No Internet",Toast.LENGTH_LONG).show()
        }

    }
    suspend fun saveOrder2(orderModel: OrderModel){

        if(NetworkUtils.isInternetAvailable(applicationContext)){
            val result = apiService.saveOrder1(orderModel)

            Log.d("BODY", "order: ${result.body()}")
            Log.d("Headers", "order: ${result.headers()}")
            Log.d("Headers", "order: ${result.code()}")
            if(result.body() != null){
                orderResponse.postValue(result.body())
            }else{
                Log.d("BODY", "saveOrder: ${result.body()}")
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

    }
}