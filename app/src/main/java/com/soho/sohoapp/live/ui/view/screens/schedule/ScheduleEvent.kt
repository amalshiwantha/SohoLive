package com.soho.sohoapp.live.ui.view.screens.schedule

sealed class ScheduleEvent {
    data object CallSubmit : ScheduleEvent()
    data object DismissAlert : ScheduleEvent()
}
