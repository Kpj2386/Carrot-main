package com.example.carrot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import com.example.carrot.Views.AppNavigation
import com.example.carrot.Views.HamburgerMenuViews.HamburgerMenuApp
import com.example.carrot.ui.theme.CarrotTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CarrotTheme {
              AppNavigation(modifier = Modifier.fillMaxSize())
            }
        }
    }
}