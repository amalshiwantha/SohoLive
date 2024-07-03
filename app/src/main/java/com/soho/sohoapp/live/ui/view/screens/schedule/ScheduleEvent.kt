package com.soho.sohoapp.live.ui.view.screens.schedule

import com.soho.sohoapp.live.model.GoLiveSubmit

sealed class ScheduleEvent {
    data class CallSubmit(val submitData: GoLiveSubmit) : ScheduleEvent()
    data object DismissAlert : ScheduleEvent()
}
