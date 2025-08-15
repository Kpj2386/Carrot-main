package com.example.carrot.Views

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.carrot.Helper.DisplayImageFromAssets
import com.example.carrot.Helper.StockStatus
import com.example.carrot.Models.Product
import com.example.carrot.ViewModels.CartViewModel
import com.example.carrot.ViewModels.HomeScreenViewModel
import com.example.carrot.Views.CartViews.CartRowCard
import com.example.carrot.ui.theme.PrimaryOrange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController, modifier: Modifier) {

    val viewModel = hiltViewModel<HomeScreenViewModel>()
    val textFieldValue by viewModel.searchedText.collectAsState("")
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    var showDialog by rememberSaveable { mutableStateOf(false) }
    val searchedProduct by viewModel.product.collectAsState()

    val p = Product(
        productId = 1,
        category = "Vegetables",
        subCategory = "Fresh Vegetables",
        productCode = 123456789,
        productName = "Organic Carrot",
        productDescription = "Freshly harvested organic carrots",
        productImage = "product_image_16",
        brand = "Organic Farms Ltd.",
        price = 1.99,
        discountPrice = null,
        subscriptionPrice = 1.50,
        productSize = "1 kg",
        stockStatus = StockStatus.IN_STOCK
    )
    Box(
        modifier = Modifier
            .padding(32.dp)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Top,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

               Text(
                   "Start Scanning",
                   style = MaterialTheme.typography.headlineMedium,
                   modifier = Modifier.padding(bottom = 16.dp)
               )

                Box(
                   contentAlignment = Alignment.BottomCenter
                ) {
                    DisplayImageFromAssets(
                        fileName = "BarCodeScanner.png",
                        modifier = Modifier.size(450.dp).clip(CircleShape)
                            .background(Color.White)
                    )

                    OutlinedTextField(
                        value = textFieldValue,
                        onValueChange = {
                            val newProductCode = it.filter { char ->
                                char.isDigit()
                            }
                            viewModel.onProductCodeChange(newProductCode)
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                                keyboardController?.hide()
                                viewModel.searchButtonAction { isProductFound ->
                                    if (isProductFound) {
                                        showDialog = true
                                    } else {
                                        Toast.makeText(navController.context,
                                            "No item found",
                                            Toast.LENGTH_LONG)
                                            .show()
                                    }
                                }
                            }
                        ),
                        label = { Text("Product code") },
                        modifier = Modifier
                        .wrapContentHeight()
                            .fillMaxWidth()
                        .padding(16.dp),
                        trailingIcon = {
                            Row(
                                modifier = Modifier
                                    .padding(end = 8.dp), // Adjust padding as needed
                                horizontalArrangement = Arrangement.End,
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .clickable {
                                            keyboardController?.hide()
                                            viewModel.searchButtonAction { isValid ->
                                                if (isValid) {
                                                    showDialog = true
                                                } else {
                                                    Toast.makeText(
                                                        navController.context,
                                                        "Please enter a valid product code",
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                                }
                                            }
                                        }
                                        .background(
                                            color = PrimaryOrange,
                                            shape = RoundedCornerShape(100)
                                        ),
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = "Next",
                                )
                            }
                        },
                        singleLine = true,
                        textStyle = TextStyle(fontSize = 16.sp)
                    )
                }
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {
                CartViewInHomeScreen()
            }

            if (showDialog && searchedProduct != null) {
                BasicAlertDialog(
                    onDismissRequest = {
                        showDialog = false
                    },
                    modifier = Modifier.background(Color.White)
                ) {
                    searchedProduct?.let {
                        AddToCartDialogView(
                            product = it,
                            quantityInCart = viewModel.getProductCartQuantity(),
                            primaryButtonAction = { quantity ->
                                viewModel.addToCartAction(quantity)
                                showDialog = false
                            }, secondaryButtonAction = { quantity ->
                                Log.d("HomeScreen", "Save for later: $quantity")
                                showDialog = false
                            }
                        )

                    }
                }
            }
        }
    }
}

@Composable
fun CartViewInHomeScreen() {
    val viewModel = hiltViewModel<CartViewModel>()
    val showCartItems by viewModel.showCartItems.collectAsState()
    val grandTotalPrice by viewModel.cartGrandTotalPrice.collectAsState()

    Column() {
        Text(
            "Cart Items",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Box(
            modifier = Modifier.weight(1f),
        ) {
            if (showCartItems) {
                CartRowCard(viewModel)
            } else {
                OutlinedCard(
                    colors = CardDefaults.outlinedCardColors(Color.White),
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Your cart is empty")
                    }
                }
            }
        }

        OutlinedCard(
            colors = CardDefaults.outlinedCardColors(Color.White),
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Grand Total (GST Incl.)")
                Spacer(modifier = Modifier.weight(.1f))
                Text("${grandTotalPrice}")
            }
        }
    }

}
