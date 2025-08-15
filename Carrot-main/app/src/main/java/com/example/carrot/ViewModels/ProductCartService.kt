package com.example.carrot.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carrot.AppManager.UserSession
import com.example.carrot.Models.CartItem
import com.example.carrot.Models.Product
import com.example.carrot.Models.ProductWithCartQuantity
import com.example.carrot.Repositories.CartRepository
import com.example.carrot.Repositories.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductCartService @Inject constructor(
    private val cartRepository: CartRepository,
    private val productRepository: ProductRepository
): ViewModel() {

    private var _productsList: MutableStateFlow<List<Product>> = MutableStateFlow(emptyList())
    val productsList: StateFlow<List<Product>> = _productsList

    private var _cartItems: MutableStateFlow<List<CartItem>> = MutableStateFlow(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems

    private var _productsWithCartQuantity: MutableStateFlow<List<ProductWithCartQuantity>> = MutableStateFlow(emptyList())
    val productsWithCartQuantity: StateFlow<List<ProductWithCartQuantity>> = _productsWithCartQuantity

    init {
        observeUserSession()
    }

    private fun observeUserSession() {
        UserSession.currentUser
            .onEach{ user ->
                if (user == null) {
                    clearState()
                } else {
                    getProductsFromDB()
                    getCartItemsFromDB()
                }
            }
            .launchIn(viewModelScope)
    }

    private fun clearState() {
        _cartItems.value = emptyList()
        _productsList.value = emptyList()
        _productsWithCartQuantity.value = emptyList()
    }

    private fun getCartItemsFromDB() {
        viewModelScope.launch {
            val userId = UserSession.getCurrentUserId()
            if (userId != null) {
                cartRepository.getCartItemsList(userId = userId).collect { cartItems ->
                    _cartItems.value = cartItems
                    combineProductsWithCartQuantity() // Combines products and cart items
                }
            }
            else {
                println("User is not fount")

            }

        }
    }

    private fun getProductsFromDB() {
        viewModelScope.launch {
            // Fetch products from the database
            productRepository.getAllProducts().collect { products ->
                _productsList.value = products // Update the StateFlow
                combineProductsWithCartQuantity() // Combine products and cart items
            }
        }
    }

    fun getProductQuantityInCart(product: Product): Int {
        val productWithCartQuantity = productsWithCartQuantity.value.find { it.product.productId == product.productId }
        return productWithCartQuantity?.quantity ?: 0
    }

    private fun combineProductsWithCartQuantity() {
        Log.d("ProductCartService", "combineProductsWithCartQuantity called")
        val products = _productsList.value
        val cartItems = _cartItems.value

        // Create a map of productId to quantity for quick lookup
        val cartItemMap = cartItems.associate { it.productId to it.quantity }

        // Combine products with their cart quantities
        val productsWithCartQuantity = products.map { product ->
            ProductWithCartQuantity(
                product = product,
                quantity = cartItemMap[product.productId] ?: 0
            )
        }

        _productsWithCartQuantity.value = productsWithCartQuantity
        Log.d("ProductCartService", "combineProductsWithCartQuantity Finished ${productsWithCartQuantity.count()}")
    }

    // Handles "Add to Cart" with stock check
    suspend fun addToCartWithStockCheck(productId: Int, quantity: Int) {
        val userId = UserSession.getCurrentUserId()
        val cartItem = userId?.let {
            CartItem(
                userId = it,
                productId = productId,
                quantity = quantity,
                price = 69.0
            )
        }

        try {
            if (quantity == 0 && userId != null) {
                Log.d("ProductCartService", "Quantity is 0")
                val item = cartRepository.getCartItem(userId, productId)
                Log.d("ProductCartService", "Item: $item")
                if (item != null) {
                    cartRepository.removeFromCart(item.cartItemId)
                    Log.d("ProductCartService", "Item removed from cart")
                } else {
                    Log.d("ProductCartService", "Item not found in cart")
                }
            } else {
                // Call the repository method to add to cart with stock check
                if (cartItem != null) {
                    cartRepository.addToCartWithStockCheck(cartItem, productRepository)
                    Log.d("ProductCartService", "Item added to cart")
                }
            }
        } catch (e: IllegalStateException) {
            // Handle out-of-stock error (e.g., show a message to the user)
            println("Error: ${e.message}")
        }
    }
}