package com.soho.sohoapp.live.ui.view.screens.player

import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.net.Uri
import android.provider.MediaStore
import android.widget.MediaController
import android.widget.VideoView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.enums.AlertConfig
import com.soho.sohoapp.live.ui.components.AppAlertDialog
import com.soho.sohoapp.live.ui.components.AppTopBar
import com.soho.sohoapp.live.ui.components.ButtonColoured
import com.soho.sohoapp.live.ui.components.ButtonOutlineWhiteNormal
import com.soho.sohoapp.live.ui.components.SpacerSide
import com.soho.sohoapp.live.ui.components.brushMainGradientBg
import com.soho.sohoapp.live.ui.theme.AppGreen
import java.io.File

@Composable
fun PlayerScreen(navController: NavHostController, fileUri: Uri, onNextClick: () -> Unit = {}) {

    var isShowAlert by remember { mutableStateOf(false) }
    var isPlaying by remember { mutableStateOf(false) }
    var thumbnailBitmap by remember { mutableStateOf<Bitmap?>(null) }

    // Extract the thumbnail from the video
    LaunchedEffect(fileUri) {
        thumbnailBitmap = getVideoThumbnail(fileUri)
    }

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
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ButtonOutlineWhiteNormal(
                    text = "Edit Details",
                    onBtnClick = {},
                    modifier = Modifier.weight(1f)
                )
                SpacerSide(size = 16.dp)
                ButtonColoured(
                    text = "Next",
                    onBtnClick = { onNextClick() },
                    color = AppGreen,
                    modifier = Modifier.weight(1f)
                )
            }
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

                    if (!isPlaying) {
                        thumbnailBitmap?.let { bitmap ->
                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = "Video Thumbnail",
                                modifier = Modifier
                                    .fillMaxSize()
                            )
                        }
                    }

                    //Player
                    if (isPlaying) {
                        AndroidView(
                            factory = { context ->
                                // Create a VideoView
                                val videoViewInstance = VideoView(context)

                                // Set up the MediaController for play/pause and seek controls
                                val mediaController = MediaController(context)
                                mediaController.setAnchorView(videoViewInstance)
                                videoViewInstance.setMediaController(mediaController)

                                // Set the video URI to the VideoView
                                videoViewInstance.setVideoURI(fileUri)

                                // Start the video automatically
                                videoViewInstance.setOnPreparedListener {
                                    it.start()
                                    it.pause()
                                }

                                videoViewInstance
                            },
                            update = {
                                it.setVideoURI(fileUri)
                                it.start()
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    // Play IconButton in the center
                    IconButton(
                        onClick = {
                            isPlaying = true
                        },
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(56.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_play),
                            contentDescription = "Play"
                        )
                    }

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

fun getVideoThumbnail(videoUri: Uri): Bitmap? {
    return ThumbnailUtils.createVideoThumbnail(
        File(videoUri.path).toString(),
        MediaStore.Images.Thumbnails.MINI_KIND
    )
}
