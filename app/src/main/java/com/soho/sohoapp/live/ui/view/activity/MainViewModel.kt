package com.soho.sohoapp.live.ui.view.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.soho.sohoapp.live.SohoLiveApp.Companion.context
import com.soho.sohoapp.live.datastore.AppDataStoreManager
import com.soho.sohoapp.live.enums.SocialMediaInfo
import com.soho.sohoapp.live.model.ConnectedSocialProfile
import com.soho.sohoapp.live.model.SocialMediaProfile
import com.soho.sohoapp.live.ui.view.screens.signin.gauth.GoogleUserModel
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

    private val _stateGoogleAuth = MutableStateFlow(GoogleUserModel())
    val stateGoogleAuth = _stateGoogleAuth.asStateFlow()

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
            val gUser = smProfile.profile

            _stateGoogleAuth.update {
                it.copy(
                    name = gUser.fullName,
                    email = gUser.email,
                    image = gUser.imageUrl,
                    isLoggedIn = gUser.token != null
                )
            }

            saveSMProfileList(smProfile)
        }
    }

    fun resetState() {
        _stateGoogleAuth.update { GoogleUserModel() }
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
                } ?: run {
                    val freshList = mutableListOf<SocialMediaProfile>()
                    freshList.add(smProfile)

                    val connectedSMProfile = ConnectedSocialProfile(freshList)
                    dataStore.saveConnectedSMProfile(connectedSMProfile)
                }
            }
        }
    }
}