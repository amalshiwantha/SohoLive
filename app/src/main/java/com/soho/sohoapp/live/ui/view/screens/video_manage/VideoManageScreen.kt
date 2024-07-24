package com.soho.sohoapp.live.ui.view.screens.video_manage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.model.GoLiveSubmit
import com.soho.sohoapp.live.model.VideoItem
import com.soho.sohoapp.live.ui.components.AppTopBar
import com.soho.sohoapp.live.ui.components.Text400_14sp
import com.soho.sohoapp.live.ui.components.brushMainGradientBg
import com.soho.sohoapp.live.ui.theme.AppPrimaryDark

@Composable
fun VideoManageScreen(
    goLiveData: GoLiveSubmit,
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            AppTopBar(
                title = "Manage Video",
                isAllowBack = false,
                rightIcon = R.drawable.ic_share,
                onBackClick = {  },
                onRightClick = { navController.popBackStack() })
        },
        content = { innerPadding ->
            goLiveData.videoItem?.let {
                MainContent(it, innerPadding)
            } ?: run {
                NoDataView(innerPadding)
            }
        }
    )
}

@Composable
fun MainContent(videoItem: VideoItem, innerPadding: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        Column {
            Text400_14sp(info = videoItem.title.orEmpty(), color = AppPrimaryDark)
        }
    }
}

@Composable
fun NoDataView(innerPadding: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brushMainGradientBg)
            .padding(innerPadding)
    ) {
        Text400_14sp(info = "No Video Data", color = AppPrimaryDark)
    }
}
