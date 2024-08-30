package com.soho.sohoapp.live.utility

import com.soho.sohoapp.live.enums.CastEnd
import com.soho.sohoapp.live.model.SocialMediaProfile
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow


sealed class AppEvent {
    data class SMProfile(val smProfile: SocialMediaProfile) : AppEvent()
    data class LiveEndStatus(val castEnd: CastEnd) : AppEvent()
    data class NavigateToLogin(val isLogout: Boolean) : AppEvent()
}

object AppEventBus {
    private val _events = MutableSharedFlow<Any>()
    val events = _events.asSharedFlow()

    suspend fun sendEvent(event: Any) {
        _events.emit(event)
    }
}
