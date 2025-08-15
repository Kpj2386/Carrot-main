package com.example.carrot.Views.CartViews

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.carrot.Helper.AppConstants
import com.example.carrot.Helper.DisplayImageFromAssets
import com.example.carrot.Models.Product
import com.example.carrot.ViewModels.CartItemRowViewModel
import com.example.carrot.Views.Components.QuantityButton

@Composable
fun CartItemRowView(cartItemId: Int) {
    val viewModel: CartItemRowViewModel = hiltViewModel(
        key = cartItemId.toString(),
        creationCallback = { factory: CartItemRowViewModel.MyViewModelFactory ->
            factory.create(cartItemId)
        }
    )

    val cartProduct by viewModel.product.collectAsState()
    val cartItem by viewModel.cartItem.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onLoadingOfScreen()
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Product Image
        Card(
            modifier = Modifier.padding(10.dp),
            border = BorderStroke(width = 1.dp, color = Color.Gray)
        ) {
            cartProduct?.let { product ->
                DisplayImageFromAssets(
                    fileName = product.productImage,
                    modifier = Modifier.size(75.dp))
            } ?: DisplayImageFromAssets(
                fileName = "",
                modifier = Modifier.size(75.dp))
        }

        // Product Details
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = cartProduct?.productName ?: "Loading...",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = cartProduct?.productSize ?: "Loading...",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = cartProduct?.price?.let { "${AppConstants.RUPEE}$it" } ?: "Loading...",
                style = MaterialTheme.typography.titleLarge
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Quantity Selector
        Box(
            modifier = Modifier
                .width(100.dp)
                .height(35.dp)
                .border(BorderStroke(1.dp, Color(0xFFFFA500)), RoundedCornerShape(8.dp))
        ) {
            cartItem?.let { item ->
                QuantityButton(
                    quantityInCart = item.quantity,
                    incrementAction = { viewModel.incrementAction() },
                    decrementAction = { viewModel.decrementAction() }
                )
            }
//                ?: CircularProgressIndicator(modifier = Modifier.size(20.dp))
        }
    }
}