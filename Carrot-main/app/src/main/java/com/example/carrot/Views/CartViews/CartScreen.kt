package com.example.carrot.Views.CartViews

import android.view.Display
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.carrot.Helper.DisplayImageFromAssets
import com.example.carrot.R
import com.example.carrot.ViewModels.CartViewModel
import com.example.carrot.Views.Components.BigButton

@Composable
fun CartScreen(navController: NavController) {

    val viewModel = hiltViewModel<CartViewModel>()
    val showCartItems by viewModel.showCartItems.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFE8E9EA),
    ) {
        if (showCartItems) {
            Box(modifier = Modifier.background(Color.White), contentAlignment = Alignment.TopStart) {
                CartItemsView(viewModel)
            }
        } else {
            Box(modifier = Modifier.fillMaxSize()) {
                Text("No products are added into the cart", modifier = Modifier.align(Alignment.Center))
            }

        }
    }

}

@Composable
private fun CartItemsView(viewModel: CartViewModel) {
        Row(
            modifier = Modifier.fillMaxSize().background(Color(0xFFE8E9EA)).padding(horizontal = 16.dp),
            verticalAlignment = Alignment.Top
        )
        {
            Box(
                modifier = Modifier
                    .weight(2f)
                    .padding(vertical = 16.dp)
            ) {
                CartRowCard(viewModel)

            }

            Spacer(Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Spacer(modifier = Modifier.height(32.dp))
                Box(
                    modifier = Modifier.weight(.1f)
                ) {
                    BillCard(viewModel)
                }

                Box(
                    modifier = Modifier.weight(.1f)
                ) {
                    PaymentMethods()
                }
            }
        }
}

@Composable
fun PaymentMethods() {
    Column() {
        Text("Payment methods", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.size(16.dp))
        Row(

        ) {
            PaymentMethodBoxView(imageId = "phonePeLogo.png", text = "PhonePe")
            Spacer(modifier = Modifier.width(8.dp))
            PaymentMethodBoxView(imageId = "gpayLogo.png", text = "Google Pay")
        }
    }
}

@Composable
fun PaymentMethodBoxView(imageId: String, text: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier.size(100.dp),
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            shadowElevation = 1.dp
        ) {
            Box(
                modifier = Modifier.size(50.dp).fillMaxSize()
            ) {
                DisplayImageFromAssets(fileName = imageId, modifier = Modifier)
            }
        }

        Spacer(Modifier.height(4.dp))

        Text(text, style = MaterialTheme.typography.bodyMedium)
    }

}

@Composable
fun CartRowCard(viewModel: CartViewModel) {
    OutlinedCard(
        colors = CardDefaults.outlinedCardColors(Color.White)
    ) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp)
        ) {
            items(viewModel.cartItems) { item ->
                Box(modifier = Modifier.padding(bottom = 8.dp)) {
                    CartItemRowView(item.cartItemId)
                }

            }
        }

    }
}

@Composable
fun BillCard(viewModel: CartViewModel) {
    val grandTotalPrice by viewModel.cartGrandTotalPrice.collectAsState()
    val sgst by viewModel.sgst.collectAsState()
    val cgst by viewModel.cgst.collectAsState()
    val totalCost by viewModel.totalCartCost.collectAsState()

    OutlinedCard(
        colors = CardDefaults.outlinedCardColors(Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Total cost", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.weight(1f))
            Text("$totalCost", style = MaterialTheme.typography.bodyLarge)
        }
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            Text("SGST (9%)", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.weight(1f))
            Text("$sgst", style = MaterialTheme.typography.bodyLarge)
        }
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            Text("CGST (9%)", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.weight(1f))
            Text("$cgst", style = MaterialTheme.typography.bodyLarge)
        }
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Grand total", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.weight(1f))
            Text("$grandTotalPrice", style = MaterialTheme.typography.headlineMedium)
        }

    }
}

/*
@Composable
fun TableScreen() {
    // Just a fake data... a Pair of Int and String
    val tableData = (1..100).mapIndexed { index, item ->
        index to "Item $index"
    }
    // Each cell of a column must have the same weight.
    val column1Weight = .3f // 30%
    val column2Weight = .7f // 70%
    // The LazyColumn will be our table. Notice the use of the weights below
    LazyColumn(Modifier.fillMaxSize().padding(16.dp)) {
        // Here is the header
        item {
            Row(Modifier.background(Color.Gray)) {
                TableCell(text = "Column 1", weight = column1Weight)
                TableCell(text = "Column 2", weight = column2Weight)
            }
        }
        // Here are all the lines of your table.
        items(tableData.count()) {
            val (id, text) = tableData[it]
            Row(Modifier.fillMaxWidth()) {
                TableRow(rowNumber = id, color = Color.Gray )
//                TableCell(text = id.toString(), weight = column1Weight)
//                TableCell(text = text, weight = column2Weight)
            }
        }
    }
}

@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float
) {
    Text(
        text = text,
        Modifier
            .border(1.dp, Color.Black)
            .weight(weight)
            .padding(8.dp)
    )
}


@Composable
fun TwoColumnTable() {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Table Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
        ) {
            Text(
                text = "Row",
                modifier = Modifier
                    .weight(0.2f)
                    .padding(8.dp),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "View",
                modifier = Modifier
                    .weight(0.8f)
                    .padding(8.dp),
                fontWeight = FontWeight.Bold
            )
        }

        // Table Rows
        TableRow(rowNumber = 1, color = Color.Red)
        TableRow(rowNumber = 2, color = Color.Green)
        TableRow(rowNumber = 3, color = Color.Blue)
        // Add more rows as needed
    }
}

@Composable
fun TableRow(rowNumber: Int, color: Color) {
    val item = CartItem(
        userId = 0,
        productId = 1,
        quantity = 1,
        price = 20.0
    )
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(text = rowNumber.toString(),
                modifier = Modifier
                    .weight(0.2f)
                    .padding(8.dp))


        Surface(
            modifier = Modifier
                .weight(0.8f)
                .height(50.dp)
                .padding(8.dp)
                .background(color),
            color = color
        ) {
            CartItemRowView(cartItem = item)
        }
    }
}*/