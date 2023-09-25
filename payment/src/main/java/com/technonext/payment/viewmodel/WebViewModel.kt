package com.technonext.payment.viewmodel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewModelScope
import com.cheezycode.randomquote.repository.WebRepository
import com.technonext.payment.model.ApiError
import com.technonext.payment.model.PaymentResponse
import com.technonext.payment.model.VerifyPaymentModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


public class WebViewModel(private val repository: WebRepository) : ViewModel() {


    var paymentResponse : MutableLiveData<PaymentResponse?>? =null
        get() = repository.paymentResponse
    var errorResponse : MutableLiveData<List<ApiError>?>? =null
        get() = repository.errorResponse

    var execption : MutableLiveData<String?>? =null
        get() = repository.exception
    fun makeOrder(id:Int){
        val paymentModel =VerifyPaymentModel(id)
        viewModelScope.launch(Dispatchers.IO){
            repository.verifyPayment(paymentModel)
        }
    }

    override fun onCleared() {
        super.onCleared()
        clear()
    }

    private fun clear(){
        repository.clear()
    }





}


