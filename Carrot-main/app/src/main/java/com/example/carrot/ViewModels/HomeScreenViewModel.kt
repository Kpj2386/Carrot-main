package com.example.carrot.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carrot.Models.Product
import com.example.carrot.Repositories.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val productCartService: ProductCartService
) : ViewModel() {

    private val _searchedText = MutableStateFlow<String>("")
    val searchedText = _searchedText

    private val _product = MutableStateFlow<Product?>(null)
    val product = _product

    fun onProductCodeChange(newValue: String) {
        Log.d("HomeScreenViewModel", "onProductCodeChange: $newValue")
        _searchedText.value = newValue
    }

    fun searchButtonAction(callBack: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val product = productRepository.getProductByProductCode(_searchedText.value)
            Log.d("HomeScreenViewModel", "searchButtonAction: $product")
            withContext(Dispatchers.Main) {
                if (product != null) {
                    _product.value = product
                    callBack(true)
                } else {
                    _product.value = null
                    callBack(false)
                }
            }
        }
    }

    fun getProductCartQuantity(): Int {
        product.value?.let {
            return productCartService.getProductQuantityInCart(it)
        } ?: return 0
    }

    fun addToCartAction(quantity: Int) {
        viewModelScope.launch {
            product.value?.let {
                productCartService.addToCartWithStockCheck(it.productId, quantity)
            }
        }

    }
}