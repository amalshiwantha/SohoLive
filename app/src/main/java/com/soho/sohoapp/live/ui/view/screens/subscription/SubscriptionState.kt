package com.soho.sohoapp.live.ui.view.screens.subscription

import com.soho.sohoapp.live.enums.FieldType
import com.soho.sohoapp.live.model.SubscriptionCategory
import com.soho.sohoapp.live.network.common.AlertState
import kotlinx.serialization.SerialName

data class SubscriptionState(
    val isSuccess: Boolean = false,
    val isLoading: Boolean = true,
    val loadingMessage: String = "Subscription Loading...",
    val alertState: AlertState = AlertState.Idle,
    val errorStates: Map<FieldType, String> = emptyMap(),
    val planListRes: List<SubscriptionCategory> = listOf()
)
