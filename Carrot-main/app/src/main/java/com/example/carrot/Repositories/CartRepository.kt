package com.example.carrot.Repositories

import android.util.Log
import androidx.room.Transaction
import com.example.carrot.Helper.StockStatus
import com.example.carrot.Models.CartItem
import com.example.carrot.db.CartDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface CartRepository {
    suspend fun getCartItemsList(userId: Int): Flow<List<CartItem>>
    suspend fun getCartItem(userId: Int, productId: Int): CartItem?
    suspend fun getCartItemWithId(cartItemId: Int): CartItem?
    suspend fun addToCart(cartItem: CartItem)
    suspend fun updateCartItem(cartItem: CartItem)
    suspend fun removeFromCart(cartItemId: Int)
    suspend fun clearCart(userId: String)
    suspend fun addToCartWithStockCheck(cartItem: CartItem, productRepository: ProductRepository)
    fun getTotalCartItemQuantity(userId: Int): Flow<Int>
}

class CartRepositoryImpl @Inject constructor(
    private val cartDao: CartDao,
    private val productRepository: ProductRepository
) : CartRepository {

    override suspend fun getCartItemsList(userId: Int): Flow<List<CartItem>> {
        Log.d("CartRepositoryImpl", "getCartItemsList started for user: $userId")
        val result = cartDao.getCartItems(userId)
        Log.d("CartRepositoryImpl", "getCartItemsList ended for user: $userId")
        return result
    }

    override suspend fun addToCart(cartItem: CartItem) {
        Log.d("CartRepositoryImpl", "addToCart started for product: ${cartItem.productId}, user: ${cartItem.userId}")
        cartDao.insert(cartItem)
        Log.d("CartRepositoryImpl", "addToCart ended for product: ${cartItem.productId}, user: ${cartItem.userId}")
    }

    override suspend fun updateCartItem(cartItem: CartItem) {
        Log.d("CartRepositoryImpl", "updateCartItem started for product: ${cartItem.productId}, user: ${cartItem.userId}")
        val existingCartItem = cartDao.getCartItem(userId = cartItem.userId, productId = cartItem.productId)
        if (existingCartItem != null) {
            cartItem.cartItemId = existingCartItem.cartItemId // Update the quantity
            cartDao.update(cartItem) // Save the updated cart item to the database
             Log.d("CartRepositoryImpl", "updateCartItem ended for product: ${cartItem.productId}, user: ${cartItem.userId}")
        } else {
            Log.d("CartRepositoryImpl", "CartItem not found in the database")
        }

    }

    override suspend fun getCartItem(userId: Int, productId: Int): CartItem? {
        Log.d("CartRepositoryImpl", "getCartItem started for user: $userId, product: $productId")
        val result = cartDao.getCartItem(userId, productId)
        Log.d("CartRepositoryImpl", "getCartItem ended for user: $userId, product: $productId")
        return result
    }

    override suspend fun removeFromCart(cartItemId: Int) {
        Log.d("CartRepositoryImpl", "removeFromCart started for cartItemId: $cartItemId")
        cartDao.delete(cartItemId)
        Log.d("CartRepositoryImpl", "removeFromCart ended for cartItemId: $cartItemId")
    }

    override suspend fun clearCart(userId: String) {
        Log.d("CartRepositoryImpl", "clearCart started for user: $userId")
        cartDao.clearCart(userId)
        Log.d("CartRepositoryImpl", "clearCart ended for user: $userId")
    }

    override fun getTotalCartItemQuantity(userId: Int): Flow<Int> {
        Log.d("CartRepositoryImpl", "getTotalCartItemQuantity started for user: $userId")

        return cartDao.getTotalCartItemQuantity(userId)
    }

    override suspend fun getCartItemWithId(cartItemId: Int): CartItem? {
        return cartDao.getCartItemWithId(cartItemId)
    }

    @Transaction
    override suspend fun addToCartWithStockCheck(cartItem: CartItem, productRepository: ProductRepository) {
        Log.d("CartRepositoryImpl", "addToCartWithStockCheck started for product: ${cartItem.productId}, user: ${cartItem.userId}")
        val product = productRepository.getProductById(cartItem.productId)
        val existingCartItem = cartDao.getCartItem(userId = cartItem.userId, productId = cartItem.productId)
        val stock = product?.stockStatus

        if (stock != null && stock == StockStatus.IN_STOCK) {
            if (existingCartItem != null) {
                updateCartItem(cartItem = cartItem)
            }
            else{
                addToCart(cartItem)
            }
        }
        else {
            throw IllegalStateException("Product out of stock")
        }
        Log.d("CartRepositoryImpl", "addToCartWithStockCheck ended for product: ${cartItem.productId}, user: ${cartItem.userId}")
    }
}
