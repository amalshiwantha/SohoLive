package com.soho.sohoapp.live.ui.view.screens.video_library

import com.soho.sohoapp.live.model.VidLibRequest
import com.soho.sohoapp.live.network.response.VidPrivacyRequest
import com.soho.sohoapp.live.network.response.VideoDeleteReq

sealed class VidLibEvent {
    data class CallLoadVideo(val request: VidLibRequest) : VidLibEvent()
    data class CallUpdateVideo(val request: VidPrivacyRequest) : VidLibEvent()
    data class CallDeleteVideo(val request: VideoDeleteReq) : VidLibEvent()
    data object DismissAlert : VidLibEvent()
}
