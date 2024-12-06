package com.soho.sohoapp.live.ui.view.screens.usage

import com.soho.sohoapp.live.enums.FieldType
import com.soho.sohoapp.live.model.SubscriptionCategory
import com.soho.sohoapp.live.network.common.AlertState
import com.soho.sohoapp.live.network.response.PlanData
import com.soho.sohoapp.live.network.response.UsageResponse

data class UsageState(
    val isSuccess: Boolean = false,
    val isLoading: Boolean = true,
    val loadingMessage: String = "Usage Loading...",
    val alertState: AlertState = AlertState.Idle,
    val errorStates: Map<FieldType, String> = emptyMap(),
    val usageRes: List<UsageResponse> = listOf(),
)
