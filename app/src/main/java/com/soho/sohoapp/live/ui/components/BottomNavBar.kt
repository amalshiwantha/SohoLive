package com.soho.sohoapp.live.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.enums.BottomNavigationItem
import com.soho.sohoapp.live.ui.theme.AppRed
import com.soho.sohoapp.live.ui.theme.BottomBarBg
import com.soho.sohoapp.live.ui.theme.BottomBarSelect
import com.soho.sohoapp.live.ui.theme.BottomBarUnselect

@Composable
fun BottomNavigationBar(
    navController: NavController, selectedItem: Int, onTabClick: (Int) -> Unit
) {
    var navigationSelectedItem = selectedItem

    NavigationBar(
        containerColor = BottomBarBg
    ) {
        BottomNavigationItem().navigationItems().forEachIndexed { index, navigationItem ->
                NavigationBarItem(colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = BottomBarSelect,
                    selectedTextColor = BottomBarSelect,
                    unselectedIconColor = BottomBarUnselect,
                    unselectedTextColor = BottomBarUnselect,
                    indicatorColor = BottomBarBg,

                    ), selected = index == navigationSelectedItem, label = {
                    if (navigationItem.label.isNotEmpty()) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                text = navigationItem.label,
                            )

                            Icon(
                                modifier = Modifier.fillMaxWidth(),
                                painter = painterResource(id = R.drawable.ic_red_dot),
                                contentDescription = navigationItem.label,
                                tint = if (index == navigationSelectedItem) {
                                    AppRed
                                } else {
                                    BottomBarBg
                                }
                            )


                        }
                    }
                }, icon = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = navigationItem.icon),
                            contentDescription = navigationItem.label,
                            modifier = if (navigationItem.label.isEmpty()) {
                                Modifier.size(42.dp)
                            } else {
                                Modifier.size(24.dp)
                            }
                        )

                        if (navigationItem.label.isEmpty()) {
                            Icon(
                                modifier = Modifier.fillMaxWidth(),
                                painter = painterResource(id = R.drawable.ic_red_dot),
                                contentDescription = navigationItem.label,
                                tint = if (index == navigationSelectedItem) {
                                    AppRed
                                } else {
                                    BottomBarBg
                                }
                            )
                        }
                    }
                }, onClick = {
                    onTabClick(index)
                    navigationSelectedItem = index
                    navController.navigate(navigationItem.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                })
            }
    }
}