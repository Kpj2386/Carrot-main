package com.example.carrot.Models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.carrot.Helper.StockStatus
import com.google.gson.annotations.SerializedName

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true) @SerializedName("Id") val productId: Int,
    val category: String,
    @SerializedName("sub_category") val subCategory: String,
    @SerializedName("product_code") val productCode: Int,
    @SerializedName("product_name") val productName: String,
    @SerializedName("product_description") val productDescription: String,
    @SerializedName("product_image") val productImage: String,
    @SerializedName("Brand") val brand: String,
    val price: Double,
    @SerializedName("discount_price") val discountPrice: Double?,
    @SerializedName("subscription_price") val subscriptionPrice: Double,
    @SerializedName("product_size") val productSize: String,
    @SerializedName("stock_status") val stockStatus: StockStatus
)


