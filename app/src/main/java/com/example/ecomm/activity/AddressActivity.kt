package com.example.ecomm.activity

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.ecomm.R
import com.example.ecomm.databinding.ActivityAddressBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddressActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAddressBinding
    private lateinit var preferense : SharedPreferences
    private lateinit var totalCost : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferense = this.getSharedPreferences("user", MODE_PRIVATE)
        totalCost = intent.getStringExtra("totalCost")!!
        loadUserInfo()
        binding.proceed.setOnClickListener {
            validateData(
                binding.userName.text.toString(),
                binding.userNumber.text.toString(),
                binding.userAddress.text.toString(),
                binding.userState.text.toString(),
                binding.userCity.text.toString(),
                binding.userZip.text.toString(),

            )
        }
    }

    private fun validateData(name: String, number: String, add: String, state: String, city: String, zip: String) {
        if (name.isEmpty() || number.isEmpty() || add.isEmpty() || state.isEmpty() || city.isEmpty() || zip.isEmpty()){
            Toast.makeText(this, "All fields required", Toast.LENGTH_SHORT).show()
        }
        else{
            storeData(add, state, city, zip)
        }
    }

    private fun storeData(add: String, state: String, city: String, zip: String) {
        val map = hashMapOf<String, Any>()
        map["address"] = add
        map["state"] = state
        map["city"] = city
        map["zip"] = zip

        Firebase.firestore.collection("users")
                .document(preferense.getString("number", "")!!)
            .update(map)
            .addOnSuccessListener {
                val b = Bundle()
                b.putStringArrayList("productIds", intent.getStringArrayListExtra("productIds"))
                b.putString("totalCost", totalCost)

                val intent = Intent(this, CheckoutActivity::class.java)
                intent.putExtras(b)
                startActivity(intent)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
    }


    private fun loadUserInfo() {

        Firebase.firestore.collection("users")
            .document(preferense.getString("number", "")!!)
            .get().addOnSuccessListener {
                binding.userName.setText(it.getString("userName"))
                binding.userNumber.setText(it.getString("userPhoneNumber"))
                binding.userAddress.setText(it.getString("address"))
                binding.userState.setText(it.getString("state"))
                binding.userCity.setText(it.getString("city"))
                binding.userZip.setText(it.getString("zip"))
            }
    }
}