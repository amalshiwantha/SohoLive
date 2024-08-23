package com.soho.sohoapp.live.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.soho.sohoapp.live.model.GlobalState
import com.soho.sohoapp.live.model.GoLiveSubmit
import com.soho.sohoapp.live.model.ScheduleSlots
import com.soho.sohoapp.live.network.response.DataGoLive
import com.soho.sohoapp.live.network.response.TsPropertyResponse
import com.soho.sohoapp.live.ui.view.activity.main.MainViewModel
import com.soho.sohoapp.live.ui.view.screens.golive.GoLiveAssets
import com.soho.sohoapp.live.ui.view.screens.golive.GoLiveScreen
import com.soho.sohoapp.live.ui.view.screens.golive_success.GoLiveOkScreen
import com.soho.sohoapp.live.ui.view.screens.home.HomeContent
import com.soho.sohoapp.live.ui.view.screens.liveEnd.LiveEndScreen
import com.soho.sohoapp.live.ui.view.screens.schedule.ScheduleScreen
import com.soho.sohoapp.live.ui.view.screens.video.VideoLibraryScreen
import com.soho.sohoapp.live.ui.view.screens.video_manage.VideoManageScreen


@SuppressLint("MutableCollectionMutableState")
@Composable
fun BottomNavHost(
    navController: NavHostController, mainViewModel: MainViewModel,
    onTabMoveClick: (Int) -> Unit
) {

    var onGoLiveResult by remember { mutableStateOf<DataGoLive?>(null) }
    var onGoLiveTsResult by remember { mutableStateOf<TsPropertyResponse?>(null) }
    var mState by remember { mutableStateOf(GoLiveAssets()) }
    val scheduleSlots = remember { mutableStateListOf<ScheduleSlots>() }
    val mGoLiveSubmit by remember { mutableStateOf(GoLiveSubmit()) }
    val mGlobalState by remember { mutableStateOf(GlobalState()) }

    NavHost(
        navController = navController, startDestination = NavigationPath.GO_LIVE.name
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
                mGlobalState,
                onLoadApiResults = { onGoLiveResult = it },
                onLoadTSResults = { onGoLiveTsResult = it },
                onUpdateState = { mState = it })
        }
        composable(route = NavigationPath.VIDEO_LIBRARY.name) {
            VideoLibraryScreen(
                mGState = mGlobalState,
                navController = navController
            )
        }
        composable(route = NavigationPath.PROFILE.name) {
            HomeContent(navController, "Profile")
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
        composable(route = NavigationPath.VIDEO_MANAGE.name) {
            VideoManageScreen(
                navController = navController,
                mGState = mGlobalState
            )
        }
        composable(route = NavigationPath.LIVE_CAST_END.name) {
            LiveEndScreen(
                navController = navController,
                goVideoLibrary = {
                    //tab move
                    onTabMoveClick(1)

                    // Navigate to VideoLibraryScreen and clear the back stack
                    navController.navigate(NavigationPath.VIDEO_LIBRARY.name) {
                        // Pop up to the start destination (or a specific destination) and clear the stack
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = true
                        }
                        // Ensure the new screen is the top-most screen
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}