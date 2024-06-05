package com.soho.sohoapp.live.view.screens.signin

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soho.sohoapp.live.model.SignInRequest
import com.soho.sohoapp.live.network.api.soho.SohoApiRepository
import com.soho.sohoapp.live.network.common.AlertState
import com.soho.sohoapp.live.network.common.ApiState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SignInViewModel(private val apiRepo: SohoApiRepository) : ViewModel() {
    val state: MutableState<SignInState> = mutableStateOf(SignInState())

    fun onTriggerEvent(signInEvent: SignInEvent) {
        when (signInEvent) {
            SignInEvent.CallSignIn -> callSignInApi()
            is SignInEvent.OnUpdateRequest -> updateRequest(signInEvent.request)
            SignInEvent.DismissAlert -> dismissAlertState()
        }
    }

    private fun dismissAlertState() {
        state.value =
            state.value.copy(alertState = AlertState.Idle)
    }

    private fun updateRequest(event: SignInRequest) {
        state.value = state.value.copy(request = event)
    }

    private fun callSignInApi() {
        apiRepo.signIn(state.value.request).onEach { apiState ->

            when (apiState) {

                is ApiState.Data -> {
                    apiState.data?.let { result ->
                        val isSuccessLogin = !result.responseType.equals("error")
                        state.value = state.value.copy(isLoginSuccess = isSuccessLogin)
                    }
                }

                is ApiState.Loading -> {
                    state.value = state.value.copy(loadingState = apiState.progressBarState)
                }

                is ApiState.Alert -> {
                    state.value = state.value.copy(alertState = apiState.alertState)
                }
            }
        }.launchIn(viewModelScope)
    }
}