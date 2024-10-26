package com.soho.sohoapp.live.ui.view.screens.pre_rec_library

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.soho.sohoapp.live.enums.FieldType
import com.soho.sohoapp.live.network.common.AlertState
import com.soho.sohoapp.live.network.common.ProgressBarState

data class PreRecLibState(
    val isSuccess: Boolean = false,
    val loadingState: ProgressBarState = ProgressBarState.Idle,
    val loadingMessage: String = "Video Library Loading...",
    val alertState: AlertState = AlertState.Idle,
    val errorStates: Map<FieldType, String> = emptyMap(),
    var updatedPrivacy: MutableState<Boolean> = mutableStateOf(false)
)
