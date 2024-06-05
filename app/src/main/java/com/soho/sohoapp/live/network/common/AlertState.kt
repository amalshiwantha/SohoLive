package com.soho.sohoapp.live.network.common

import com.soho.sohoapp.live.enums.AlertConfig

/*sealed class AlertState {
    data object Display : AlertState()
    data object Idle : AlertState()
}*/

sealed class AlertState {
    data object Idle : AlertState()
    data class Display(val config: AlertConfig) : AlertState()
}

