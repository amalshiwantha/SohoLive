package com.soho.sohoapp.live.ui.view.screens.splash

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.Firebase
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.ui.components.brushMainGradientBg
import com.soho.sohoapp.live.ui.navigation.NavigationPath
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    splashViewModel: SplashViewModel = koinViewModel()
) {

    val isSplashVisible = remember { mutableStateOf(true) }
    val isLoggedIn by splashViewModel.isLoggedIn.collectAsState()


    FetchRemoteConfig()

    LaunchedEffect(Unit) {
        // Fetch a new Installation ID
        FirebaseInstallations.getInstance().id.addOnSuccessListener { newId ->
            println("MyFirebase ID: $newId")
        }.addOnFailureListener { e ->
            println("MyFirebase Failed ID: ${e.message}")
        }

        delay(1000)
        isSplashVisible.value = false
        screenNavigation(isLoggedIn, navController)
    }

    if (isSplashVisible.value) {
        SplashViewContent(modifier)
    }
}

@Composable
fun FetchRemoteConfig() {
    val remoteConfig = Firebase.remoteConfig
    val configSettings = remoteConfigSettings {
        minimumFetchIntervalInSeconds = 3600 // Adjust as needed
    }
    remoteConfig.setConfigSettingsAsync(configSettings)

    LaunchedEffect(Unit) {
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val msg = remoteConfig.getString("welcome_message")
                    Log.d("MyFirebase RC", "Fetch OK $msg")
                } else {
                    Log.e("MyFirebase RC", "Fetch failed", task.exception)
                }
            }
    }


}

private fun screenNavigation(isLoggedIn: Boolean, navController: NavHostController) {
    if (isLoggedIn) {
        // Navigate to the HomeScreen
        navController.navigate(NavigationPath.HOME.name) {
            popUpTo(NavigationPath.SPLASH.name) { inclusive = true }
        }
    } else {
        // Navigate to the PreAccessScreen
        navController.navigate(NavigationPath.PRE_ACCESS.name) {
            popUpTo(NavigationPath.SPLASH.name) { inclusive = true }
        }
    }
}

@Composable
private fun SplashViewContent(modifier: Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(brushMainGradientBg),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(id = R.drawable.soho_livecast_logo),
                contentDescription = null,
                modifier = Modifier.wrapContentSize()
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewSplash() {
    SplashScreen(navController = NavHostController(LocalContext.current))
}