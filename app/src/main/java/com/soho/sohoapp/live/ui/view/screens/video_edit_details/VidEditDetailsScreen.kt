package com.soho.sohoapp.live.ui.view.screens.video_edit_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.model.GlobalState
import com.soho.sohoapp.live.ui.components.AppTopBar
import com.soho.sohoapp.live.ui.components.brushMainGradientBg
import org.koin.compose.koinInject

@Composable
fun VidEditDetailsScreen(
    mGState: GlobalState,
    navController: NavHostController,
    vmVidEdit: VidEditDetailsViewModel = koinInject(),
) {
    val states = vmVidEdit.mState.value

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppTopBar(
                title = "Video Details",
                isAllowBack = false,
                rightIcon = R.drawable.ic_close_circle,
                onBackClick = { }, onRightClick = { navController.popBackStack() })
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(brushMainGradientBg)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
            ) {
                //form content
            }
        }
    }
}


