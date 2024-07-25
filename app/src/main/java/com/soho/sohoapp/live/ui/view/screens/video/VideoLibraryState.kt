package com.soho.sohoapp.live.ui.view.screens.video

import androidx.compose.runtime.MutableState
import com.soho.sohoapp.live.enums.FieldType
import com.soho.sohoapp.live.network.common.AlertState
import com.soho.sohoapp.live.network.common.ProgressBarState
import com.soho.sohoapp.live.network.response.DataVidRes
import com.soho.sohoapp.live.network.response.VidLibResponse

data class VideoLibraryState(
    val isSuccess: Boolean = false,
    val loadingState: ProgressBarState = ProgressBarState.Idle,
    val loadingMessage: String = "Video Library Loading...",
    val alertState: AlertState = AlertState.Idle,
    val errorStates: Map<FieldType, String> = emptyMap(),
    var sApiResponse: MutableState<DataVidRes>? = null
)
