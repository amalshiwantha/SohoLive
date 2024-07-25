package com.soho.sohoapp.live.ui.view.screens.video

import com.soho.sohoapp.live.model.VidLibRequest
import com.soho.sohoapp.live.network.response.VidPrivacyRequest

sealed class VidLibEvent {
    data class CallLoadVideo(val request: VidLibRequest) : VidLibEvent()
    data class CallUpdateVideo(val request: VidPrivacyRequest) : VidLibEvent()
    data object DismissAlert : VidLibEvent()
}
