package com.soho.sohoapp.live.view.screens.signin

import com.soho.sohoapp.live.model.SignInRequest
import com.soho.sohoapp.live.network.common.ProgressBarState
import network.common.NetworkState

data class SignInState(
    val request: SignInRequest? = null,
    val response: String? = null,
    val progressBarState: ProgressBarState = ProgressBarState.Idle,
    val networkState: NetworkState = NetworkState.Good
)
