package com.example.carrot.Repositories

import android.util.Log
import com.example.carrot.Models.Product
import com.example.carrot.db.ProductDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


interface ProductRepository {
    suspend fun getAllProducts(): Flow<List<Product>>
    suspend fun getProductById(productId: Int): Product?
    suspend fun searchInProducts(query: String): Flow<List<Product>>
    suspend fun getProductByProductCode(productCode: String): Product?
    fun getDiscountedProducts(): Flow<List<Product>>
}

class ProductRepositoryImpl @Inject constructor(
    private val productDao: ProductDao
) : ProductRepository {

    override suspend fun getAllProducts(): Flow<List<Product>> {
        return productDao.getAllProducts()
    }

    override suspend fun getProductById(productId: Int): Product? {
        return productDao.getProductById(productId)
    }

    override suspend fun searchInProducts(query: String): Flow<List<Product>> {
        return productDao.searchProducts(query)
    }

    override suspend fun getProductByProductCode(productCode: String): Product? {
        Log.d("ProductRepositoryImpl", "getProductByProductCode: $productCode")
        return productDao.getProductByProductCode(productCode)
    }

    override fun getDiscountedProducts(): Flow<List<Product>> {
        return productDao.getDiscountedProducts()
    }

}