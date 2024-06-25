package com.soho.sohoapp.live.ui.view.activity

import androidx.compose.runtime.Immutable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.facebook.Profile
import com.facebook.ProfileTracker
import com.soho.sohoapp.live.enums.SocialMediaInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {
    private val profileTracker =
        object : ProfileTracker() {
            override fun onCurrentProfileChanged(oldProfile: Profile?, currentProfile: Profile?) {
                if (currentProfile != null) {
                    this@MainViewModel.updateProfile(currentProfile)
                } else {
                    this@MainViewModel.resetProfile()
                }
            }
        }

    private val _profileViewState = MutableLiveData(ProfileViewState(Profile.getCurrentProfile()))
    val profileViewState: LiveData<ProfileViewState> = _profileViewState

    private val _isCallSMConnect = MutableStateFlow(SocialMediaInfo.NONE)
    val isCallSMConnect: StateFlow<SocialMediaInfo> = _isCallSMConnect.asStateFlow()

    fun updateSocialMediaState(smInfo: SocialMediaInfo) {
        _isCallSMConnect.value = smInfo
    }

    override fun onCleared() {
        profileTracker.stopTracking()
        super.onCleared()
    }

    private fun updateProfile(profile: Profile) {
        _profileViewState.value = _profileViewState.value?.copy(profile = profile)
    }

    private fun resetProfile() {
        _profileViewState.value = _profileViewState.value?.copy(profile = null)
    }
}

@Immutable
data class ProfileViewState(
    val profile: Profile? = null
)