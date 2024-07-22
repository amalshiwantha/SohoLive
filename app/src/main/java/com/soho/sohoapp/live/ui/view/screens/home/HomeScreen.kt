package com.soho.sohoapp.live.ui.view.screens.home

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.compose.ui.res.dimensionResource
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
import com.soho.sohoapp.live.model.UiState
import com.soho.sohoapp.live.ui.components.AppTopBar
import com.soho.sohoapp.live.ui.components.BottomNavigationBar
import com.soho.sohoapp.live.ui.components.brushMainGradientBg
import com.soho.sohoapp.live.ui.navigation.BottomNavHost
import com.soho.sohoapp.live.ui.navigation.NavigationPath
import com.soho.sohoapp.live.ui.theme.AppGreen
import com.soho.sohoapp.live.ui.view.activity.HaishinActivity
import com.soho.sohoapp.live.ui.view.activity.MainViewModel

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
    var selectedTabTitle by remember { mutableStateOf("SCHEDULED") }
    var showBottomBar by remember { mutableStateOf(true) }

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

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (showBottomBar) {
                AppTopBar(
                    title = selectedTabTitle,
                    isAllowBack = false,
                    onBackClick = { })
            }
        },
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(navController, navigationSelectedItem, onTabClick = {
                    navigationSelectedItem = it
                })
            }
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(brushMainGradientBg)
                .padding(innerPadding)
        ) {
            BottomNavHost(navController, viewMMain)

            val currentBackStackEntry by navController.currentBackStackEntryAsState()
            showBottomBar = isBottomBarVisible(currentBackStackEntry)

        }
    }
}

fun isBottomBarVisible(backStack: NavBackStackEntry?): Boolean {
    backStack?.let {
        when (backStack.destination.route) {
            NavigationPath.GO_LIVE_SUCCESS.name -> return false
            NavigationPath.SET_SCHEDULE.name -> return false
            else -> return true
        }
    } ?: run {
        return true
    }
}

@Composable
fun GoLiveScreenActivity(uiState: UiState, context: Context) {
    Box(
        modifier = Modifier
            .fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Welcome to the SohoLive ${uiState.loadingMessage}")
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))
            Button(onClick = {
                //homeVm.setMessage("Updated")
                val intent = Intent(context, HaishinActivity::class.java)
                context.startActivity(intent)
            }) {
                Text("Update Message")
            }
        }
    }
}

@Composable
fun HomeContent(navController: NavController, title: String) {

    Box(
        modifier = Modifier
            .background(brushMainGradientBg)
            .fillMaxSize()
    ) {
        Column(modifier = Modifier.align(Alignment.Center)) {
            Text(
                title,
                style = MaterialTheme.typography.titleLarge,
                color = AppGreen,
                modifier = Modifier
                    .padding(vertical = 20.dp)
            )
            Button(onClick = { }) {
                Text(text = "Connect")
            }
        }
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