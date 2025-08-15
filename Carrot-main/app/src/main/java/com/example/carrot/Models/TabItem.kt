package com.example.carrot.Models

import androidx.compose.runtime.Composable

data class TabItem(
    val title: String,
    val selectedIcon: @Composable () -> Unit,
    val unselectedIcon: @Composable () -> Unit,
    val view: @Composable () -> Unit
)