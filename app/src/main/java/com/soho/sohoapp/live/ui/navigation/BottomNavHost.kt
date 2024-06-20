package com.soho.sohoapp.live.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.soho.sohoapp.live.ui.view.screens.golive.GoLiveScreen
import com.soho.sohoapp.live.ui.view.screens.home.HomeContent

@Composable
fun BottomNavHost(navController: NavHostController) {

    var isLoadedGoLive by remember { mutableStateOf(false) }

    NavHost(
        navController = navController, startDestination = NavigationPath.SCHEDULED.name
    ) {
        composable(route = NavigationPath.SCHEDULED.name) {
            HomeContent(navController, "SCHEDULED")
        }
        composable(route = NavigationPath.GO_LIVE.name) {
            GoLiveScreen(navController, isLoadedGoLive, onDataLoad = {
                isLoadedGoLive = it
            })
        }
        composable(route = NavigationPath.VIDEO_LIBRARY.name) {
            HomeContent(navController, "VIDEO LIBRARY")
        }
        composable(route = NavigationPath.PROFILE.name) {
            HomeContent(navController, "PROFILE")
        }
    }
}