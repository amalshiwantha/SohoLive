package com.soho.sohoapp.live.ui.view.screens.signin

import com.soho.sohoapp.live.model.ResetPwRequest
import com.soho.sohoapp.live.model.SignInRequest

sealed class SignInEvent {
    data class OnUpdateRequest(val request: SignInRequest) : SignInEvent()
    data class OnForgetPWRequest(val request: SignInRequest) : SignInEvent()
    data class OnUpdateSetPWRequest(val request: ResetPwRequest) : SignInEvent()
    data object CallSignIn : SignInEvent()
    data object DismissAlert : SignInEvent()
    data object CallResetPassword : SignInEvent()
    data object CallForgetPassword : SignInEvent()
}
