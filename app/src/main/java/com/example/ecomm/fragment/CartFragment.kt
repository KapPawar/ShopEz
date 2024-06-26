package com.example.ecomm.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.appcompat.app.AppCompatActivity
import com.example.ecomm.R
import com.example.ecomm.activity.AddressActivity
import com.example.ecomm.activity.CategoryActivity
import com.example.ecomm.adapter.CartAdapter
import com.example.ecomm.databinding.FragmentCartBinding
import com.example.ecomm.roomdb.AppDatabase
import com.example.ecomm.roomdb.ProductModel


class CartFragment : Fragment() {

    private lateinit var binding : FragmentCartBinding
    private lateinit var list : ArrayList<String>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(layoutInflater)

        val preference = requireContext().getSharedPreferences("info", AppCompatActivity.MODE_PRIVATE)
        val editor = preference.edit()
        editor.putBoolean("isCart", false)
        editor.apply()


        val dao = AppDatabase.getInstance(requireContext()).productDao()
        list = ArrayList()
        dao.getAllProducts().observe(requireActivity()){
            binding.cartRecycler.adapter = CartAdapter(requireContext(), it)
            list.clear()
            for (data in it){
                list.add(data.productId)
            }
            totalCost(it)
        }
        return binding.root
    }

    private fun totalCost(data: List<ProductModel>?) {
        var total = 0
        for (item in data!!){
            total += item.productCost!!.substring(1, item.productCost!!.length).toInt() * item.productQuantity!!.toInt()
        }
        binding.textView1.text = "Total items in cart : ${data.size}"
        binding.textView2.text = "Total Cost : ${total}"
        binding.Checkout.setOnClickListener {
            val intent = Intent(context, AddressActivity::class.java)

            val b = Bundle()
            b.putStringArrayList("productIds", list)
            b.putString("totalCost", total.toString())
            intent.putExtras(b)
            startActivity(intent)
//            intent.putExtra("totalCost", total.toString())
//            intent.putStringArrayListExtra()
        }
    }


}