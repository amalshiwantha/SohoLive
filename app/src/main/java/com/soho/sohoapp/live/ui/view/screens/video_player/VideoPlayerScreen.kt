package com.soho.sohoapp.live.ui.view.screens.video_player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavHostController
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.ui.components.AppTopBar
import com.soho.sohoapp.live.ui.components.AppTopBarCustom
import com.soho.sohoapp.live.ui.components.TopAppBarCustomClose
import com.soho.sohoapp.live.ui.components.brushMainGradientBg

@Composable
fun VideoPlayerScreen(navController: NavHostController, title: String, url: String) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brushMainGradientBg)
    ) {
        AppTopBarCustom(title = title,
            modifier = Modifier.fillMaxWidth(),
            onBackClick = { navController.popBackStack() })

        ExoPlayerView(
            url = url,
            modifier = Modifier.fillMaxSize()
        )
    }
}


@Composable
private fun ExoPlayerView(url: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    // Initialize ExoPlayer
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri(url)
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true
        }
    }

    DisposableEffect(
        AndroidView(
            modifier = modifier,
            factory = {
                PlayerView(context).apply {
                    player = exoPlayer
                }
            }
        )
    ) {
        onDispose {
            exoPlayer.release()
        }
    }
}


