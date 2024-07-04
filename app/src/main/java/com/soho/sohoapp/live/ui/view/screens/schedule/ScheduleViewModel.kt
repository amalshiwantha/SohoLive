package com.soho.sohoapp.live.ui.view.screens.schedule

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soho.sohoapp.live.datastore.AppDataStoreManager
import com.soho.sohoapp.live.enums.AlertConfig
import com.soho.sohoapp.live.model.GoLiveSubmit
import com.soho.sohoapp.live.network.api.soho.SohoApiRepository
import com.soho.sohoapp.live.network.common.AlertState
import com.soho.sohoapp.live.network.common.ApiState
import com.soho.sohoapp.live.network.common.ProgressBarState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ScheduleViewModel(
    private val apiRepo: SohoApiRepository,
    private val dataStore: AppDataStoreManager,
) : ViewModel() {
    val mState: MutableState<ScheduleState> = mutableStateOf(ScheduleState())

    fun onTriggerEvent(event: ScheduleEvent) {
        when (event) {
            ScheduleEvent.DismissAlert -> {
                mState.value = mState.value.copy(alertState = AlertState.Idle)
            }
            is ScheduleEvent.CallSubmit -> {
                submitGoLiveData(event.submitData)
            }
        }
    }

    private fun submitGoLiveData(submitData: GoLiveSubmit) {
        mState.value = mState.value.copy(loadingState = ProgressBarState.Loading)
        mState.value = mState.value.copy(
            loadingState = ProgressBarState.Loading,
            loadingMessage = "GoLive Requesting"
        )

        viewModelScope.launch {
            dataStore.userProfile.collect { profile ->
                profile?.let {
                    submitNowGoLive(it.authenticationToken, submitData)
                }
            }
        }
    }

    //same submitNowGoLive() has GoLiveViewModel
    private fun submitNowGoLive(authToken: String, submitData: GoLiveSubmit) {

        apiRepo.submitGoLive(authToken, submitData).onEach { apiState ->

            when (apiState) {

                is ApiState.Data -> {

                    apiState.data?.let { result ->

                        val isSuccess = !result.responseType.equals("error")
                        val errorMsg = result.response
                        val res = result.data

                        if (isSuccess) {
                            mState.value = mState.value.copy(results = res)
                            mState.value = mState.value.copy(isSuccess = true)
                        } else {
                            mState.value =
                                mState.value.copy(alertState = AlertState.Display(AlertConfig.GO_LIVE_SUBMIT_ERROR.apply {
                                    message = errorMsg.orEmpty()
                                }))
                        }
                    }
                }

                is ApiState.Loading -> {
                    mState.value = mState.value.copy(loadingState = apiState.progressBarState)
                }

                is ApiState.Alert -> {
                    mState.value = mState.value.copy(alertState = apiState.alertState)
                }
            }
        }.launchIn(viewModelScope)
    }


}