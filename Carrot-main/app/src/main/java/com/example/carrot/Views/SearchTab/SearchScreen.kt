package com.example.carrot.Views.SearchTab

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.example.carrot.R
import com.example.carrot.ViewModels.SearchScreenViewModel
import com.example.carrot.Views.AddToCartDialogView
import com.example.carrot.Views.Components.ProductItemView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController) {
    val viewModel = hiltViewModel<SearchScreenViewModel>()
    val focusRequester = remember { FocusRequester() }

    val buttonColor = Color.White

    val searchText by viewModel.searchText
    val productsWithCartQuantity by viewModel.productsList.collectAsState()
    val showDialog = remember { mutableStateOf(false) }

    val selectedProduct by viewModel.selectedProduct.collectAsState()
    var showKeyboard by rememberSaveable { mutableStateOf(true) }

    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(showKeyboard) {
        if (showKeyboard) {
            focusRequester.requestFocus()
            keyboardController?.show()
        } else {
            keyboardController?.hide()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center
        ) {
            Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                TextField(
                    value = searchText,
                    onValueChange = { it: String ->
                        viewModel.onSearchTextChange(it)
                    },
                    placeholder = { Text("Search", style = MaterialTheme.typography.bodyMedium) },
                    modifier = Modifier
                        .weight(2f)
                        .height(56.dp)
                        .focusRequester(focusRequester),
                    colors = TextFieldDefaults
                        .colors(
                        focusedContainerColor = buttonColor,
                        unfocusedContainerColor = buttonColor,
                        disabledContainerColor = Color.LightGray,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.width(8.dp))

                OutlinedButton(
                    onClick = {
                        showKeyboard = !showKeyboard
                    },
                    contentPadding = PaddingValues(16.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color.LightGray),
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor, contentColor = Color.Black)
                ) {
                    Image(
                        painter = painterResource(
                            if (showKeyboard) {
                                R.drawable.baseline_keyboard_24
                            } else
                                R.drawable.baseline_qr_code_scanner_24
                            ),
                        contentDescription = "QR Code Scanner"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        if (showKeyboard) "Keyboard"
                        else "Barcode",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            LazyVerticalGrid(
                modifier = Modifier.weight(1f),
                columns = GridCells.Fixed(4),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(0.dp, androidx.compose.ui.Alignment.CenterHorizontally),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(productsWithCartQuantity) { productWithCartQuantity ->
                    Box(
                        modifier = Modifier
                            .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = {
                                    viewModel.onSelectedProductChange(productWithCartQuantity.product)
                                    showDialog.value = true
                                }
                            )
                        }
                    ) {
                        ProductItemView(
                            product = productWithCartQuantity.product,
                            quantityInCart = productWithCartQuantity.quantity,
                            incrementAction = {
                                viewModel.onSelectedProductChange(productWithCartQuantity.product)
                                showDialog.value = true
//                                viewModel.onSelectedProductChange(productWithCartQuantity.product)
//                                viewModel.incrementAction()
                            },
                            decrementAction = {
                                viewModel.onSelectedProductChange(productWithCartQuantity.product)
                                showDialog.value = true
//                                viewModel.onSelectedProductChange(productWithCartQuantity.product)
//                                viewModel.decrementAction()
                            }
                        )
                    }
                }
            }

            if (showDialog.value) {
                BasicAlertDialog(
                    onDismissRequest = {
                        showDialog.value = false
                    },
                    modifier = Modifier.background(Color.White)
                ) {
                    selectedProduct?.let { product ->

                        AddToCartDialogView(
                            product = product,
                            quantityInCart = viewModel.getProductCartQuantity(),
                            primaryButtonAction = { quantity ->
                                viewModel.addToCartWithStockCheck(
                                    product.productId,
                                    quantity = quantity
                                )
                                showDialog.value = false
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
