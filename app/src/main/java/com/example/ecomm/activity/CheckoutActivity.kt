package com.example.ecomm.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.ecomm.MainActivity
import com.example.ecomm.R
import com.example.ecomm.roomdb.AppDatabase
import com.example.ecomm.roomdb.ProductModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class CheckoutActivity : AppCompatActivity(), PaymentResultListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        val checkout = Checkout()
        checkout.setKeyID("rzp_test_5G1qanePX8UYHx")
        val price = intent.getStringExtra("totalCost")
        try {
            val options = JSONObject()
            options.put("name","ShopEz")
            options.put("description","Ecomm App")
            //You can omit the image option to fetch the image from the dashboard
//            options.put("image","https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg")
            options.put("theme.color", "#282A3A");
            options.put("currency","USD");
//            options.put("order_id", "order_DBJOWzybf0sJbb");
//            options.put("amount",(price!!.toInt()*100).toString())//pass amount in currency subunits
            options.put("amount","100")//pass amount in currency subunits


            val prefill = JSONObject()
            prefill.put("email","pawar.kapil.de@gmail.com")
//            prefill.put("contact","9167369035")

            options.put("prefill",prefill)
            checkout.open(this,options)
        }catch (e: Exception){
            Toast.makeText(this,"Error in payment: "+ e.message,Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(p0: String?) {
        Toast.makeText(this, "Payment Successfull", Toast.LENGTH_SHORT).show()
        uploadData()
    }

    private fun uploadData() {
        val id = intent.getStringArrayListExtra("productIds")
        for (currentId in id!!){
            fetchData(currentId)
        }
    }

    private fun fetchData(productId: String?) {
        val dao = AppDatabase.getInstance(this).productDao()
        val prod = dao.isExist(productId!!)
        Firebase.firestore.collection("products").document(productId!!)
            .get().addOnSuccessListener {
                lifecycleScope.launch(Dispatchers.IO){
                dao.deleteProduct(ProductModel(productId))}
                saveData(it.getString("productName"), it.getString("productCost"), it.getString("productId"), prod.productQuantity)

            }
    }

    private fun saveData(productName: String?, productCost: String?, productId: String?, productQuantity: String?) {
        val preferences = this.getSharedPreferences("user", MODE_PRIVATE)
        val data = hashMapOf<String, Any>()
        data["name"] = productName!!
        data["price"] = productCost!!
        data["productId"] = productId!!
        data["status"] = "Ordered"
        data["userId"] = preferences.getString("number", "")!!
        data["quantity"] = productQuantity!!
        val firestore = Firebase.firestore.collection("allOrders")
        val key = firestore.document().id
        data["orderId"] = key

        firestore.document(key).set(data).addOnSuccessListener {
            Toast.makeText(this, "Order Placed", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
        }.addOnFailureListener {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this, "Payment Error", Toast.LENGTH_SHORT).show()
    }
}