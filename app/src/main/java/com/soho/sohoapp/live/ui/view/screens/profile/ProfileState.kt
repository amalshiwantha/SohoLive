package com.soho.sohoapp.live.ui.view.screens.profile

import com.soho.sohoapp.live.network.common.AlertState
import com.soho.sohoapp.live.network.common.ProgressBarState

data class ProfileState(
    val isSuccess: Boolean = false,
    val loadingState: ProgressBarState = ProgressBarState.Idle,
    val loadingMessage: String = "Schedule Loading",
    val alertState: AlertState = AlertState.Idle,
    val profileName: String? = null,
    val profileImage: String? = null,
    val appVersion: String? = null,
)