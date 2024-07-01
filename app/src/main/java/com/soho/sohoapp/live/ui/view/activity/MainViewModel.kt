package com.soho.sohoapp.live.ui.view.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.soho.sohoapp.live.SohoLiveApp.Companion.context
import com.soho.sohoapp.live.datastore.AppDataStoreManager
import com.soho.sohoapp.live.enums.SocialMediaInfo
import com.soho.sohoapp.live.model.ConnectedSocialProfile
import com.soho.sohoapp.live.model.Profile
import com.soho.sohoapp.live.model.SocialMediaProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(private val dataStore: AppDataStoreManager) : ViewModel() {
    private val _isCallSMConnect = MutableStateFlow(SocialMediaInfo.NONE)
    val isCallSMConnect: StateFlow<SocialMediaInfo> = _isCallSMConnect.asStateFlow()

    private val _stateIsSMConnected = MutableStateFlow(SocialMediaProfile())
    val stateIsSMConnected = _stateIsSMConnected.asStateFlow()

    private val _stateConnectedProfile = MutableStateFlow(Profile())
    val stateConnectedProfile = _stateConnectedProfile.asStateFlow()

    //update LiveData
    fun updateSocialMediaState(smInfo: SocialMediaInfo) {
        _isCallSMConnect.value = smInfo
    }

    fun saveSocialMediaProfile(smProfile: SocialMediaProfile) {
        viewModelScope.launch {
            _stateIsSMConnected.update { smProfile }
        }
    }

    //Google
    fun fetchGoogleUser(smProfile: SocialMediaProfile) {
        viewModelScope.launch {
            saveSMProfileList(smProfile)
        }
    }

    fun resetState() {
        _stateConnectedProfile.update { Profile() }
    }

    fun resetViewModelState() {
        _stateIsSMConnected.update { SocialMediaProfile() }
    }

    fun googleSignOut() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
        val googleSignInClient = GoogleSignIn.getClient(context, gso)
        googleSignInClient.signOut().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                println("myGuser signed out successfully.")
            } else {
                println("myGuser Sign out failed.")
            }
        }
    }
    //Google End


    //save SM profiles in Local
    private fun saveSMProfileList(smProfile: SocialMediaProfile) {
        viewModelScope.launch {
            dataStore.connectedSMProfile.collect { profileList ->
                profileList?.let { profList ->
                    val list = profList.smProfileList
                    val hasStored = list.find { it.smInfo == smProfile.smInfo }

                    if (hasStored == null) {
                        list.add(smProfile)
                        profList.copy(smProfileList = list)
                    }

                    getSavedSMProfile(smProfile,profList)

                } ?: run {
                    val freshList = mutableListOf<SocialMediaProfile>()
                    freshList.add(smProfile)

                    val connectedSMProfile = ConnectedSocialProfile(freshList)
                    dataStore.saveConnectedSMProfile(connectedSMProfile)
                }
            }
        }
    }

    private fun getSavedSMProfile(smProfile: SocialMediaProfile, profList: ConnectedSocialProfile) {
        viewModelScope.launch {
            val list = profList.smProfileList
            val foundProfile = list.find { it.smInfo == smProfile.smInfo }
            if (foundProfile != null) {
                _stateConnectedProfile.update { foundProfile.profile }
            }
        }
    }


}