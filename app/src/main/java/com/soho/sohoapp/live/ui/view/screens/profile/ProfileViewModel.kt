package com.soho.sohoapp.live.ui.view.screens.profile

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soho.sohoapp.live.datastore.AppDataStoreManager
import com.soho.sohoapp.live.utility.getAppVersion
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val dataStore: AppDataStoreManager
) : ViewModel() {

    val mState: MutableState<ProfileState> = mutableStateOf(ProfileState())

    init {
        viewModelScope.launch {
            dataStore.userProfile.collect { profile ->
                profile?.let {
                    mState.value = mState.value.copy(
                        profileName = "Smith Jhone ",
                        profileImage = "https://img.freepik.com/free-photo/portrait-man-laughing_23-2148859448.jpg?size=338&ext=jpg",
                        appVersion = getAppVersion()
                    )
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            dataStore.clearAllData()
        }
    }

}