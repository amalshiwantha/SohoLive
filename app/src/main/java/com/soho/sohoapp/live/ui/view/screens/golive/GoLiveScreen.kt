package com.soho.sohoapp.live.ui.view.screens.golive

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.soho.sohoapp.live.ui.components.SearchBar
import com.soho.sohoapp.live.ui.components.Text400_14sp
import com.soho.sohoapp.live.ui.components.Text700_14sp
import com.soho.sohoapp.live.ui.components.Text950_20sp
import com.soho.sohoapp.live.ui.components.brushMainGradientBg
import com.soho.sohoapp.live.utility.NetworkUtils
import org.koin.compose.koinInject

@Composable
fun GoLiveScreen(
    navController: NavController,
    goLiveVm: GoLiveViewModel = koinInject(),
    netUtil: NetworkUtils = koinInject()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brushMainGradientBg)
    ) {
        ScreenContent()
    }
}

@Composable
fun ScreenContent() {
    Column(
        modifier = Modifier.padding(
            horizontal = 16.dp, vertical = 24.dp
        )
    ) {
        StepCountTitleInfo()

        Spacer(modifier = Modifier.height(40.dp))

        SearchBar()

        Spacer(modifier = Modifier.height(16.dp))
    }
}


@Composable
fun StepCountTitleInfo() {
    Text700_14sp(step = "Step 1 of 4")
    Spacer(modifier = Modifier.height(8.dp))
    Text950_20sp(title = "Link livestream to your property")
    Spacer(modifier = Modifier.height(8.dp))
    Text400_14sp(info = "Prospect interested in your listing will be notified of your scheduled livestream and will be livecasted on your property listing page.")
}
