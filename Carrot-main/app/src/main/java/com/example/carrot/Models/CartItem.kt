package com.example.carrot.Models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import javax.inject.Inject


@Entity(
    tableName = "cart_items",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Product::class,
            parentColumns = ["productId"],
            childColumns = ["productId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CartItem (
    @PrimaryKey(autoGenerate = true) var cartItemId: Int = 0, // Unique ID for the cart item
    val userId: Int, // Reference to the user who added the product
    val productId: Int, // Reference to the product
    var quantity: Int, // Quantity of the product in the cart
    var price: Double // Price at the time of adding to cart (in case it changes later)
)
