package com.soho.sohoapp.live.ui.view.screens.golive

sealed class GoLiveEvent {
    data object CallLoadProperties : GoLiveEvent()
    data object DismissAlert : GoLiveEvent()
}
