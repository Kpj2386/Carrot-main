package com.example.carrot.Views

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.carrot.NavRoutes
import com.example.carrot.Views.CartViews.CartScreen
import com.example.carrot.Views.DialyDealViews.DialyDealsScreen
import com.example.carrot.Views.LoginScreen.LoginScreen
import com.example.carrot.Views.ProfileViews.ProfileScreen
import com.example.carrot.Views.SearchTab.SearchScreen

@Composable
fun AppNavigation(modifier: Modifier) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = NavRoutes.LOGIN_SCREEN) {
        composable(route = NavRoutes.LOGIN_SCREEN) {
            LoginScreen(navController = navController)
        }
        composable(route = NavRoutes.ROOT_SCREEN) {
            RootScreen(
                modifier = modifier,
                navController = navController)
        }
    }
}