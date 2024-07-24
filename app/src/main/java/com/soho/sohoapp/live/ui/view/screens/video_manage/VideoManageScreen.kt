package com.soho.sohoapp.live.ui.view.screens.video_manage

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.soho.sohoapp.live.model.GoLiveSubmit
import com.soho.sohoapp.live.ui.components.Text700_14sp

@Composable
fun VideoManageScreen(
    goLiveData: GoLiveSubmit,
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {

    Column {
        println("myData "+goLiveData.videoItem?.title)
        Text700_14sp(step = "Video Manage ${goLiveData.videoItem?.title}")
    }
}