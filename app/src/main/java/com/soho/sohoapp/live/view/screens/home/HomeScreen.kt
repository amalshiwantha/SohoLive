package com.soho.sohoapp.live.view.screens.home

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.enums.Screens
import com.soho.sohoapp.live.model.UiState
import com.soho.sohoapp.live.view.activity.HaishinActivity
import com.soho.sohoapp.live.view.ui.components.BottomNavigationBar

@Composable
fun HomeScreen(navControllerHome: NavHostController, homeVm: HomeViewModel = viewModel()) {

    val context = LocalContext.current
    val uiState by homeVm.uiState.collectAsState()
    var navigationSelectedItem by remember { mutableStateOf(0) }
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomNavigationBar(navController, navigationSelectedItem, onTabClick = {
                navigationSelectedItem = it
            })
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = Screens.Scheduled.route,
            modifier = Modifier.padding(paddingValues = innerPadding)
        ) {
            composable(Screens.Scheduled.route) {
                HomeContent(navController, "Home Content")
            }
            composable(Screens.GoLive.route) {
                GoLiveScreen(uiState, context)
            }
            composable(Screens.VideoLibrary.route) {
                HomeContent(navController, "Video Library")
            }
            composable(Screens.Profile.route) {
                HomeContent(navController, "Profile Content")
            }
        }

    }
}

@Composable
fun GoLiveScreen(uiState: UiState, context: Context) {
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
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(horizontal = 15.dp, vertical = 10.dp)
                    .clip(MaterialTheme.shapes.large)
            ) {
                Image(
                    painter = painterResource(R.drawable.inspection),
                    contentDescription = "home_screen_bg",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Text(
                title,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(vertical = 20.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHome() {
    HomeScreen(navControllerHome = NavHostController(LocalContext.current))
}