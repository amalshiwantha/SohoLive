package com.soho.sohoapp.live.ui.view.screens.video_recorder

import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
    isRecordMode: Boolean = false
) {
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    val aspectRatio = if (isLandscape) 9f / 16f else 16f / 9f

    LaunchedEffect(controller) {
        if (isRecordMode) {
            controller.setVideoCaptureQualitySelector(QualitySelector.from(Quality.HD))
        }
    }

    if (isRecordMode) {
        Box(
            modifier = modifier
                .aspectRatio(aspectRatio)
                .fillMaxWidth()
        ) {
            AndroidView(
                factory = {
                    PreviewView(it).apply {
                        this.controller = controller
                        controller.bindToLifecycle(lifecycleOwner)
                        this.scaleType = PreviewView.ScaleType.FIT_CENTER

                        if (camPadding.first != 0 && camPadding.second != 0) {
                            if (isLandscape) {
                                this.setPadding(
                                    camPadding.second,
                                    camPadding.second,
                                    0,
                                    camPadding.second
                                )
                            } else {
                                this.setPadding(camPadding.first, 0, camPadding.first, 0)
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    } else {
        AndroidView(
            factory = {
                PreviewView(it).apply {
                    this.controller = controller
                    controller.bindToLifecycle(lifecycleOwner)

                    if (camPadding.first != 0 && camPadding.second != 0) {
                        if (isLandscape) {
                            this.setPadding(
                                camPadding.second,
                                camPadding.second,
                                0,
                                camPadding.second
                            )
                        } else {
                            this.setPadding(camPadding.first, 0, camPadding.first, 0)
                        }
                    }
                }
            },
            modifier = modifier
        )
    }
}