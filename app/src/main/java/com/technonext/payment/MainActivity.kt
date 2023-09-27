package com.technonext.payment

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.technonext.payment.model.Customer
import com.technonext.payment.model.PaymentResponse
import com.technonext.payment.model.Url
import com.technonext.payment.utils.SDKType
import com.technonext.payment.view.PaymentActivity

class MainActivity : AppCompatActivity()  {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btn=findViewById<Button>(R.id.btnPay)
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
        val url=Url(
            "http://localhost/PaymentGatewayClient/success.php",
            "http://localhost/PaymentGatewayClient/success.php",
            "http://localhost/PaymentGatewayClient/success.php",
            "http://localhost/PaymentGatewayClient/success.php"
        )

        btn.setOnClickListener {
            val intent = Intent(this, PaymentActivity::class.java)
            intent.putExtra("amount","10")
            intent.putExtra("customer",customer)
            intent.putExtra("url",url)
            intent.putExtra("sdk_type",SDKType.TEST)
            startForResult.launch(intent)
        }

    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        val intent = result.data
        if (result.resultCode == Activity.RESULT_OK) {
            Log.d("RESULT OK", "onActivityResult: result ok")
            val response: PaymentResponse? =intent?.getSerializableExtra("response", PaymentResponse::class.java)
            Log.d("RETURN VALUE", "onActivityResult: ${response?.txnId}")
            if (response != null) {
                val builder = AlertDialog.Builder(this@MainActivity)
                builder.setTitle("Message!")
                builder.setMessage("Transaction Successfully Done\nTransaction ID: ${response.txnId}")
                builder.setPositiveButton(
                    "Ok"
                ) { dialog, which -> dialog.cancel() }
                builder.create().show()
            }else{
                Toast.makeText(this,"Value return null",Toast.LENGTH_LONG).show()
            }


        }
        if (result.resultCode == Activity.RESULT_CANCELED) {
            Log.d("value", "data cancelled : ${intent?.getStringExtra("value")}")
        }
    }


}

