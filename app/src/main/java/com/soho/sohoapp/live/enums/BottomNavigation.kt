package com.soho.sohoapp.live.enums

import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.ui.navigation.NavigationPath

data class BottomNavigationItem(
    val label: String = "",
    val icon: Int = R.drawable.ic_bottom_schadule,
    val route: String = ""
) {
    fun navigationItems(): List<BottomNavigationItem> {
        return listOf(
            BottomNavigationItem(
                label = "Scheduled",
                icon = R.drawable.ic_bottom_schadule,
                route = NavigationPath.SCHEDULED.name
            ),
            BottomNavigationItem(
                label = "Go Live",
                icon = R.drawable.ic_bottom_live,
                route = NavigationPath.GO_LIVE.name
            ),
            BottomNavigationItem(
                label = "Video Library",
                icon = R.drawable.ic_bottom_play,
                route = NavigationPath.VIDEO_LIBRARY.name
            ),
            BottomNavigationItem(
                label = "",
                icon = R.drawable.ic_bottom_profile,
                route = NavigationPath.PROFILE.name
            ),
        )
    }
}