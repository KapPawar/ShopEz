package com.example.ecomm.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecomm.activity.ProductDetailActivity
import com.example.ecomm.databinding.LayoutCartItemBinding
import com.example.ecomm.roomdb.AppDatabase
import com.example.ecomm.roomdb.ProductModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CartAdapter(val context: Context,val list:List<ProductModel>) :
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
//    private lateinit var numberPicker: NumberPicker
    inner class CartViewHolder(val binding: LayoutCartItemBinding) :
            RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = LayoutCartItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return CartViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        Glide.with(context).load(list[position].productImage).into(holder.binding.imageView4)
        holder.binding.textView11.text = list[position].productName
        holder.binding.textView13.text = list[position].productCost
        holder.binding.editTextNumber2.setText(list[position].productQuantity!!)
//        numberPicker = holder.binding.numberPicker
//        numberPicker.minValue = 1
//        numberPicker.maxValue = 10
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductDetailActivity::class.java)
            intent.putExtra("id", list[position].productId)
            context.startActivity(intent)
        }

        val dao = AppDatabase.getInstance(context).productDao()
        holder.binding.imageView5.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
            dao.deleteProduct(ProductModel(list[position].productId, list[position].productName, list[position].productImage,list[position].productCost))
            }
        }

        holder.binding.imageButton.setOnClickListener {
            var quan = list[position].productQuantity!!.toInt()
            quan = quan + 1
            GlobalScope.launch(Dispatchers.IO) {
                dao.updateProduct(ProductModel(list[position].productId, list[position].productName, list[position].productImage,list[position].productCost, quan.toString()))
            }
        }
        holder.binding.imageButton2.setOnClickListener {
            var quan = list[position].productQuantity!!.toInt()
            if (quan > 1){
            quan = quan - 1
            GlobalScope.launch(Dispatchers.IO) {
                dao.updateProduct(ProductModel(list[position].productId, list[position].productName, list[position].productImage,list[position].productCost, quan.toString()))
            }}
        }
    }
}