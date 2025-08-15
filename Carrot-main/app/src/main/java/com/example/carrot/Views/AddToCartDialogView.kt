package com.example.carrot.Views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import com.example.carrot.Models.Product
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.carrot.Helper.AppConstants
import com.example.carrot.Helper.DisplayImageFromAssets
import com.example.carrot.R
import com.example.carrot.ui.theme.PrimaryOrange

@Composable
fun AddToCartDialogView(
    product: Product,
    quantityInCart: Int = 0,
    primaryButtonAction: (Int) -> Unit,
    secondaryButtonAction: (Int) -> Unit
) {
    val quantity = remember { mutableIntStateOf(quantityInCart) }
    val updateQuantity = { newQuantity: Int ->
        quantity.intValue = newQuantity
    }
    Column {

        Row(
            modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp)
        ) {
            DisplayImageFromAssets(fileName = product.productImage, modifier = Modifier.size(100.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Column() {
                Text(
                    product.brand,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    AppConstants.RUPEE + " " + product.price,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Thin
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            AddRemoveButtons(
                quantity = quantity.intValue,
                incrementAction = { updateQuantity(quantity.intValue + 1) },
                decrementAction = {
                    if (quantity.intValue > 0) {
                        updateQuantity(quantity.intValue - 1)
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        DialogBoxButtons(quantity = quantity.intValue, primaryButtonAction, secondaryButtonAction)
    }

}

@Composable
fun DialogBoxButtons(
    quantity: Int,
    primaryButtonAction: (Int) -> Unit,
    secondaryButtonAction: (Int) -> Unit
) {
    Row(
        modifier = Modifier.height(50.dp).fillMaxSize()
    ) {

        TextButton(
            onClick = {primaryButtonAction(quantity)},
            modifier = Modifier.weight(1f).height(50.dp).background(PrimaryOrange),
            colors = ButtonDefaults.buttonColors(contentColor = Color.White)
        ) {
            Text("Add to Cart")
        }

        VerticalDivider(modifier = Modifier.height(50.dp))

        TextButton(
            onClick = {secondaryButtonAction(quantity)},
            modifier = Modifier.weight(1f).height(50.dp).background(Color.White)
        ) {
            Text("Save for later")
        }

    }
}

@Composable
fun AddRemoveButtons(quantity: Int, incrementAction: () -> Unit, decrementAction: () -> Unit) {

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = decrementAction,
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            border = BorderStroke(width = 1.dp, color = Color.Black),
        ) {
            Image(
                painter = painterResource(R.drawable.baseline_remove_24),
                contentDescription = "Remove",
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(4.dp))

        Text("$quantity")

        Spacer(modifier = Modifier.width(4.dp))

        Button(
            onClick = incrementAction,
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            border = BorderStroke(width = 1.dp, color = Color.Black)
        ) {
            Image(
                painter = painterResource(R.drawable.baseline_add_24),
                contentDescription = "Add",
                modifier = Modifier.size(20.dp)
            )
        }
    }
}