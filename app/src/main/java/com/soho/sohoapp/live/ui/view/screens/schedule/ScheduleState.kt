package com.soho.sohoapp.live.ui.view.screens.schedule

import com.soho.sohoapp.live.network.common.AlertState
import com.soho.sohoapp.live.network.common.ProgressBarState
import com.soho.sohoapp.live.network.response.DataGoLiveSubmit

data class ScheduleState(
    val isSuccess: Boolean = false,
    val loadingState: ProgressBarState = ProgressBarState.Idle,
    val loadingMessage: String = "Schedule Loading",
    val alertState: AlertState = AlertState.Idle,
    val results: DataGoLiveSubmit? = null,
)