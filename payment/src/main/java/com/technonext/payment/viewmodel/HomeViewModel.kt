
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cheezycode.randomquote.repository.HomeRepository
import com.technonext.payment.model.Customer
import com.technonext.payment.model.Login
import com.technonext.payment.model.OrderModel
import com.technonext.payment.model.OrderResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HomeViewModel(private val repository: HomeRepository) : ViewModel() {

    fun login(){
        val login=Login("FIRSTTRIP","FIRSTTRIP@1")
        viewModelScope.launch(Dispatchers.IO){
           repository.login(login)
        }
    }

    val token : LiveData<String>
        get() = repository.token

    val orderResponse : LiveData<OrderResponse>
        get() = repository.orderResponse

    fun makeOrder(){
        val customer= Customer(
            "Dhaka",
            "BD",
            "12",
            "12",
            "mdtaizulislam50@gmail.com",
            "Taizul",
            "Islam",
            "01723733950"
        )
        val orderModel=OrderModel(
            "10",
            "http://localhost/PaymentGatewayClient/success.php",
            2,
            1,
            "PC_WEB",
            "050",
            customer,
            "http://localhost/PaymentGatewayClient/success.php",
            "http://localhost/PaymentGatewayClient/success.php",
            getRandomString(4),
            "12df",
            "12asaS",
            "12saSa",

        )
        viewModelScope.launch(Dispatchers.IO){
            repository.saveOrder2(orderModel)
        }
    }


    fun getRandomString(length: Int) : String {
        val allowedChars = ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }


}


