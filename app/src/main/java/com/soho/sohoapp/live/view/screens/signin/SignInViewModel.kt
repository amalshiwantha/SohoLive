package com.soho.sohoapp.live.view.screens.signin

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soho.sohoapp.live.model.SignInRequest
import com.soho.sohoapp.live.network.api.soho.SohoApiRepository
import com.soho.sohoapp.live.network.common.ApiState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SignInViewModel(private val apiRepo: SohoApiRepository) : ViewModel() {
    private val state: MutableState<SignInState> = mutableStateOf(SignInState())

    fun onTriggerEvent(signInEvent: SignInEvent) {
        when (signInEvent) {
            SignInEvent.CallSignIn -> callSignIn()
            is SignInEvent.OnUpdateRequest -> updateRequest(signInEvent.request)
        }
    }

    private fun updateRequest(event: SignInRequest) {
        state.value = state.value.copy(request = event)
    }

    private fun callSignIn() {
        state.value.request?.let {
            apiRepo.login(it).onEach { apiState ->
                when (apiState) {
                    is ApiState.Data -> {
                        apiState.data?.let { result ->
                            state.value = state.value.copy(response = result)
                        }
                    }

                    is ApiState.Loading -> {
                        state.value =
                            state.value.copy(progressBarState = apiState.progressBarState)
                    }

                    is ApiState.NetworkStatus -> TODO()
                    is ApiState.Response -> TODO()
                }
            }.launchIn(viewModelScope)
        }
    }
}