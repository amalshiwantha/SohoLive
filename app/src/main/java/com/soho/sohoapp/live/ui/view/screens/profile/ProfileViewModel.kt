package com.soho.sohoapp.live.ui.view.screens.profile

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soho.sohoapp.live.datastore.AppDataStoreManager
import com.soho.sohoapp.live.enums.AlertConfig
import com.soho.sohoapp.live.enums.SocialMediaInfo
import com.soho.sohoapp.live.model.MainStateHolder
import com.soho.sohoapp.live.model.SocialMediaProfile
import com.soho.sohoapp.live.network.common.AlertState
import com.soho.sohoapp.live.ui.view.screens.golive.doLogout
import com.soho.sohoapp.live.utility.AppEvent
import com.soho.sohoapp.live.utility.AppEventBus
import com.soho.sohoapp.live.utility.getAppVersion
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val dataStore: AppDataStoreManager
) : ViewModel() {

    val mState: MutableState<ProfileState> = mutableStateOf(ProfileState())

    fun onTriggerEvent(event: ProfileEvent) {
        when (event) {
            ProfileEvent.DismissAlert -> {
                mState.value = mState.value.copy(alertState = AlertState.Idle)
            }

            ProfileEvent.LogoutDismissAlert -> {
                viewModelScope.launch {
                    val smFb = SocialMediaProfile().apply {
                        smInfo = SocialMediaInfo.FACEBOOK
                        profile.isConnected = false
                        smInfo.isConnect = false
                        smInfo.isItemChecked = false
                    }

                    val smYT = SocialMediaProfile().apply {
                        smInfo = SocialMediaInfo.YOUTUBE
                        profile.isConnected = false
                        smInfo.isConnect = false
                        smInfo.isItemChecked = false
                    }
                    doLogout(smFb)
                    doLogout(smYT)
                    MainStateHolder.mState.reset()
                    dataStore.clearAllData()
                    AppEventBus.sendEvent(AppEvent.NavigateToLogin(true))
                }
                mState.value = mState.value.copy(alertState = AlertState.Idle)
            }
        }
    }

    init {
        viewModelScope.launch {
            dataStore.userProfile.collect { profile ->
                profile?.let {
                    mState.value = mState.value.copy(
                        profileName = it.name,
                        profileImage = it.imageUrl,
                        appVersion = getAppVersion()
                    )
                }
            }
        }
    }

    fun showLogoutConfirm() {
        mState.value = mState.value.copy(
            alertState = AlertState.Display(AlertConfig.SIGN_OUT_ALERT)
        )
    }

}