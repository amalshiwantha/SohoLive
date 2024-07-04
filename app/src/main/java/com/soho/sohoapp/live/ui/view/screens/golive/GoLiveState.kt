package com.soho.sohoapp.live.ui.view.screens.golive

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import com.soho.sohoapp.live.enums.FieldType
import com.soho.sohoapp.live.model.AgencyItem
import com.soho.sohoapp.live.model.PropertyItem
import com.soho.sohoapp.live.network.common.AlertState
import com.soho.sohoapp.live.network.common.ProgressBarState
import com.soho.sohoapp.live.network.response.DataGoLive
import com.soho.sohoapp.live.network.response.TsPropertyResponse

data class GoLiveState(
    val isSuccess: Boolean = false,
    val loadingState: ProgressBarState = ProgressBarState.Idle,
    val loadingMessage: String = "Property Loading",
    val alertState: AlertState = AlertState.Idle,
    val errorStates: Map<FieldType, String> = emptyMap(),
    val apiResults: DataGoLive? = null,
    val tsResults: TsPropertyResponse? = null,
    val results: DataGoLive? = null,
)

data class GoLiveAssets(
    var propertyListState: MutableState<List<PropertyItem>>? = null,
    var agencyListState: MutableState<List<AgencyItem>>? = null,
    var stepId: MutableState<Int> = mutableIntStateOf(3)
)
