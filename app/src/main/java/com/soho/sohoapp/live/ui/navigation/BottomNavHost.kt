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
import com.soho.sohoapp.live.network.response.TsPropertyResponse
import com.soho.sohoapp.live.ui.view.activity.MainViewModel
import com.soho.sohoapp.live.ui.view.screens.golive.GoLiveAssets
import com.soho.sohoapp.live.ui.view.screens.golive.GoLiveScreen
import com.soho.sohoapp.live.ui.view.screens.golive.GoLiveState
import com.soho.sohoapp.live.ui.view.screens.home.HomeContent
import com.soho.sohoapp.live.ui.view.screens.schedule.ScheduleScreen


@Composable
fun BottomNavHost(navController: NavHostController, mainViewModel: MainViewModel) {

    var onGoLiveResult by remember { mutableStateOf<DataGoLive?>(null) }
    var onGoLiveTsResult by remember { mutableStateOf<TsPropertyResponse?>(null) }
    var mState by remember { mutableStateOf(GoLiveAssets()) }

    NavHost(
        navController = navController, startDestination = NavigationPath.GO_LIVE.name
    ) {
        composable(route = NavigationPath.SCHEDULED.name) {
            HomeContent(navController, "SCHEDULED")
        }
        composable(route = NavigationPath.GO_LIVE.name) {
            GoLiveScreen(navController, mainViewModel, onGoLiveResult, onGoLiveTsResult, mState,
                onLoadApiResults = { onGoLiveResult = it },
                onLoadTSResults = { onGoLiveTsResult = it },
                onUpdateState = { mState = it })
        }
        composable(route = NavigationPath.VIDEO_LIBRARY.name) {
            HomeContent(navController, "VIDEO LIBRARY")
        }
        composable(route = NavigationPath.PROFILE.name) {
            HomeContent(navController, "PROFILE")
        }
        composable(route = NavigationPath.SET_SCHEDULE.name) {
            ScheduleScreen(navController = navController)
        }
    }
}