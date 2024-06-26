package com.soho.sohoapp.live.ui.view.activity

import androidx.lifecycle.ViewModel
import com.soho.sohoapp.live.enums.SocialMediaInfo
import com.soho.sohoapp.live.model.SocialMediaProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {
    private val _isCallSMConnect = MutableStateFlow(SocialMediaInfo.NONE)
    val isCallSMConnect: StateFlow<SocialMediaInfo> = _isCallSMConnect.asStateFlow()

    private val _isSMConnected =
        MutableStateFlow(SocialMediaProfile(SocialMediaInfo.NONE, mutableListOf()))
    val isSMConnected: StateFlow<SocialMediaProfile> = _isSMConnected.asStateFlow()

    fun updateSocialMediaState(smInfo: SocialMediaInfo) {
        _isCallSMConnect.value = smInfo
    }

    fun saveSocialMediaProfile(smProfile: SocialMediaProfile) {
        _isSMConnected.value = smProfile
    }
}