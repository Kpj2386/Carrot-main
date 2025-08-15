package com.example.carrot.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.carrot.Models.CartItem
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Insert
    suspend fun insert(cartItem: CartItem)

    @Query("SELECT * FROM cart_items WHERE userId = :userId")
    fun getCartItems(userId: Int): Flow<List<CartItem>>

    @Query("SELECT * FROM cart_items WHERE userId = :userId AND productId = :productId")
    suspend fun getCartItem(userId: Int, productId: Int): CartItem?

    @Update
    suspend fun update(cartItem: CartItem)

    @Query("DELETE FROM cart_items WHERE cartItemId = :cartItemId")
    suspend fun delete(cartItemId: Int)

    @Query("DELETE FROM cart_items WHERE userId = :userId")
    suspend fun clearCart(userId: String)

    @Query("SELECT SUM(quantity) FROM cart_items WHERE userId = :userId")
    fun getTotalCartItemQuantity(userId: Int): Flow<Int>

    @Query("SELECT * FROM cart_items WHERE cartItemId = :cartItemId")
    suspend fun getCartItemWithId(cartItemId: Int): CartItem?

}