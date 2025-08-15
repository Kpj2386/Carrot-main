package com.example.carrot.Views.DialyDealViews

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.carrot.Models.Product
import com.example.carrot.ViewModels.DialyDealsScreenViewModel
import com.example.carrot.Views.AddToCartDialogView
import com.example.carrot.Views.Components.ProductItemView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialyDealsScreen(navController: NavController) {

    val viewModel: DialyDealsScreenViewModel = hiltViewModel()

    val discountedProducts by viewModel.discountedProductsList.collectAsState()
    var showDialog by rememberSaveable { mutableStateOf(false) }
    val selectedProduct by viewModel.selectedProduct.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(

        ) {
            Text("Deals of the day", style = MaterialTheme.typography.headlineLarge)

            Spacer(Modifier.size(16.dp))

            LazyVerticalGrid(
                modifier = Modifier.weight(1f),
                columns = GridCells.Fixed(4),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(0.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(discountedProducts) { productWithCartQuantity ->
                    Box(
                        modifier = Modifier
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onTap = {
                                        viewModel.onSelectedProductChange(productWithCartQuantity.product)
                                        showDialog = true
                                    }
                                )
                            }
                    ) {
                        ProductItemView(
                            product = productWithCartQuantity.product,
                            quantityInCart = productWithCartQuantity.quantity,
                            incrementAction = {
                                viewModel.onSelectedProductChange(productWithCartQuantity.product)
                                showDialog = true
                            },
                            decrementAction = {
                                viewModel.onSelectedProductChange(productWithCartQuantity.product)
                                showDialog = true
                            }
                        )
                    }
                }
            }

            if (showDialog) {
                BasicAlertDialog(
                    onDismissRequest = {
                        showDialog = false
                    },
                    modifier = Modifier.background(Color.White)
                ) {
                    selectedProduct?.let { product ->

                        AddToCartDialogView(
                            product = product,
                            quantityInCart = viewModel.getProductQuantityInCart(product),
                            primaryButtonAction = { quantity ->
                                viewModel.addToCartWithStockCheck(
                                    product.productId,
                                    quantity = quantity
                                )
                                showDialog = false
                            },
                            secondaryButtonAction = { quantity ->
                            }
                        )
                    }
                }
            }
        }
    }
}