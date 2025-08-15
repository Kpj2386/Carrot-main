package com.example.carrot.ViewModels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carrot.AppManager.UserSession
import com.example.carrot.Models.CartItem
import com.example.carrot.Models.Product
import com.example.carrot.Models.ProductWithCartQuantity
import com.example.carrot.Repositories.CartRepository
import com.example.carrot.Repositories.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository,
    private val productCartService: ProductCartService
) : ViewModel() {

    private var _productsList: MutableStateFlow<List<ProductWithCartQuantity>> = MutableStateFlow(
        emptyList()
    )
    val productsList: StateFlow<List<ProductWithCartQuantity>> = _productsList

    private var _selectedProduct: MutableStateFlow<Product?> = MutableStateFlow(null)
    val selectedProduct: StateFlow<Product?> = _selectedProduct

    private var _searchText: MutableState<String> = mutableStateOf("")
    val searchText = _searchText

    init {
        updateProductsList()
    }

    private fun updateProductsList() {
        viewModelScope.launch {
            productCartService.productsWithCartQuantity.collectLatest { items ->
                if (_productsList.value.isEmpty()) {
                    _productsList.value = items
                } else {
                    _productsList.value = _productsList.value.map { productWithQuantity ->
                        items.find {
                            it.product.productId == productWithQuantity.product.productId
                        }?.let { updatedItem ->
                            productWithQuantity.copy(quantity = updatedItem.quantity)
                        } ?: productWithQuantity
                    }
                }
            }
        }
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
        searchInDB()
    }

    fun onSelectedProductChange(product: Product?) {
        _selectedProduct.value = product
    }

    private fun searchInDB() {
        viewModelScope.launch {
            if (searchText.value.isEmpty()) {
                Log.d("SearchScreenViewModel", "Search text is empty")
                _productsList.value = productCartService.productsWithCartQuantity.value
            } else {
                productRepository
                    .searchInProducts(query = searchText.value)
                    .collectLatest { products ->
                        _productsList.value = products.map { item ->
                            productCartService.productsWithCartQuantity.value.first {
                                item.productId == it.product.productId
                            }
                        }
                    }
            }
        }

    }

    fun getProductCartQuantity(): Int {
        selectedProduct.value?.let {
            return productCartService.getProductQuantityInCart(it)
        } ?: return 0
    }



    // Handles "Add to Cart" with stock check
    fun addToCartWithStockCheck(productId: Int, quantity: Int) {
        viewModelScope.launch {
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
                    Log.d("AddToCart", "Quantity is 0")
                    val item = cartRepository.getCartItem(userId, productId)
                    Log.d("AddToCart", "Item: $item")
                    if (item != null) {
                        cartRepository.removeFromCart(item.cartItemId)
                        Log.d("AddToCart", "Item removed from cart")
                    } else {
                        Log.d("AddToCart", "Item not found in cart")
                    }
                } else {
                    // Call the repository method to add to cart with stock check
                    if (cartItem != null) {
                        cartRepository.addToCartWithStockCheck(cartItem, productRepository)
                    }
                }
            } catch (e: IllegalStateException) {
                // Handle out-of-stock error (e.g., show a message to the user)
                println("Error: ${e.message}")
            }
        }
    }

    fun incrementAction() {
        viewModelScope.launch {
            _selectedProduct.value?.let {
                addToCartWithStockCheck(it.productId, productCartService.getProductQuantityInCart(it) + 1)
            }
        }
    }

    fun decrementAction() {
        viewModelScope.launch {
            _selectedProduct.value?.let {
                addToCartWithStockCheck(it.productId, productCartService.getProductQuantityInCart(it) - 1)
            }
        }
    }
}

