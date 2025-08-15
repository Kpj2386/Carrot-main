package com.example.carrot.ViewModels

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carrot.AppManager.UserSession
import com.example.carrot.Models.CartItem
import com.example.carrot.Repositories.CartRepository
import com.example.carrot.Repositories.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val productRepository: ProductRepository
): ViewModel() {

    private val _cartItems = mutableStateListOf<CartItem>()
    val cartItems: List<CartItem> = _cartItems

    private val _cartGrandTotalPrice = MutableStateFlow(0.00)
    val cartGrandTotalPrice: StateFlow<Double> = _cartGrandTotalPrice

    // SGST calculation
    private val sgst_percentage = 9.0
    private val _sgst = MutableStateFlow(0.0)
    val sgst: StateFlow<Double> = _sgst

    //CGST calculation
    private val cgst_percentage = 9.0
    private val _cgst = MutableStateFlow(0.0)
    val cgst: MutableStateFlow<Double> = _cgst

    // cart items total
    private val _totalCartCost = MutableStateFlow(0.00)
    val totalCartCost: StateFlow<Double> = _totalCartCost

    private val _showCartItems = MutableStateFlow(false)
    val showCartItems: StateFlow<Boolean> = _showCartItems

    // Fetch the current user's ID from UserSession
    val userId = UserSession.getCurrentUserId() ?: throw IllegalStateException("User not logged in")

    // Observe the total quantity of items in the cart
    val totalCartItemQuantity: Flow<Int> = cartRepository.getTotalCartItemQuantity(userId)
        .flowOn(Dispatchers.IO) // Perform database operations on a background thread

    init {
        Log.d("CartViewModel", "CartViewModel initialized")
        fetchCartItems()
    }

    private fun fetchCartItems() {
        viewModelScope.launch {
            Log.d("CartViewModel", "Fetching cart items for user ID: $userId")
            cartRepository.getCartItemsList(userId = userId)
                .flowOn(Dispatchers.IO) // Perform database operations on a background thread
                .collect { items ->
                    var totalCost = 0.00
                    Log.d("CartViewModel", "Fetched cart items: $items")
                    _showCartItems.value = items.isNotEmpty()
                    _cartItems.clear()
                    _cartItems.addAll(items)
                    items.forEach {
                        val product = productRepository.getProductById(it.productId)
                        if (product != null) {
                            it.price = product.price
                        }
                        totalCost += it.price * it.quantity
                    }

                    _totalCartCost.value = String.format("%.2f", totalCost).toDouble()
                    _sgst.value = String.format("%.2f", (totalCost * sgst_percentage) / 100).toDouble()
                    _cgst.value = String.format("%.2f", (totalCost * cgst_percentage) / 100).toDouble()
                    _cartGrandTotalPrice.value = String.format("%.2f", totalCost + (_sgst.value + _cgst.value)).toDouble()

                    Log.d("CartViewModel", "grand total: $totalCost")

                }
        }
    }

}