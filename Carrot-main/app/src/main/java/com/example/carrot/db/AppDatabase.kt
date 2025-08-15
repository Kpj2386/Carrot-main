package com.example.carrot.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.carrot.Models.CartItem
import com.example.carrot.Models.Product
import com.example.carrot.Models.User

@Database(entities = [User::class, Product::class, CartItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao

    companion object {
        const val DBNAME = "App_DB"
    }
}