package com.soho.sohoapp.live.ui.view.screens.signin

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soho.sohoapp.live.datastore.AppDataStoreManager
import com.soho.sohoapp.live.enums.AlertConfig
import com.soho.sohoapp.live.enums.FieldType
import com.soho.sohoapp.live.model.ResetPwRequest
import com.soho.sohoapp.live.model.SignInRequest
import com.soho.sohoapp.live.network.api.soho.SohoApiRepository
import com.soho.sohoapp.live.network.common.AlertState
import com.soho.sohoapp.live.network.common.ApiState
import com.soho.sohoapp.live.network.common.ProgressBarState
import com.soho.sohoapp.live.network.response.Data
import com.soho.sohoapp.live.utility.Const.Companion.ERR_500
import com.soho.sohoapp.live.utility.Const.Companion.ERR_VAL
import com.soho.sohoapp.live.utility.formValidation
import com.soho.sohoapp.live.utility.toErrorCode
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SignInViewModel(
    private val apiRepo: SohoApiRepository,
    private val userPref: AppDataStoreManager
) : ViewModel() {
    val mStateLogin: MutableState<SignInState> = mutableStateOf(SignInState())

    fun onTriggerEvent(signInEvent: SignInEvent) {
        when (signInEvent) {
            SignInEvent.CallSignIn -> validateSignIn()
            SignInEvent.DismissAlert -> dismissAlertState()
            SignInEvent.CallResetPassword -> resetPwRequest()
            is SignInEvent.OnUpdateRequest -> updateRequest(signInEvent.request)
            is SignInEvent.OnForgetPWRequest -> forgetPwRequest(signInEvent.request)
            is SignInEvent.OnUpdateSetPWRequest -> resetPwRequest(signInEvent.request)
        }
    }

    private fun dismissAlertState() {
        mStateLogin.value =
            mStateLogin.value.copy(alertState = AlertState.Idle)
    }

    private fun resetPwRequest() {
        println("resetPwRequest : ${mStateLogin.value.resetPwRequest}")
        mStateLogin.value.resetPwRequest.let {
            val mapList = mutableMapOf<FieldType, String?>()
            mapList[FieldType.RESET_NPW] = it.newPassword
            mapList[FieldType.RESET_CPW] = it.confirmPassword

            mStateLogin.value = formValidation(mStateLogin, mapList)

            if (mStateLogin.value.errorStates.isEmpty()) {
                //callSignInApi(it)
            }
        }
    }

    private fun resetPwRequest(event: ResetPwRequest) {
        mStateLogin.value = mStateLogin.value.copy(resetPwRequest = event)
    }

    private fun forgetPwRequest(event: SignInRequest) {
        mStateLogin.value = mStateLogin.value.copy(isForgetPwLinkSent = true)
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

        mStateLogin.value = mStateLogin.value.copy(
            loadingState = ProgressBarState.Loading
        )

        apiRepo.signIn(requestParam).onEach { apiState ->

            when (apiState) {

                is ApiState.Data -> {
                    apiState.data?.let { result ->
                        val isSuccessLogin = !result.responseType.equals(ERR_VAL)

                        if (isSuccessLogin) {

                            //call get activePlan
                            result.data?.authenticationToken?.let { authToken ->

                                apiRepo.getCurrentPlan(authToken).onEach { apiState ->
                                    when (apiState) {
                                        is ApiState.Data -> {
                                            apiState.data?.let { activeRes ->
                                                val isActivated =
                                                    !activeRes.responseType.equals(ERR_VAL)

                                                if (isActivated) {
                                                    setAsLoggedState(result.data)
                                                    mStateLogin.value =
                                                        mStateLogin.value.copy(isLoginSuccess = true)
                                                }
                                            }
                                        }

                                        else -> {}
                                    }
                                }.launchIn(viewModelScope)
                            }

                        } else {
                            val errCode = result.response?.toErrorCode() ?: ERR_500

                            when (errCode) {
                                ERR_500 -> {
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

                                else -> {
                                    mStateLogin.value = mStateLogin.value.copy(
                                        errorStates = mStateLogin.value.errorStates.toMutableMap()
                                            .apply {
                                                put(FieldType.LOGIN_EMAIL, "")
                                                put(
                                                    FieldType.LOGIN_PW,
                                                    "Account not found or subscription expired. Please try a different email or sign up at soho.com.au/agents/livecast"
                                                )
                                            })
                                }
                            }
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

    private fun setAsLoggedState(profileData: Data?) {
        viewModelScope.launch {
            profileData?.let {
                userPref.saveUserProfile(it)
                userPref.setLoginState(true)
            }
        }
    }
}