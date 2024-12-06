package com.soho.sohoapp.live.ui.view.screens.usage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.soho.sohoapp.live.model.GlobalState
import com.soho.sohoapp.live.ui.components.Text950_20sp
import com.soho.sohoapp.live.ui.components.brushMainGradientBg
import com.soho.sohoapp.live.utility.NetworkUtils
import org.koin.compose.koinInject

@Composable
fun UsageScreen(
    mGState: GlobalState,
    navController: NavController,
    usageVm: UsageViewModel = koinInject(),
    netUtil: NetworkUtils = koinInject(),
) {
    val mState = usageVm.mState.value

    LaunchedEffect("load_usage") {
        usageVm.loadUsage()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brushMainGradientBg)
    ) {
        Text950_20sp(title = "Usage Screen", modifier = Modifier.align(Alignment.Center))
    }
}