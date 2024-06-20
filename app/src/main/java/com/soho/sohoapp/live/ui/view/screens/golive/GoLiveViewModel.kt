package com.soho.sohoapp.live.ui.view.screens.golive

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soho.sohoapp.live.enums.AlertConfig
import com.soho.sohoapp.live.network.api.soho.SohoApiRepository
import com.soho.sohoapp.live.network.common.AlertState
import com.soho.sohoapp.live.network.common.ApiState
import com.soho.sohoapp.live.ui.view.screens.signin.SignInEvent
import com.soho.sohoapp.live.ui.view.screens.signin.SignInState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class GoLiveViewModel(private val apiRepo: SohoApiRepository) : ViewModel() {

    val mStateLogin: MutableState<SignInState> = mutableStateOf(SignInState())

    init {
        loadPropertyListing()
    }

    fun onTriggerEvent(signInEvent: SignInEvent) {
        when (signInEvent) {
            SignInEvent.CallSignIn -> {}
            is SignInEvent.OnUpdateRequest -> {}
            SignInEvent.DismissAlert -> {}
        }
    }

    private fun loadPropertyListing() {
        apiRepo.goLivePropertyListing().onEach { apiState ->

            when (apiState) {

                is ApiState.Data -> {
                    apiState.data?.let { result ->
                        val isSuccessLogin = !result.responseType.equals("error")

                        if (isSuccessLogin) {
                            mStateLogin.value = mStateLogin.value.copy(isLoginSuccess = true)
                        } else {
                            mStateLogin.value =
                                mStateLogin.value.copy(
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
                    mStateLogin.value =
                        mStateLogin.value.copy(loadingState = apiState.progressBarState)
                }

                is ApiState.Alert -> {
                    mStateLogin.value = mStateLogin.value.copy(alertState = apiState.alertState)
                }
            }
        }.launchIn(viewModelScope)
    }
}