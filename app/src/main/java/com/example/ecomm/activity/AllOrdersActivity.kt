package com.example.ecomm.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.ecomm.R
import com.example.ecomm.adapter.AllOrderAdapter
import com.example.ecomm.model.AllOrderModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AllOrdersActivity : AppCompatActivity() {
    private lateinit var list:ArrayList<AllOrderModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_orders)

        list = ArrayList()
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val preferences = this.getSharedPreferences("user", MODE_PRIVATE)
        val number = preferences.getString("number", "")!!
        Firebase.firestore.collection("allOrders").whereEqualTo("userId",number).get().addOnSuccessListener {
            list.clear()
            for(doc in it){
                val data = doc.toObject(AllOrderModel::class.java)
                list.add(data)
            }
            recyclerView.adapter = AllOrderAdapter(list,this)
        }
    }
}