package com.soho.sohoapp.live.ui.view.screens.player

import android.net.Uri
import android.widget.MediaController
import android.widget.VideoView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController

@Composable
fun PlayerScreen(navController: NavHostController, fileUri: Uri) {

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
