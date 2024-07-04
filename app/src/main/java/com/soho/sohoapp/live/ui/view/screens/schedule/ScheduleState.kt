package com.soho.sohoapp.live.ui.view.screens.schedule

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import com.soho.sohoapp.live.enums.FieldType
import com.soho.sohoapp.live.network.common.AlertState
import com.soho.sohoapp.live.network.common.ProgressBarState
import com.soho.sohoapp.live.network.response.DataGoLive
import com.soho.sohoapp.live.network.response.TsPropertyResponse

data class ScheduleState(
    val isSuccess: Boolean = false,
    val loadingState: ProgressBarState = ProgressBarState.Idle,
    val loadingMessage: String = "Schedule Loading",
    val alertState: AlertState = AlertState.Idle,
    val results: DataGoLive? = null,
)