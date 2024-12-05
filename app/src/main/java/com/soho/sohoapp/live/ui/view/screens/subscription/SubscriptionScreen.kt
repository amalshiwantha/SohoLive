package com.soho.sohoapp.live.ui.view.screens.subscription

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.soho.sohoapp.live.model.GlobalState
import com.soho.sohoapp.live.ui.components.CenterMessageProgress
import com.soho.sohoapp.live.ui.components.TextWhite12
import com.soho.sohoapp.live.ui.theme.AppGreen
import com.soho.sohoapp.live.ui.view.screens.golive.NoInternetScreen
import com.soho.sohoapp.live.utility.NetworkUtils
import org.koin.compose.koinInject

@Composable
fun SubscriptionScreen(
    mGState: GlobalState,
    navController: NavController,
    vmSubs: SubscriptionViewModel = koinInject(),
    netUtil: NetworkUtils = koinInject(),
) {
    val mState = vmSubs.mState.value

    //load main subs plans
    LaunchedEffect(key1 = "LoadPlans") {
        if (netUtil.isNetworkAvailable()) {
            vmSubs.loadPlans()
        }
    }

    //Main Content
    if (netUtil.isNetworkAvailable()) {
        MainContent(mState.isLoading)
    } else {
        NoInternetScreen(onRetryClick = {
            vmSubs.loadPlans()
        })
    }
}

@Composable
fun MainContent(isLoading: Boolean) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            CenterMessageProgress(message = "Loading...")
        } else {
            Column(modifier = Modifier.align(Alignment.Center)) {
                TextWhite12(title = "This Is Subscription", txtColor = AppGreen)
            }
        }
    }
}

@Preview
@Composable
private fun Screen() {
    MainContent(false)
}