package com.example.carrot.AppManager

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.carrot.Models.Product
import com.example.carrot.Models.User
import com.example.carrot.db.AppDatabase
import com.google.gson.Gson
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class MainApplication : Application() {

    @Inject
    lateinit var appDatabase: AppDatabase

    override fun onCreate() {
        super.onCreate()
        // Initialize the database
        initializeDatabase()
    }

    private fun initializeDatabase() {
        val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val productsAdded = sharedPreferences.getBoolean("products_added", false)

        if (!productsAdded) {
            println("First launch of the app: Products need to be added to the database.")
            // Launch a coroutine to insert products
            CoroutineScope(Dispatchers.IO).launch {
                // Read JSON and insert products
                val jsonString = AssetManager().readJsonFromAssets(this@MainApplication, "products.json")
                val productList: List<Product> = Gson().fromJson(jsonString, Array<Product>::class.java).toList()

                // Read users and insert user entities
                val usersJsonString = AssetManager().readUsersJsonFromAssets(this@MainApplication, "users.json")
                val userList: List<User> = Gson().fromJson(usersJsonString, Array<User>::class.java).toList()


                val productDao = appDatabase.productDao()
                productList.forEach { product ->
                    productDao.insert(product)
                }

                val userDao = appDatabase.userDao()
                userList.forEach { user ->
                    userDao.insertUser(user)
                }

                // Set the flag to true
                sharedPreferences.edit().putBoolean("products_added", true).apply()
            }
        }
    }
}