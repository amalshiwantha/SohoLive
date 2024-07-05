package com.soho.sohoapp.live.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.soho.sohoapp.live.model.GoLiveSubmit
import com.soho.sohoapp.live.model.ScheduleSlots
import com.soho.sohoapp.live.network.response.DataGoLive
import com.soho.sohoapp.live.network.response.TsPropertyResponse
import com.soho.sohoapp.live.ui.view.activity.MainViewModel
import com.soho.sohoapp.live.ui.view.screens.golive.GoLiveAssets
import com.soho.sohoapp.live.ui.view.screens.golive.GoLiveScreen
import com.soho.sohoapp.live.ui.view.screens.golive_success.GoLiveOkScreen
import com.soho.sohoapp.live.ui.view.screens.home.HomeContent
import com.soho.sohoapp.live.ui.view.screens.schedule.ScheduleScreen


@SuppressLint("MutableCollectionMutableState")
@Composable
fun BottomNavHost(navController: NavHostController, mainViewModel: MainViewModel) {

    var onGoLiveResult by remember { mutableStateOf<DataGoLive?>(null) }
    var onGoLiveTsResult by remember { mutableStateOf<TsPropertyResponse?>(null) }
    var mState by remember { mutableStateOf(GoLiveAssets()) }
    val scheduleSlots = remember { mutableStateListOf<ScheduleSlots>() }
    val mGoLiveSubmit by remember { mutableStateOf(GoLiveSubmit()) }

    NavHost(
        navController = navController, startDestination = NavigationPath.SCHEDULED.name
    ) {
        composable(route = NavigationPath.SCHEDULED.name) {
            HomeContent(navController, "SCHEDULED")
        }
        composable(route = NavigationPath.GO_LIVE.name) {
            GoLiveScreen(navController,
                mainViewModel,
                onGoLiveResult,
                onGoLiveTsResult,
                mState,
                mGoLiveSubmit,
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
            ScheduleScreen(
                goLiveData = mGoLiveSubmit,
                scheduleSlots = scheduleSlots,
                navController = navController
            )
        }
        composable(route = NavigationPath.GO_LIVE_SUCCESS.name) {
            GoLiveOkScreen(
                navController = navController,
                goLiveData = mGoLiveSubmit
            )
        }

    }
}