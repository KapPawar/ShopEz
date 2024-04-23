package com.example.ecomm.model

data class AddProductModel(
    val productName: String? = "",
    val productDescription: String? = "",
    val productCoverImg: String? = "",
    val productCategory: String? = "",
    val productId: String? = "",
    val productCost: String? = "",
    val productSp: String? = "",
    val productImages: ArrayList<String> = ArrayList()
)
