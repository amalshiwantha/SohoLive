package com.soho.sohoapp.live.ui.view.screens.profile

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soho.sohoapp.live.datastore.AppDataStoreManager
import com.soho.sohoapp.live.enums.AlertConfig
import com.soho.sohoapp.live.network.api.soho.SohoApiRepository
import com.soho.sohoapp.live.network.common.AlertState
import com.soho.sohoapp.live.network.common.ApiState
import com.soho.sohoapp.live.utility.AppEvent
import com.soho.sohoapp.live.utility.AppEventBus
import com.soho.sohoapp.live.utility.Const.Companion.ERR_VAL
import com.soho.sohoapp.live.utility.getAppVersion
import com.soho.sohoapp.live.utility.getForceExitMessage
import com.soho.sohoapp.live.utility.toErrorCode
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val apiRepo: SohoApiRepository,
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
                    AppEventBus.sendEvent(AppEvent.NavigateToLogin(true))
                }
                mState.value = mState.value.copy(alertState = AlertState.Idle)
            }
        }
    }

    fun loadProfileData() {
        viewModelScope.launch {
            dataStore.userProfile.collect { profile ->
                profile?.let {
                    mState.value = mState.value.copy(
                        profileName = it.name,
                        profileImage = it.imageUrl,
                        appVersion = getAppVersion().first
                    )

                    getActivePlan(profile.authenticationToken)
                }
            }
        }
    }

    //Check ActivePlan and call getSubsPlans
    private fun getActivePlan(authToken: String) {
        apiRepo.getCurrentPlan(authToken)
            .onEach { apiState ->
                when (apiState) {
                    is ApiState.Data -> {
                        apiState.data?.let { activeRes ->
                            if (activeRes.responseType.equals(ERR_VAL)) {
                                //ForceLogout Now
                                val forceExit =
                                    getForceExitMessage(activeRes.response?.toErrorCode())
                                AppEventBus.sendEvent(AppEvent.ForceLogout(forceExit))
                            }
                        }
                    }

                    else -> {}
                }
            }.launchIn(viewModelScope)
    }

    fun showLogoutConfirm() {
        mState.value = mState.value.copy(
            alertState = AlertState.Display(AlertConfig.SIGN_OUT_ALERT)
        )
    }

}