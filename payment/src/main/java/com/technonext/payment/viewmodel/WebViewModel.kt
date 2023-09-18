
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cheezycode.randomquote.repository.HomeRepository
import com.cheezycode.randomquote.repository.WebRepository
import com.technonext.payment.model.Customer
import com.technonext.payment.model.Login
import com.technonext.payment.model.OrderModel
import com.technonext.payment.model.OrderResponse
import com.technonext.payment.model.PaymentResponse
import com.technonext.payment.model.VerifyPaymentModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class WebViewModel(private val repository: WebRepository) : ViewModel() {


    val paymentResponse : LiveData<PaymentResponse>
        get() = repository.paymentResponse

    fun makeOrder(id:Int){
        val paymentModel =VerifyPaymentModel(id)
        viewModelScope.launch(Dispatchers.IO){
            repository.verifyPayment(paymentModel)
        }
    }



}


