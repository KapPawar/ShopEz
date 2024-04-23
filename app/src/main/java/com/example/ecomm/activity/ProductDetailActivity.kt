package com.example.ecomm.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.ecomm.MainActivity
import com.example.ecomm.R
import com.example.ecomm.databinding.ActivityProductDetailBinding
import com.example.ecomm.roomdb.AppDatabase
import com.example.ecomm.roomdb.ProductDao
import com.example.ecomm.roomdb.ProductModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductDetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityProductDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)

        getProductDetail(intent.getStringExtra("id"))
        setContentView(binding.root)
    }

    private fun getProductDetail(productId: String?) {
    Firebase.firestore.collection("products").document(productId!!).get().addOnSuccessListener {
        val list = it.get("productImages") as ArrayList<String>
        val productName = it.getString("productName")
        val productCost = "$"+it.getString("productCost")
        val productDescription = it.getString("productDescription")
        binding.textView4.text = productName
        binding.textView5.text = productCost
        binding.textView6.text = productDescription

        val slideList = ArrayList<SlideModel>()
        for (data in list){
            slideList.add(SlideModel(data, ScaleTypes.CENTER_CROP))
        }

        cartAction(productId, productName, productCost, it.getString("productCoverImg"))

        binding.imageSlider.setImageList(slideList)
    }.addOnFailureListener {
        Toast.makeText(this,"Something went wrong!", Toast.LENGTH_SHORT ).show()
    }
    }

    private fun cartAction(productId: String, productName: String?, productCost: String, coverImg: String?) {
        val productDao = AppDatabase.getInstance(this).productDao()
        if (productDao.isExist(productId) != null){
            binding.textView10.text = "Go to Cart"
        }else{
            binding.textView10.text = "Add to Cart"
        }

        binding.textView10.setOnClickListener{
            if (productDao.isExist(productId) != null){
               openCart()
            }else{
                addToCart(productDao, productId, productName, productCost, coverImg)
            }
        }
    }

    private fun addToCart(productDao: ProductDao, productId: String, productName: String?, productCost: String, coverImg: String?) {
        val data = ProductModel(productId, productName, coverImg, productCost)
        lifecycleScope.launch(Dispatchers.IO){
            productDao.insertProduct(data)
            binding.textView10.text = "Go to Cart"
        }
        }

    private fun openCart() {
        val preference = this.getSharedPreferences("info", MODE_PRIVATE)
        val editor = preference.edit()
        editor.putBoolean("isCart", true)
        editor.apply()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}