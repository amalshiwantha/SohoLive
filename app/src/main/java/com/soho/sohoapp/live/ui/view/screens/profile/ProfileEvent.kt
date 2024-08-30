package com.soho.sohoapp.live.ui.view.screens.profile

sealed class ProfileEvent {
    data object LogoutDismissAlert : ProfileEvent()
    data object DismissAlert : ProfileEvent()
}
