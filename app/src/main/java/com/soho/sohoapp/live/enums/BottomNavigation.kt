package com.soho.sohoapp.live.enums

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screens(val route: String) {
    data object Scheduled : Screens("route_scheduled")
    data object GoLive : Screens("route_go_live")
    data object VideoLibrary : Screens("route_video_library")
    data object Profile : Screens("route_profile")
}

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
                route = Screens.Scheduled.route
            ),
            BottomNavigationItem(
                label = "Go Live",
                icon = Icons.Filled.Search,
                route = Screens.GoLive.route
            ),
            BottomNavigationItem(
                label = "Video Library",
                icon = Icons.Filled.Search,
                route = Screens.VideoLibrary.route
            ),
            BottomNavigationItem(
                label = "Profile",
                icon = Icons.Filled.AccountCircle,
                route = Screens.Profile.route
            ),
        )
    }
}