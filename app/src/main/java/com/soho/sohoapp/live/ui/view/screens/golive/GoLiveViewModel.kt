package com.soho.sohoapp.live.ui.view.screens.golive

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soho.sohoapp.live.datastore.AppDataStoreManager
import com.soho.sohoapp.live.enums.AlertConfig
import com.soho.sohoapp.live.network.api.soho.SohoApiRepository
import com.soho.sohoapp.live.network.common.AlertState
import com.soho.sohoapp.live.network.common.ApiState
import com.soho.sohoapp.live.network.common.ProgressBarState
import com.soho.sohoapp.live.ui.view.screens.signin.SignInEvent
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class GoLiveViewModel(
    private val apiRepo: SohoApiRepository,
    private val userPref: AppDataStoreManager
) : ViewModel() {

    val mState: MutableState<GoLiveState> = mutableStateOf(GoLiveState())

    fun onTriggerEvent(event: GoLiveEvent) {
        when (event) {
            GoLiveEvent.CallLoadProperties -> {
                loadProfile()
            }
            GoLiveEvent.DismissAlert -> {}
        }
    }

    private fun loadProfile() {
        mState.value =
            mState.value.copy(loadingState = ProgressBarState.Loading)

        viewModelScope.launch {
            userPref.userProfile.collect { profile ->
                profile?.let {
                    loadPropertyListing(it.authenticationToken)
                }
            }
        }
    }

    private fun loadPropertyListing(authToken: String) {

        apiRepo.goLivePropertyListing(authToken).onEach { apiState ->

            when (apiState) {

                is ApiState.Data -> {
                    apiState.data?.let { result ->
                        val isSuccess = !result.responseType.equals("error")

                        if (isSuccess) {
                            mState.value = mState.value.copy(isSuccess = true)
                        } else {
                            mState.value =
                                mState.value.copy(
                                    alertState = AlertState.Display(
                                        AlertConfig.SIGN_IN_ERROR.apply {
                                            result.response?.let {
                                                message = it
                                            }
                                        }
                                    )
                                )
                        }
                    }
                }

                is ApiState.Loading -> {
                    mState.value =
                        mState.value.copy(loadingState = apiState.progressBarState)
                }

                is ApiState.Alert -> {
                    mState.value = mState.value.copy(alertState = apiState.alertState)
                }
            }
        }.launchIn(viewModelScope)
    }
}