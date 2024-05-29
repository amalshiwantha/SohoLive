package com.soho.sohoapp.live.view.screens.signin

import com.soho.sohoapp.live.model.SignInRequest

sealed class SignInEvent {
    data class OnUpdateRequest(val request: SignInRequest) : SignInEvent()
    data object CallSignIn : SignInEvent()
}
