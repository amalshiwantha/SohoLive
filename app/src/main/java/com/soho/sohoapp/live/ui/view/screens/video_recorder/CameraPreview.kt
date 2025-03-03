package com.soho.sohoapp.live.ui.view.screens.video_recorder

import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun CameraPreview(
    controller: LifecycleCameraController,
    modifier: Modifier = Modifier,
    camPadding: Pair<Int, Int> = Pair(0, 0),
    isLandscape: Boolean = false,
    isRecording: Boolean = false
) {
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

    LaunchedEffect(controller) {
        if (isRecording) {
            controller.setVideoCaptureQualitySelector(QualitySelector.from(Quality.HD))
        }
    }

    AndroidView(
        factory = {
            PreviewView(it).apply {
                this.controller = controller
                controller.bindToLifecycle(lifecycleOwner)

                if (camPadding.first != 0 && camPadding.second != 0) {
                    if (isLandscape) {
                        this.setPadding(camPadding.second, camPadding.second, 0, camPadding.second)
                    } else {
                        this.setPadding(camPadding.first, 0, camPadding.first, 0)
                    }
                }
            }
        },
        modifier = modifier
    )
}