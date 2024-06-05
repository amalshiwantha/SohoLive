package com.soho.sohoapp.live.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.soho.sohoapp.live.enums.BottomNavigationItem
import com.soho.sohoapp.live.ui.theme.BottomBarBg
import com.soho.sohoapp.live.ui.theme.BottomBarSelect
import com.soho.sohoapp.live.ui.theme.BottomBarUnselect

@Composable
fun BottomNavigationBar(
    navController: NavController,
    selectedItem: Int,
    onTabClick: (Int) -> Unit
) {
    var navigationSelectedItem = selectedItem

    NavigationBar(
        containerColor = BottomBarBg
    ) {
        BottomNavigationItem().navigationItems()
            .forEachIndexed { index, navigationItem ->
                NavigationBarItem(
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BottomBarSelect,
                        selectedTextColor = BottomBarSelect,
                        unselectedIconColor = BottomBarUnselect,
                        unselectedTextColor = BottomBarUnselect,
                        indicatorColor = BottomBarBg,

                        ),
                    selected = index == navigationSelectedItem,
                    label = {
                        Text(navigationItem.label)
                    },
                    icon = {
                        Icon(
                            painterResource(id = navigationItem.icon),
                            contentDescription = navigationItem.label
                        )
                    },
                    onClick = {
                        onTabClick(index)
                        navigationSelectedItem = index
                        navController.navigate(navigationItem.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
    }
}