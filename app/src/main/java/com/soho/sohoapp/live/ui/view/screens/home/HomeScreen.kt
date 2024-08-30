package com.soho.sohoapp.live.ui.view.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.datastore.AppDataStoreManager
import com.soho.sohoapp.live.model.GlobalState
import com.soho.sohoapp.live.ui.components.AppTopBar
import com.soho.sohoapp.live.ui.components.BottomNavigationBar
import com.soho.sohoapp.live.ui.components.HandleBackPress
import com.soho.sohoapp.live.ui.components.brushMainGradientBg
import com.soho.sohoapp.live.ui.navigation.BottomNavHost
import com.soho.sohoapp.live.ui.navigation.NavigationPath
import com.soho.sohoapp.live.ui.theme.AppGreen
import com.soho.sohoapp.live.ui.view.activity.main.MainViewModel

@Composable
fun HomeScreen(
    navControllerHome: NavHostController,
    homeVm: HomeViewModel = viewModel(),
    viewMMain: MainViewModel
) {
    val context = LocalContext.current
    val uiState by homeVm.uiState.collectAsState()
    val navController = rememberNavController()
    var navigationSelectedItem by remember { mutableIntStateOf(0) }
    var selectedTabTitle by remember { mutableStateOf(context.getString(R.string.create_livestream)) }
    var showBottomBar by remember { mutableStateOf(true) }
    var showTopBar by remember { mutableStateOf(true) }

    LaunchedEffect(navController) {
        navController.currentBackStackEntryFlow.collect { backStackEntry ->
            selectedTabTitle = when (backStackEntry.destination.route) {
                NavigationPath.SCHEDULED.name -> context.getString(R.string.scheduled_livestreams)
                NavigationPath.GO_LIVE.name -> context.getString(R.string.create_livestream)
                NavigationPath.VIDEO_LIBRARY.name -> context.getString(R.string.video_library)
                NavigationPath.PROFILE.name -> context.getString(R.string.manage_profile)
                else -> context.getString(R.string.scheduled_livestreams)
            }
        }
    }

    //HandleBackPress
    HandleBackPress(navController = navController)

    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        if (showBottomBar && showTopBar) {
            AppTopBar(title = selectedTabTitle,
                isAllowBack = false,
                onBackClick = { },
                onRightClick = {})
        }
    }, bottomBar = {
        if (showBottomBar) {
            BottomNavigationBar(navController, navigationSelectedItem, onTabClick = {
                navigationSelectedItem = it
            })
        }
    }) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(brushMainGradientBg)
                .padding(innerPadding)
        ) {
            BottomNavHost(navController, viewMMain, onTabMoveClick = {
                navigationSelectedItem = it
            })

            val currentBackStackEntry by navController.currentBackStackEntryAsState()
            showBottomBar = isBottomBarVisible(currentBackStackEntry)
            showTopBar = isTopBarVisible(currentBackStackEntry)

        }
    }
}

fun isBottomBarVisible(backStack: NavBackStackEntry?): Boolean {
    backStack?.let {
        when (backStack.destination.route) {
            NavigationPath.GO_LIVE_SUCCESS.name -> return false
            NavigationPath.SET_SCHEDULE.name -> return false
            NavigationPath.VIDEO_MANAGE.name -> return false
            NavigationPath.LIVE_CAST_END.name -> return false
            else -> return true
        }
    } ?: run {
        return true
    }
}

fun isTopBarVisible(backStack: NavBackStackEntry?): Boolean {
    backStack?.let {
        when (backStack.destination.route) {
            NavigationPath.PROFILE.name -> return false
            else -> return true
        }
    } ?: run {
        return true
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHome() {
    HomeScreen(
        navControllerHome = NavHostController(LocalContext.current),
        viewMMain = MainViewModel(dataStore = AppDataStoreManager(LocalContext.current))
    )
}