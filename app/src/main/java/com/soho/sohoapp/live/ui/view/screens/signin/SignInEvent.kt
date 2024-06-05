package com.soho.sohoapp.live.ui.view.screens.signin

import com.soho.sohoapp.live.model.SignInRequest

sealed class SignInEvent {
    data class OnUpdateRequest(val request: SignInRequest) : SignInEvent()
    data object CallSignIn : SignInEvent()
    data object DismissAlert : SignInEvent()
}
