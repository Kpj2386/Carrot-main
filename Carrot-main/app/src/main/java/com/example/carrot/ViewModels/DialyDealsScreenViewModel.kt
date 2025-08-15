package com.example.carrot.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DialyDealsScreenViewModel @Inject constructor(
    private val productCartService: ProductCartService
): ViewModel() {

    private val _discountedProductsList = MutableStateFlow<List<ProductWithCartQuantity>>(emptyList())
    val discountedProductsList: MutableStateFlow<List<ProductWithCartQuantity>> = _discountedProductsList

    private val _selectedProduct = MutableStateFlow<Product?>(null)
    val selectedProduct: StateFlow<Product?> = _selectedProduct

    init {
        fetchDiscountedProducts()
    }

    private fun fetchDiscountedProducts() {
        Log.d("DailyDealsScreenViewModel", "Fetching discounted products")
        viewModelScope.launch {
            productCartService.productsWithCartQuantity.collectLatest { items ->
                if (_discountedProductsList.value.isEmpty()) {
                    _discountedProductsList.value = items.filter { it.product.discountPrice != null }
                } else {
                    _discountedProductsList.value =
                        _discountedProductsList.value.map { productWithQuantity ->
                            items.find {
                                it.product.productId == productWithQuantity.product.productId && it.product.discountPrice != null
                            }?.let { updatedItem ->
                                productWithQuantity.copy(quantity = updatedItem.quantity)
                            } ?: productWithQuantity
                        }
                }
            }

            Log.d("DailyDealsScreenViewModel", "Fetching discounted products ${discountedProductsList.value.count()}")
        }

    }

    fun onSelectedProductChange(product: Product) {
        _selectedProduct.value = product
    }

    // Handles "Add to Cart" with stock check
    fun addToCartWithStockCheck(productId: Int, quantity: Int) {
        viewModelScope.launch {
            productCartService.addToCartWithStockCheck(productId, quantity)
        }
    }

    fun getProductQuantityInCart(product: Product): Int {
        return productCartService.getProductQuantityInCart(product)
    }

    fun incrementAction() {
        viewModelScope.launch {
            _selectedProduct.value?.let {
                addToCartWithStockCheck(it.productId, getProductQuantityInCart(it) + 1)
            }
        }
    }

    fun decrementAction() {
        viewModelScope.launch {
            _selectedProduct.value?.let {
                addToCartWithStockCheck(it.productId, getProductQuantityInCart(it) - 1)
            }
        }
    }
}