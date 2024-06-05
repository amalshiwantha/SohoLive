package com.soho.sohoapp.live.enums

import com.soho.sohoapp.live.R

sealed class BottomNavItem(val route: String, val icon: Int, val label: String) {
    object Scheduled : BottomNavItem("scheduled", R.drawable.inspection, "Scheduled")
    object GoLive : BottomNavItem("goLive", R.drawable.inspection, "Go Live")
    object VideoLibrary : BottomNavItem("videoLibrary", R.drawable.inspection, "Video Library")
    object Profile : BottomNavItem("profile", R.drawable.inspection, "Profile")
}