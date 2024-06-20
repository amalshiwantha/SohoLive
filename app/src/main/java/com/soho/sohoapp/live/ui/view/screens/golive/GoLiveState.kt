package com.soho.sohoapp.live.ui.view.screens.golive

import com.soho.sohoapp.live.enums.FieldType
import com.soho.sohoapp.live.model.SignInRequest
import com.soho.sohoapp.live.network.common.AlertState
import com.soho.sohoapp.live.network.common.ProgressBarState

data class GoLiveState(
    val isSuccess: Boolean = false,
    val loadingState: ProgressBarState = ProgressBarState.Idle,
    val alertState: AlertState = AlertState.Idle,
    val errorStates: Map<FieldType, String> = emptyMap()
)
