package com.soho.sohoapp.live.ui.view.screens.splash

import android.content.Intent
import android.net.Uri
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.SohoLiveApp.Companion.context
import com.soho.sohoapp.live.model.RemoteConfigModel
import com.soho.sohoapp.live.ui.components.UpdateAlertDialog
import com.soho.sohoapp.live.ui.components.brushMainGradientBg
import com.soho.sohoapp.live.ui.navigation.NavigationPath
import com.soho.sohoapp.live.utility.getAppVersion
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import org.koin.androidx.compose.koinViewModel

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    splashViewModel: SplashViewModel = koinViewModel()
) {

    val isLoggedIn by splashViewModel.isLoggedIn.collectAsState()
    val isSplashVisible = remember { mutableStateOf(true) }
    var configData by remember { mutableStateOf<RemoteConfigModel?>(null) }
    var showUpdateDialog by remember { mutableStateOf(false) }
    var isAppOpen by remember { mutableStateOf(false) }

    //Check App Updates
    LaunchedEffect(Unit) {

        delay(1000)

        fetchRemoteData { result ->
            configData = result

            configData?.let {
                if (it.versionCode > getAppVersion().second) {
                    //has updated
                    showUpdateDialog = true
                } else {
                    //no updates
                    isAppOpen = true
                }
            } ?: kotlin.run {
                isAppOpen = true
            }
        }
    }

    //If having app update display an alert
    if (showUpdateDialog) {
        configData?.let { config ->
            UpdateAlertDialog(
                message = config.message,
                isCritical = config.level == "critical",
                onUpdate = {
                    // Redirect to Play Store
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=${context.packageName}")
                    ).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                    context.startActivity(intent)
                },
                onCancel = {
                    showUpdateDialog = false
                    isAppOpen = true
                }
            )
        }
    }

    //Start navigation
    if (isAppOpen) {
        appOpen(isSplashVisible, isLoggedIn, navController)
        isAppOpen = false
    }

    if (isSplashVisible.value) {
        SplashViewContent(modifier)
    }
}

// Set Remote Config settings to force a fresh fetch
private fun fetchRemoteData(onResult: (RemoteConfigModel?) -> Unit) {
    val remoteConfig = FirebaseRemoteConfig.getInstance()

    val configSettings = remoteConfigSettings {
        minimumFetchIntervalInSeconds = 0 // Force immediate fetch
        fetchTimeoutInSeconds = 5 // Reduce fetch timeout to 5s
    }
    remoteConfig.setConfigSettingsAsync(configSettings)

    remoteConfig.fetchAndActivate()
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val jsonString = remoteConfig.getString("android_version")

                try {
                    val configData = Json.decodeFromString<RemoteConfigModel>(jsonString)
                    onResult(configData)
                } catch (e: Exception) {
                    Log.e("MyFirebase RC", "JSON parsing error", e)
                    onResult(null)
                }
            } else {
                Log.e("MyFirebase RC", "Fetch failed", task.exception)
                onResult(null)
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

private fun appOpen(
    isSplashVisible: MutableState<Boolean>,
    isLoggedIn: Boolean,
    navController: NavHostController
) {
    isSplashVisible.value = false
    screenNavigation(isLoggedIn, navController)
}

@Preview(showBackground = true)
@Composable
fun PreviewSplash() {
    SplashScreen(navController = NavHostController(LocalContext.current))
}