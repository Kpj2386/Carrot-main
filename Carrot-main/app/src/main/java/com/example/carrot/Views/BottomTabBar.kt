package com.example.carrot.Views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.carrot.Models.TabItem
import com.example.carrot.R
import com.example.carrot.ui.theme.PrimaryOrange

@Composable
fun BottomTabBar(
    tabItems: List<TabItem> = emptyList(),
    selectedTabIndex: MutableIntState
) {
    Scaffold (
        topBar = {
            CompanyBar()
        },
        bottomBar = {
            TabRow(
                modifier = Modifier.shadow(elevation = 5.dp),
                selectedTabIndex = selectedTabIndex.intValue,
                contentColor = Color.Black
            ) {
                tabItems.forEachIndexed { index, tabItem ->
                    BottomTabIcon(index, selectedTabIndex, tabItem)
                }
            }
        }
    ) {  paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues)
        ) {
            tabItems[selectedTabIndex.intValue].view()
        }
    }
}

@Composable
fun BottomTabIcon(index: Int, selectedTabIndex: MutableIntState, item: TabItem) {
    Tab(
        selected = index == selectedTabIndex.intValue,
        onClick = {
            selectedTabIndex.intValue = index
        },
        text = { Text(item.title)},
        icon = {
            if(index == selectedTabIndex.intValue) {
                item.selectedIcon()
            } else {
                item.unselectedIcon()
            }
        })
}

@Composable
fun CompanyBar() {
    Row(
        modifier = Modifier.fillMaxWidth().background(
            color = PrimaryOrange
        ),
    ) {

        Image(
            painter = painterResource(id = R.drawable.cart_logo),
            contentDescription = "Carrot logo",
            modifier = Modifier.size(50.dp).padding(start = 16.dp)
        )
    }
}


