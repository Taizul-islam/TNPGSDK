package com.technonext.payment

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.technonext.payment.model.Customer
import com.technonext.payment.model.Url
import com.technonext.payment.utils.TechnoNextPaymentGateway

class MainActivity : AppCompatActivity()  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btn=findViewById<Button>(R.id.btnPay)
        val customer= Customer(
            "018829209",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
        )
        val url=Url(
            "http://localhost/PaymentGatewayClient/success.php",
            "http://localhost/PaymentGatewayClient/success.php",
            "http://localhost/PaymentGatewayClient/success.php",
            "http://localhost/PaymentGatewayClient/success.php"
        )
        btn.setOnClickListener {
            TechnoNextPaymentGateway.pay(this,"10",customer,url)
//            val intent = Intent(this, HomeActivity::class.java)
//            intent.putExtra("amount","10")
//            intent.putExtra("customer",customer)
//            intent.putExtra("url",url)
//            startForResult.launch(intent)
        }

    }

//    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
//        if (result.resultCode == Activity.RESULT_OK) {
//            val intent = result.data
//            Log.d("value", "data  : ${intent?.getStringExtra("value")}")
//        }
//    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
          if (resultCode == Activity.RESULT_OK) {
            Log.d("value", "data  : ${data?.getStringExtra("value")}")
          }
        if (resultCode == Activity.RESULT_CANCELED) {
            Log.d("value", "data cancelled : ${data?.getStringExtra("value")}")
        }

    }

}

