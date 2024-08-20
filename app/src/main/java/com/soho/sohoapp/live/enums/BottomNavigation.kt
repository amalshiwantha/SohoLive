package com.soho.sohoapp.live.enums

import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.ui.navigation.NavigationPath

data class BottomNavigationItem(
    val label: String = "",
    val iconOn: Int = R.drawable.ic_bottom_schadule,
    val iconOff: Int = R.drawable.ic_bottom_schadule,
    val route: String = ""
) {
    fun navigationItems(): List<BottomNavigationItem> {
        return listOf(
            BottomNavigationItem(
                label = "Go Live",
                iconOn = R.drawable.ic_bottom_live,
                iconOff = R.drawable.ic_bottom_live_off,
                route = NavigationPath.GO_LIVE.name
            ),
            BottomNavigationItem(
                label = "Video Library",
                iconOn = R.drawable.ic_bottom_play,
                iconOff = R.drawable.ic_bottom_play_off,
                route = NavigationPath.VIDEO_LIBRARY.name
            ),
            BottomNavigationItem(
                label = "Account",
                iconOn = R.drawable.ic_bottom_schadule,
                iconOff = R.drawable.ic_bottom_schadule_off,
                route = NavigationPath.SCHEDULED.name
            ),
        )
    }
}