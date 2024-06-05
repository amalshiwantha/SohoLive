package com.soho.sohoapp.live.view.screens.home

import android.content.Context
import android.content.Intent
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.enums.BottomNavItem
import com.soho.sohoapp.live.model.UiState
import com.soho.sohoapp.live.view.activity.HaishinActivity

@Composable
fun HomeScreen(navController: NavHostController, homeVm: HomeViewModel = viewModel()) {

    val context = LocalContext.current
    val uiState by homeVm.uiState.collectAsState()

    val navController = rememberNavController()
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomNavigation {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                BottomNavItem::class.sealedSubclasses.forEach { subclass ->
                    val item = subclass.objectInstance as BottomNavItem

                    BottomNavigationItem(
                        icon = {
                            Icon(
                                painterResource(item.icon),
                                contentDescription = item.label
                            )
                        },
                        label = { Text(item.label) },
                        selected = currentRoute == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Scheduled.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Scheduled.route) {
                ContentScheduled(uiState)
            }
            composable(BottomNavItem.GoLive.route) {
                ContentGoLive(uiState)
            }
            composable(BottomNavItem.VideoLibrary.route) {
                ContentVideoLibrary(uiState)
            }
            composable(BottomNavItem.Profile.route) {
                ContentProfile(uiState)
            }
        }

    }
}

@Composable
fun DefaultContent(innerPadding: PaddingValues, uiState: UiState, context: Context) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding), contentAlignment = Alignment.Center
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
fun ContentScheduled(uiState: UiState) {
    Box(
        modifier = Modifier
            .fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Scheduled")
        }
    }
}

@Composable
fun ContentGoLive(uiState: UiState) {
    Box(
        modifier = Modifier
            .fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("GoLive")
        }
    }
}

@Composable
fun ContentVideoLibrary(uiState: UiState) {
    Box(
        modifier = Modifier
            .fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("VideoLibrary")
        }
    }
}

@Composable
fun ContentProfile(uiState: UiState) {
    Box(
        modifier = Modifier
            .fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Profile")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHome() {
    HomeScreen(navController = NavHostController(LocalContext.current))
}