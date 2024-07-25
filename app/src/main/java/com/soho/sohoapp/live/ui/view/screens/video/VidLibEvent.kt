package com.soho.sohoapp.live.ui.view.screens.video

import com.soho.sohoapp.live.model.VidLibRequest

sealed class VidLibEvent {
    data class CallLoadVideo(val request: VidLibRequest) : VidLibEvent()
    data object DismissAlert : VidLibEvent()
}
