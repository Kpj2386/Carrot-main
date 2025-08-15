package com.example.carrot.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carrot.Models.CartItem
import com.example.carrot.Models.Product
import com.example.carrot.Repositories.CartRepository
import com.example.carrot.Repositories.ProductRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = CartItemRowViewModel.MyViewModelFactory::class)
class CartItemRowViewModel @AssistedInject constructor(
    private val cartRepository: CartRepository,
    private val productRepository: ProductRepository,
    private val productCartService: ProductCartService,
    @Assisted private val cartItemId: Int
) : ViewModel() {

    private val _product = MutableStateFlow<Product?>(null)
    val product: StateFlow<Product?> = _product

    private val _cartItem = MutableStateFlow<CartItem?>(null)
    val cartItem: StateFlow<CartItem?> = _cartItem

    fun onLoadingOfScreen() {
        loadCartItemAndProduct()
    }

    private fun loadCartItemAndProduct() {
        viewModelScope.launch {
            val cartItem = cartRepository.getCartItemWithId(cartItemId)
            if (cartItem != null) {
                updateCartItem(cartItem)
                fetchProduct(cartItem.productId)
                Log.d("CartItemRowViewModel", "Cart item loaded: ${cartItem.quantity}")
            } else {
                Log.e("CartItemRowViewModel", "Cart item $cartItemId not found")
            }
        }
    }

    private suspend fun fetchProduct(productId: Int) {
        if (_product.value == null) {
            val product = productRepository.getProductById(productId)
            _product.value = product
            Log.d("CartItemRowViewModel", "Product loaded already: ${_product.value?.productName}")
        } else {
            Log.e("CartItemRowViewModel", "Product $productId not found")
        }
    }

    private fun updateCartItem(cartItem: CartItem) {
        Log.d("CartItemRowViewModel", "Updating cart item with quantity: ${cartItem.quantity}")
        _cartItem.value = cartItem
    }

    @AssistedFactory
    interface MyViewModelFactory {
        fun create(cartItemId: Int): CartItemRowViewModel
    }

    // Handles "Add to Cart" with stock check
    private fun addToCartWithStockCheck(productId: Int, quantity: Int) {
        viewModelScope.launch {
            productCartService.addToCartWithStockCheck(productId, quantity)
            Log.d("CartItemRowViewModel", "Added to cart with stock check and quantity: $quantity")
            loadCartItemAndProduct()
        }

    }

    fun incrementAction() {
        _cartItem.value?.let { addToCartWithStockCheck(it.productId, it.quantity + 1) }
    }

    fun decrementAction() {
        _cartItem.value?.let { addToCartWithStockCheck(it.productId, it.quantity - 1) }
    }

}