package com.soho.sohoapp.live.ui.navigation

import androidx.activity.result.ActivityResultRegistryOwner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.soho.sohoapp.live.network.response.DataGoLive
import com.soho.sohoapp.live.network.response.TsPropertyResponse
import com.soho.sohoapp.live.ui.view.activity.MainViewModel
import com.soho.sohoapp.live.ui.view.screens.golive.GoLiveScreen
import com.soho.sohoapp.live.ui.view.screens.home.FacebookProfileButton
import com.soho.sohoapp.live.ui.view.screens.home.HomeContent

@Composable
fun BottomNavHost(navController: NavHostController, mainViewModel: MainViewModel) {

    var onGoLiveResult by remember { mutableStateOf<DataGoLive?>(null) }
    var onGoLiveTsResult by remember { mutableStateOf<TsPropertyResponse?>(null) }

    NavHost(
        navController = navController, startDestination = NavigationPath.SCHEDULED.name
    ) {
        composable(route = NavigationPath.SCHEDULED.name) {
            HomeContent(navController, "SCHEDULED")
        }
        composable(route = NavigationPath.GO_LIVE.name) {
            GoLiveScreen(navController,mainViewModel, onGoLiveResult, onGoLiveTsResult,
                onLoadApiResults = { onGoLiveResult = it },
                onLoadTSResults = { onGoLiveTsResult = it })
        }
        composable(route = NavigationPath.VIDEO_LIBRARY.name) {
            HomeContent(navController, "VIDEO LIBRARY")
        }
        composable(route = NavigationPath.PROFILE.name) {
            HomeContent(navController, "PROFILE")
        }
    }
}