package com.example.carrot.Views

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Discount
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Discount
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.carrot.AppManager.UserSession
import com.example.carrot.Models.TabItem
import com.example.carrot.NavRoutes
import com.example.carrot.ViewModels.CartViewModel
import com.example.carrot.Views.CartViews.CartScreen
import com.example.carrot.Views.DialyDealViews.DialyDealsScreen
import com.example.carrot.Views.ProfileViews.ProfileScreen
import com.example.carrot.Views.SearchTab.SearchScreen


@Composable
fun RootScreen(modifier: Modifier,
               navController: NavHostController) {

    val cartViewModel = hiltViewModel<CartViewModel>()

    // Observe the total cart item quantity
    val totalCartItemQuantity by cartViewModel.totalCartItemQuantity.collectAsState(initial = 0)

    val showLogoutDialog = remember { mutableStateOf(false) }

    // Handle back button press
    BackHandler {
        showLogoutDialog.value = true // Show the logout dialog
    }

    val tabs: List<TabItem> = listOf(
        TabItem(
            title = "Home",
            selectedIcon = { Icon(imageVector = Icons.Filled.Home, contentDescription = "Home") },
            unselectedIcon = { Icon(imageVector = Icons.Outlined.Home, contentDescription = "Home") },
            view = { HomeScreen(modifier = modifier, navController = navController) }
        ),
        TabItem(
            title = "Search",
            selectedIcon = { Icon(imageVector = Icons.Filled.Search, contentDescription = "Search") },
            unselectedIcon = { Icon(imageVector = Icons.Outlined.Search, contentDescription = "Search") },
            view = { SearchScreen(navController = navController) }
        ),
        TabItem(
            title = "Cart",
            selectedIcon = {
                BadgedBox(badge = { Badge { Text(text = "$totalCartItemQuantity") } }) {
                    Icon(imageVector = Icons.Filled.ShoppingCart, contentDescription = "Cart")
                }
            },
            unselectedIcon = {
                BadgedBox(badge = { Badge { Text(text = "$totalCartItemQuantity") } }) {
                    Icon(imageVector = Icons.Outlined.ShoppingCart, contentDescription = "Cart")
                }
            },
            view = { CartScreen(navController = navController) }
        ),
        TabItem(
            title = "Deals",
            selectedIcon = { Icon(imageVector = Icons.Filled.Discount, contentDescription = "Deals") },
            unselectedIcon = { Icon(imageVector = Icons.Outlined.Discount, contentDescription = "Deals") },
            view = { DialyDealsScreen(navController = navController) }
        ),
        TabItem(
            title = "Profile",
            selectedIcon = { Icon(imageVector = Icons.Filled.Person, contentDescription = "Profile") },
            unselectedIcon = { Icon(imageVector = Icons.Outlined.Person, contentDescription = "Profile") },
            view = { ProfileScreen(navController = navController) }
        )
    )

    val selectedTabIndex = remember { mutableIntStateOf(0) }


    Column {
        Box(modifier = Modifier
                .weight(1f)
        ){
            BottomTabBar(
                tabItems = tabs,
                selectedTabIndex = selectedTabIndex
            )


            // Logout confirmation dialog
            if (showLogoutDialog.value) {
                AlertDialog(
                    onDismissRequest = {
                        showLogoutDialog.value = false // Hide the dialog if dismissed
                    },
                    title = {
                        Text(text = "Logout")
                    },
                    text = {
                        Text(text = "Are you sure you want to logout?")
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showLogoutDialog.value = false
                                UserSession.logoutAction()
                                // Perform logout actions here
                                navController.navigate(NavRoutes.LOGIN_SCREEN) {
                                    popUpTo("root") { inclusive = true }
                                }

                            }
                        ) {
                            Text("Logout")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                showLogoutDialog.value = false // Hide the dialog
                            }
                        ) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    }
}

