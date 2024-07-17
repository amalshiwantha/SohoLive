package com.soho.sohoapp.live.ui.view.screens.golive

import com.soho.sohoapp.live.model.GoLiveSubmit

sealed class GoLiveEvent {
    data object CallLoadProperties : GoLiveEvent()
    data class CallSubmitGoLive(val submitData: GoLiveSubmit) : GoLiveEvent()
    data class CallSubmitSchedule(val submitData: GoLiveSubmit) : GoLiveEvent()
    data object DismissAlert : GoLiveEvent()
}
