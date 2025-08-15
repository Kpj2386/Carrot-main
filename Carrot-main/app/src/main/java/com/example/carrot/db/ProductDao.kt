package com.example.carrot.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.carrot.Models.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Insert
    suspend fun insert(product: Product)

    @Query("SELECT * FROM products")
    fun getAllProducts(): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE productId = :productId")
    suspend fun getProductById(productId: Int): Product?

    @Update
    suspend fun update(product: Product)

    @Delete
    suspend fun delete(product: Product)

    @Query("DELETE FROM products")
    suspend fun deleteAllProducts()

    @Query("""
    SELECT * FROM products 
    WHERE productCode = :query
    OR brand || ' ' || productName LIKE '%' || :query || '%' 
    OR productName LIKE '%' || :query || '%'
    """)
    fun searchProducts(query: String): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE discountPrice != null")
    fun getDiscountedProducts(): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE productCode = :productCode")
    fun getProductByProductCode(productCode: String): Product?
}