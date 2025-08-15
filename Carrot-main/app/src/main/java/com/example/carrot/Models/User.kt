package com.example.carrot.Models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "users"
)
data class User(
    @PrimaryKey(autoGenerate = true) val userId: Int,
    val phoneNumber: String,
    val name: String,
    val email: String,
    val address: String
)
