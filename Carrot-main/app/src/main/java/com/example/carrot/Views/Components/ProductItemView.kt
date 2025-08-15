package com.example.carrot.Views.Components

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.carrot.Helper.AppConstants
import com.example.carrot.Helper.DisplayImageFromAssets
import com.example.carrot.Models.Product
import com.example.carrot.R

@Composable
fun ProductItemView(
    product: Product,
    quantityInCart: Int,
    incrementAction: () -> Unit,
    decrementAction: () -> Unit
) {
    Box(
        contentAlignment = Alignment.TopEnd
    ) {
        Row() {
            ThirdProduct(
                product = product,
                quantityInCart = quantityInCart,
                incrementAction = incrementAction,
                decrementAction = decrementAction
            )
        }
    }
}

@Composable
fun ThirdProduct(
    product: Product,
    quantityInCart: Int,
    incrementAction: () -> Unit,
    decrementAction: () -> Unit
) {
    // TODO: Calculate discount percentage -- discount percentage shouldn't be an double it should be a interger like 50,23 etc
    val discountPercentage = product.discountPrice?.let {
        ((product.price - it) / product.price) * 100
    }?.toInt()

    Card(
        modifier = Modifier.width(250.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
        ) {
            // Image with discount overlay
            Box(
                modifier = Modifier
                    .height(130.dp)
                    .padding(bottom = 8.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.TopEnd

            ) {
                // Product Image
                DisplayImageFromAssets(
                    fileName = product.productImage,
                    modifier = Modifier
                        .fillMaxSize()
                )

                // Discount Star with percentage
                if (discountPercentage != null) {
                    DiscountCanvas(text = "${discountPercentage}%")
                }
            }

            // Brand and Product Name with fixed height
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp) // Fixed height for two lines of text
            ) {
                Text(
                    text = product.brand + " " + product.productName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight(600),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Product Size
            Text(
                text = product.productSize,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight(100)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Price and Add Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PriceDisplay(actualPrice = product.price.toString(), discountedPrice = product.discountPrice?.toString())

                Spacer(modifier = Modifier.weight(1f))

                // Add and quantity button
                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .height(35.dp)
                        .border(BorderStroke(1.dp, Color(0xFFFFA500)), RoundedCornerShape(8.dp))
                ) {
                    if (quantityInCart > 0) {
                        QuantityButton(quantityInCart, incrementAction, decrementAction)
                    } else {
                        AddButton(incrementAction)
                    }
                }
            }
        }
    }
}

@Composable
fun PriceDisplay(actualPrice: String, discountedPrice: String?) {
    Row(
        verticalAlignment = Alignment.Bottom
    ) {
        if (discountedPrice != null) {
            Text(
                text = AppConstants.RUPEE + discountedPrice,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight(400)
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            textDecoration = TextDecoration.LineThrough,
                            color = Color.Gray
                        )
                    ) {
                        append(AppConstants.RUPEE + actualPrice)
                    }
                },
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight(400)
            )
        } else {
            Text(
                text = AppConstants.RUPEE + actualPrice,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight(400)
            )
        }
    }
}



@Composable
fun QuantityButton(
    quantityInCart: Int,
    incrementAction: () -> Unit,
    decrementAction: () -> Unit
) {
    // Wrap the Row in a clickable modifier
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFFFA500).copy(alpha = 0.4f))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            // "-" Text with brighter background
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(Color(0xFFFFA500).copy(alpha = 0.8f))
                    .clickable { decrementAction() } // Handle increment action
            ) {
                Text(
                    text = "-",
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp) // Increase font size
                )
            }

            // Quantity with white background
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(Color.White) // White background
            ) {
                Text(
                    text = "$quantityInCart",
                    color = Color(0xFFFFA500),
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp) // Increase font size
                )
            }


            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(Color(0xFFFFA500).copy(alpha = 0.8f)) // Brighter background
                    .clickable { incrementAction() } // Handle increment action
            ) {
                Text(
                    text = "+",
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp) // Increase font size
                )
            }
        }
    }
}

@Composable
fun AddButton(addAction: () -> Unit) {
    Button(
        onClick = {
            Log.d("AddButton", "Add button clicked")
            addAction()
                  },
        shape = RoundedCornerShape(size = 8.dp),
        colors = buttonColors(
            containerColor = Color(0xFFFFA500)
        ),
        modifier = Modifier
            .height(35.dp)
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "ADD",
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp)
            )
        }
    }
}


@Composable
fun FirstProduct(product: Product) {
    Card(
        modifier = Modifier.width(200.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = R.drawable.product_image_16),
                contentDescription = null,
                modifier = Modifier
                    .height(130.dp)
                    .padding(8.dp)
                    .clip(CircleShape)
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
            Text(
                text = "Amul milk",
                style = MaterialTheme.typography.headlineSmall
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
            Text(
                text = "This is a milk product from Amul",
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
            Row(
            ) {
                Text(
                    text = "1 Ltr",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight(100)
                )

                Spacer(modifier = Modifier.weight(.1f))

                Text(
                    text = "₹5.9",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
    /*
    @Composable
fun SecondProduct(
    product: Product,
    quantityInCart: Int, // Add this parameter
    addAction: () -> Unit
) {
    Card(
        modifier = Modifier.width(250.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
        ) {
            // Image
            DisplayImageFromAssets(
                fileName = product.productImage,
                modifier = Modifier
                    .height(130.dp)
                    .padding(bottom = 8.dp)
                    .fillMaxWidth()
            )

            // Brand and Product Name with fixed height
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp) // Fixed height for two lines of text
            ) {
                Text(
                    text = product.brand + " " + product.productName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight(600),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Product Size
            Text(
                text = product.productSize,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight(100)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Price and Add Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PriceDisplay(actualPrice = product.price.toString(), discountedPrice = product.discountPrice?.toString())

                Spacer(modifier = Modifier.weight(1f))

                Box(
                    modifier = Modifier
                        .width(100.dp) // Set a fixed width
                        .height(35.dp) // Set a fixed height
                        .border(BorderStroke(1.dp, Color(0xFFFFA500)), RoundedCornerShape(8.dp))
                ) {
                    if (quantityInCart > 0) {
                        QuantityButton(quantityInCart, incrementAction, decrementAction)
                    } else {
                        AddButton(addAction)
                    }
                }
            }
        }
    }
}
     */
}



