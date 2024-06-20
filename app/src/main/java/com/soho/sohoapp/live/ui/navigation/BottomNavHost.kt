package com.soho.sohoapp.live.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.soho.sohoapp.live.network.response.DataGoLive
import com.soho.sohoapp.live.ui.view.screens.golive.GoLiveScreen
import com.soho.sohoapp.live.ui.view.screens.home.HomeContent

@Composable
fun BottomNavHost(navController: NavHostController) {

    var onGoLiveResult by remember { mutableStateOf<DataGoLive?>(null) }

    NavHost(
        navController = navController, startDestination = NavigationPath.SCHEDULED.name
    ) {
        composable(route = NavigationPath.SCHEDULED.name) {
            HomeContent(navController, "SCHEDULED")
        }
        composable(route = NavigationPath.GO_LIVE.name) {
            GoLiveScreen(navController, onGoLiveResult, onLoadApiResults = {
                onGoLiveResult = it
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