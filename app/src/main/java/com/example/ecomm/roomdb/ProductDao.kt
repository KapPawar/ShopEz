package com.example.ecomm.roomdb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ProductDao {
    @Insert
    suspend fun insertProduct(product: ProductModel)
    @Delete
    suspend fun deleteProduct(product: ProductModel)
    @Query("SELECT * FROM products")
    fun getAllProducts() : LiveData<List<ProductModel>>

    @Query("SELECT * FROM products WHERE productId = :id")
    fun isExist(id: String) : ProductModel

    @Update
    suspend fun updateProduct(product: ProductModel)
}