package com.example.carrot.Views.HamburgerMenuViews

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HamburgerMenuApp() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(modifier = Modifier.width(300.dp)) { // Wider for tablets
                DrawerHeader()
                DrawerMenuItems(
                    items = listOf(
                        MenuItem(
                            id = "profile",
                            title = "Profile Details",
                            icon = Icons.Default.Person
                        ),
                        MenuItem(
                            id = "orders",
                            title = "Order Details",
                            icon = Icons.Default.ShoppingCart
                        )
                    ),
                    onItemClick = { item ->
                        navController.navigate(item.id)
                        scope.launch { drawerState.close() }
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("My App") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, "Menu")
                        }
                    }
                )
            }
        ) { padding ->
            NavHost(
                navController = navController,
                startDestination = "profile",
                modifier = Modifier.padding(padding)
            ) {
                composable("profile") { ProfileScreen() }
                composable("orders") { OrdersScreen() }
            }
        }
    }
}

@Composable
fun DrawerHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Welcome!",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "user@example.com",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun DrawerMenuItems(
    items: List<MenuItem>,
    onItemClick: (MenuItem) -> Unit
) {
    items.forEach { item ->
        NavigationDrawerItem(
            label = { Text(item.title) },
            selected = false,
            onClick = { onItemClick(item) },
            icon = { Icon(item.icon, contentDescription = null) },
            modifier = Modifier.padding(horizontal = 12.dp)
        )
    }
}

@Composable
fun ProfileScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Profile Details Screen")
    }
}

@Composable
fun OrdersScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Order Details Screen")
    }
}

data class MenuItem(
    val id: String,
    val title: String,
    val icon: ImageVector
)