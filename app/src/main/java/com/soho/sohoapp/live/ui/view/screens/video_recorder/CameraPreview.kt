package com.soho.sohoapp.live.ui.view.screens.video_recorder

import android.content.res.Resources
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun CameraPreview(
    controller: LifecycleCameraController,
    modifier: Modifier = Modifier,
    camPadding: Float = 0f,  // 15% of screen width = 0.15
    isLandscape: Boolean = false
) {

    val displayMetrics = Resources.getSystem().displayMetrics
    val screenWidth = displayMetrics.widthPixels
    val screenH = displayMetrics.heightPixels
    val dynamicWPadding = (screenWidth * camPadding).toInt()
    val dynamicHPadding = (screenH * camPadding).toInt()

    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    AndroidView(
        factory = {
            PreviewView(it).apply {
                this.controller = controller
                controller.bindToLifecycle(lifecycleOwner)

                if (isLandscape) {
                    this.setPadding(0, dynamicHPadding, 0, dynamicHPadding)
                } else {
                    this.setPadding(dynamicWPadding, 0, dynamicWPadding, 0)
                }
            }
        },
        modifier = modifier
    )
}