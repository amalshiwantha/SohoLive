package com.soho.sohoapp.live.network.common

sealed class ProgressBarState {

    data object Loading : ProgressBarState()

    data object Idle : ProgressBarState()

}

