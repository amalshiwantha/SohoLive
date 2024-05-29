package com.soho.sohoapp.live.view.screens.signin

import com.soho.sohoapp.live.model.SignInRequest
import com.soho.sohoapp.live.network.common.AlertState
import com.soho.sohoapp.live.network.common.ProgressBarState

data class SignInState(
    val request: SignInRequest = SignInRequest(),
    val isLoginSuccess: Boolean = false,
    val loadingState: ProgressBarState = ProgressBarState.Idle,
    val alertState: AlertState = AlertState.Idle,
)
