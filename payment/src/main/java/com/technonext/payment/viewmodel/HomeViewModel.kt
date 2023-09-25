
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cheezycode.randomquote.repository.HomeRepository
import com.technonext.payment.model.ApiError
import com.technonext.payment.model.Card
import com.technonext.payment.model.Customer
import com.technonext.payment.model.Login
import com.technonext.payment.model.OrderModel
import com.technonext.payment.model.OrderResponse
import com.technonext.payment.model.Url
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HomeViewModel(private val repository: HomeRepository) : ViewModel() {

    fun login(){
        val login=Login("FIRSTTRIP","FIRSTTRIP@1")
        viewModelScope.launch(Dispatchers.IO){
           repository.login(login)
        }
    }

    fun getCardList(){
        val login=Login("FIRSTTRIP","FIRSTTRIP@1")
        viewModelScope.launch(Dispatchers.IO){
            repository.getCardList()
        }
    }

    var token : MutableLiveData<String?>? =null
        get() = repository.token

    var orderResponse : MutableLiveData<OrderResponse?>? =null
        get() = repository.orderResponse
    var cardListResponse : MutableLiveData<Card?>? =null
        get() = repository.cardListResponse
    var errorResponse : MutableLiveData<List<ApiError>?>? =null
        get() = repository.errorResponse

    var execption : MutableLiveData<String?>? =null
        get() = repository.exception


    fun makeOrder(cardType:Int,channel:Int,customer: Customer,url: Url,amount:String){

        val orderModel=OrderModel(
            amount,
            url.errorUrl,
            cardType,
            null,
            "PC_WEB",
            "050",
            customer,
            url.failureUrl,
            url.successUrl,
            getRandomString(),
            "12df",
            "12asaS",
            "12saSa",

        )
        viewModelScope.launch(Dispatchers.IO){
            repository.saveOrder2(orderModel)
        }

    }
    private fun clear(){
        repository.clear()
        token=null
        orderResponse=null
        cardListResponse=null
        errorResponse=null
    }


    private fun getRandomString() : String {
        val allowedChars = ('0'..'9')
        return (1..4)
            .map { allowedChars.random() }
            .joinToString("")
    }



    override fun onCleared() {
        super.onCleared()
        clear()

    }



}


