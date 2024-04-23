package com.example.ecomm.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecomm.MainActivity
import com.example.ecomm.activity.ProductDetailActivity
import com.example.ecomm.databinding.LayoutProductItemBinding
import com.example.ecomm.model.AddProductModel
import com.example.ecomm.model.AllOrderModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProductAdapter (val context: Context, val list: ArrayList<AddProductModel>)
    : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>(){
    inner class ProductViewHolder(val binding: LayoutProductItemBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = LayoutProductItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ProductViewHolder((binding))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val data = list[position]

        Glide.with(context).load(data.productCoverImg).into(holder.binding.imageView2)
        holder.binding.textView7.text = data.productName
        holder.binding.textView8.text = data.productDescription
        holder.binding.textView9.text = data.productCategory
        holder.binding.button2.text = "$"+data.productCost

        holder.itemView.setOnClickListener{
            val intent = Intent(context, ProductDetailActivity::class.java)
            intent.putExtra("id", list[position].productId)
            context.startActivity(intent)

        }

        val preferences = context.getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        val number = preferences.getString("number", "")!!

        Firebase.firestore.collection("wishlist").whereEqualTo("userId",number)
            .whereEqualTo("productId", data.productId!!)
            .get().addOnSuccessListener {
                if(!it.isEmpty){
//                    holder.binding.wishlist.text = "\uD83E\uDD0D"
                    holder.binding.wishlist.isEnabled = false
                }
            }

        holder.binding.wishlist.setOnClickListener {
            val prod = hashMapOf<String, Any>()
            prod["productId"] = data.productId!!
            prod["productCoverImg"] = data.productCoverImg!!
            prod["productName"] = data.productName!!
            prod["userId"] = number
            val firestore = Firebase.firestore.collection("wishlist")

            firestore.add(prod).addOnSuccessListener {
                Toast.makeText(context, "Added to Wishlist", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        }
    }
}