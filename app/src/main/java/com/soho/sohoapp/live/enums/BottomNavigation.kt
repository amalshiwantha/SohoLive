package com.soho.sohoapp.live.enums

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.soho.sohoapp.live.ui.navigation.NavigationPath

data class BottomNavigationItem(
    val label: String = "",
    val icon: ImageVector = Icons.Filled.Home,
    val route: String = ""
) {
    fun navigationItems(): List<BottomNavigationItem> {
        return listOf(
            BottomNavigationItem(
                label = "Scheduled",
                icon = Icons.Filled.Home,
                route = NavigationPath.SCHEDULED.name
            ),
            BottomNavigationItem(
                label = "Go Live",
                icon = Icons.Filled.Search,
                route = NavigationPath.GO_LIVE.name
            ),
            BottomNavigationItem(
                label = "Video Library",
                icon = Icons.Filled.Search,
                route = NavigationPath.VIDEO_LIBRARY.name
            ),
            BottomNavigationItem(
                label = "Profile",
                icon = Icons.Filled.AccountCircle,
                route = NavigationPath.PROFILE.name
            ),
        )
    }
}