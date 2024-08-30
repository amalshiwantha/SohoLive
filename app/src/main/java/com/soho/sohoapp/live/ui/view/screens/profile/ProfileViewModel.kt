package com.soho.sohoapp.live.ui.view.screens.profile

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soho.sohoapp.live.datastore.AppDataStoreManager
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
                        profileName = "Jhone Smith",
                        profileImage = "http://www.",
                        appVersion = "1.0.0.9"
                    )
                }
            }
        }
    }

    fun logout() {
        //dataStore.clearData()
    }

}