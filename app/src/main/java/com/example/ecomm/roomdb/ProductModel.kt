package com.example.ecomm.roomdb

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductModel(
    @PrimaryKey
    @NonNull
    val productId : String,
    @ColumnInfo("productName") val productName: String ? = "",
    @ColumnInfo("productImage") val productImage: String ? = "",
    @ColumnInfo("productCost") val productCost: String ? = "",
    @ColumnInfo("productQuantity") val productQuantity: String ? = "1"
    )
