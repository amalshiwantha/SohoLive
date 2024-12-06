package com.soho.sohoapp.live.ui.view.screens.usage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.model.GlobalState
import com.soho.sohoapp.live.ui.components.CenterMessageProgress
import com.soho.sohoapp.live.ui.components.SpacerUp
import com.soho.sohoapp.live.ui.components.Text400_12sp
import com.soho.sohoapp.live.ui.components.Text400_14sp
import com.soho.sohoapp.live.ui.components.Text950_20sp
import com.soho.sohoapp.live.ui.components.TopAppBarCustomClose
import com.soho.sohoapp.live.ui.components.brushMainGradientBg
import com.soho.sohoapp.live.ui.theme.HintGray
import com.soho.sohoapp.live.ui.theme.ItemCardBg
import com.soho.sohoapp.live.ui.view.screens.subscription.NoNetView
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
        if (netUtil.isNetworkAvailable() && mState.usageRes.isEmpty()) {
            usageVm.loadUsage()
        }
    }

    //Main Content
    if (netUtil.isNetworkAvailable()) {
        MainContent(mState, mGState, onBackClick = {
            navController.popBackStack()
        })
    } else {
        NoNetView(onRetryClick = { usageVm.loadUsage() })
    }
}

@Composable
private fun MainContent(
    mState: UsageState,
    mGState: GlobalState,
    onBackClick: () -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .background(brushMainGradientBg)
    ) {
        val (actionBar, content) = createRefs()

        //TopActionBar
        Column(modifier = Modifier.constrainAs(actionBar) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }) {
            SpacerUp(size = 8.dp)

            TopAppBarCustomClose(
                title = "Your Usage",
                rightIcon = R.drawable.ic_close_circle,
                modifier = Modifier,
                onCloseClick = { onBackClick() }
            )

            SpacerUp(size = 64.dp)
        }

        //Content
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp)
            .constrainAs(content) {
                top.linkTo(actionBar.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            }) {
            UsageContent(mState, mGState)
        }
    }
}

@Composable
private fun UsageContent(mState: UsageState, mGState: GlobalState) {
    if (mState.isLoading) {
        CenterMessageProgress(message = mState.loadingMessage)
    } else {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {
            Text400_14sp(info = "Your usage information will only be shown after a 12 hours delay.")
            SpacerUp(size = 16.dp)
            UsageCard()
        }
    }
}

@Composable
private fun UsageCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = ItemCardBg)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            //Title
            Text950_20sp(title = "Usage Title")
            SpacerUp(size = 16.dp)

            //Info View
            Text400_12sp(
                modifier = Modifier,
                label = "Unfortunately, you can't modify your plan or view pricing in the app. We know it's not ideal.",
                txtColor = HintGray, isCenter = true
            )

            SpacerUp(size = 16.dp)
        }
    }
}