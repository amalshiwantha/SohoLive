package com.soho.sohoapp.live.ui.view.screens.video_manage

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.soho.sohoapp.live.enums.FieldType
import com.soho.sohoapp.live.network.common.AlertState
import com.soho.sohoapp.live.network.common.ProgressBarState
import com.soho.sohoapp.live.network.response.VidPrivacy

data class VideoManageState(
    val isSuccess: Boolean = false,
    val lastSavedId: Long = 0,
    val loadingState: ProgressBarState = ProgressBarState.Idle,
    val loadingMessage: String = "Video Library Loading...",
    val alertState: AlertState = AlertState.Idle,
    val errorStates: Map<FieldType, String> = emptyMap(),
    var updatedPrivacy: MutableState<VidPrivacy?> = mutableStateOf(null)
)
