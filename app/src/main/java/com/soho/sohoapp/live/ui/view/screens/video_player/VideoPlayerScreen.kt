package com.soho.sohoapp.live.ui.view.screens.video_player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavHostController
import com.soho.sohoapp.live.ui.components.AppTopBarCustom
import com.soho.sohoapp.live.ui.components.CenterMessageProgress
import com.soho.sohoapp.live.ui.components.brushMainGradientBg
import com.soho.sohoapp.live.ui.theme.AppGreen

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
    var isStartPlay by remember { mutableStateOf(false) }

    // Initialize ExoPlayer
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri(url)
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true
        }
    }

    // Set up a listener for buffering state
    DisposableEffect(Unit) {
        val listener = object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)

                if (isPlaying) {
                    isStartPlay = true
                }

            }
        }
        exoPlayer.addListener(listener)

        onDispose {
            exoPlayer.removeListener(listener)
            exoPlayer.release()
        }
    }

    DisposableEffect(

        if (isStartPlay) {
            AndroidView (
                modifier = modifier,
                factory = {
                    PlayerView(context).apply {
                        player = exoPlayer
                    }
                }
            )
        } else {
            CenterMessageProgress(message = "Video Loading...")
        }
    ) {
        onDispose {
            exoPlayer.release()
        }
    }
}


