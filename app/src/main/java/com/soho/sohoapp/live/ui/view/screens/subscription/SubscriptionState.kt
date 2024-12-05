package com.soho.sohoapp.live.ui.view.screens.subscription

import androidx.compose.runtime.MutableState
import com.soho.sohoapp.live.enums.FieldType
import com.soho.sohoapp.live.model.Subscription
import com.soho.sohoapp.live.network.common.AlertState
import com.soho.sohoapp.live.network.common.ProgressBarState
import com.soho.sohoapp.live.network.response.DataVidRes

data class SubscriptionState(
    val isSuccess: Boolean = false,
    val loadingState: ProgressBarState = ProgressBarState.Idle,
    val loadingMessage: String = "Subscription Loading...",
    val alertState: AlertState = AlertState.Idle,
    val errorStates: Map<FieldType, String> = emptyMap(),
    var apiResponse: MutableState<Subscription>? = null,
)
