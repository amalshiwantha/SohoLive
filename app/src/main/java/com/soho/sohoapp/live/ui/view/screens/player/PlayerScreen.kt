package com.soho.sohoapp.live.ui.view.screens.player

import android.net.Uri
import android.widget.MediaController
import android.widget.VideoView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.enums.AlertConfig
import com.soho.sohoapp.live.ui.components.AppAlertDialog
import com.soho.sohoapp.live.ui.components.AppTopBar
import com.soho.sohoapp.live.ui.components.brushMainGradientBg
import java.io.File

@Composable
fun PlayerScreen(navController: NavHostController, fileUri: Uri) {

    var isShowAlert by remember { mutableStateOf(false) }

    //show confirmation to delete video
    if (isShowAlert) {
        AppAlertDialog(
            alert = AlertConfig.DELETE_ALERT.apply {
                isConfirm = true
            },
            onConfirm = {
                deleteFileFromUri(fileUri)
                navController.popBackStack()
                isShowAlert = false
            },
            onDismiss = {
                isShowAlert = false
            })
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppTopBar(
                title = "",
                rightIcon = R.drawable.ic_trash,
                isAllowBack = false,
                onBackClick = { navController.popBackStack() }, onRightClick = {
                    //show confirmation to remove
                    isShowAlert = true

                })
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

                Box(modifier = Modifier.fillMaxSize()) {
                    AndroidView(
                        factory = { context ->
                            // Create a VideoView
                            val videoView = VideoView(context)

                            // Set up the MediaController for play/pause and seek controls
                            val mediaController = MediaController(context)
                            mediaController.setAnchorView(videoView)
                            videoView.setMediaController(mediaController)

                            // Set the video URI to the VideoView
                            videoView.setVideoURI(fileUri)

                            // Start the video automatically
                            videoView.setOnPreparedListener { it.start() }

                            videoView
                        },
                        update = {
                            it.setVideoURI(fileUri)
                            it.start()
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }

            }
        }
    }
}

fun deleteFileFromUri(fileUri: Uri): Boolean {
    return try {
        val file = File(fileUri.path) // Convert Uri to File
        if (file.exists()) {
            file.delete() // Delete the file
        } else {
            false
        }
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}