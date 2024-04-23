package com.example.ecomm.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ecomm.MainActivity
import com.example.ecomm.R
import com.example.ecomm.activity.AllOrdersActivity
import com.example.ecomm.databinding.FragmentMoreBinding
import com.google.firebase.auth.FirebaseAuth

class MoreFragment : Fragment() {
    private lateinit var binding : FragmentMoreBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_more, container, false)
        binding = FragmentMoreBinding.inflate(layoutInflater)
        binding.history.setOnClickListener {
            startActivity(Intent(requireContext(), AllOrdersActivity::class.java))
        }
        binding.logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(requireContext(), MainActivity::class.java))
        }
        return binding.root
    }
}