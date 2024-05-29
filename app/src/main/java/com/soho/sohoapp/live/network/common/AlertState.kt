package com.soho.sohoapp.live.network.common

sealed class AlertState {
    data object Display : AlertState()
    data object Idle : AlertState()
}

