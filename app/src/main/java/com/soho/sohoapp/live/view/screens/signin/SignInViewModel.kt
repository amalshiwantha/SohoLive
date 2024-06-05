package com.soho.sohoapp.live.view.screens.signin

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soho.sohoapp.live.enums.AlertConfig
import com.soho.sohoapp.live.enums.FieldType
import com.soho.sohoapp.live.model.SignInRequest
import com.soho.sohoapp.live.network.api.soho.SohoApiRepository
import com.soho.sohoapp.live.network.common.AlertState
import com.soho.sohoapp.live.network.common.ApiState
import com.soho.sohoapp.live.utility.formValidation
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SignInViewModel(private val apiRepo: SohoApiRepository) : ViewModel() {
    val mStateLogin: MutableState<SignInState> = mutableStateOf(SignInState())

    fun onTriggerEvent(signInEvent: SignInEvent) {
        when (signInEvent) {
            SignInEvent.CallSignIn -> validateSignIn()
            is SignInEvent.OnUpdateRequest -> updateRequest(signInEvent.request)
            SignInEvent.DismissAlert -> dismissAlertState()
        }
    }

    private fun dismissAlertState() {
        mStateLogin.value =
            mStateLogin.value.copy(alertState = AlertState.Idle)
    }

    private fun updateRequest(event: SignInRequest) {
        mStateLogin.value = mStateLogin.value.copy(request = event)
    }

    private fun validateSignIn() {

        mStateLogin.value.request.let {
            val mapList = mutableMapOf<FieldType, String?>()
            mapList[FieldType.LOGIN_EMAIL] = it.email
            mapList[FieldType.LOGIN_PW] = it.password

            mStateLogin.value = formValidation(mStateLogin, mapList)

            if (mStateLogin.value.errorStates.isEmpty()) {
                callSignInApi(it)
            }
        }
    }

    private fun callSignInApi(requestParam: SignInRequest) {

        apiRepo.signIn(requestParam).onEach { apiState ->

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