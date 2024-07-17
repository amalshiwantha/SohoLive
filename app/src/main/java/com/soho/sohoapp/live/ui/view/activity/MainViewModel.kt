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

    //update LiveData
    fun updateSocialMediaState(smInfo: SocialMediaInfo) {
        _isCallSMConnect.value = smInfo
    }

    //Google
    fun saveSMProfile(smProfile: SocialMediaProfile) {
        viewModelScope.launch {
            saveConnectedSMProfileList(smProfile)
        }
    }

    fun resetSMConnectState() {
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


    //add or replace a connected social media profile
    private fun saveConnectedSMProfileList(newProfile: SocialMediaProfile) {
        viewModelScope.launch {
            val currentList =
                dataStore.getSMProfileList() ?: ConnectedSocialProfile(mutableListOf())

            currentList.smProfileList.removeAll { it.smInfo == newProfile.smInfo }
            currentList.smProfileList.add(newProfile.apply { smInfo.isItemChecked = true })

            // Save the updated profile list
            dataStore.saveSMProfileList(currentList)
            getSavedSMProfile(newProfile, currentList)
        }
    }

    private fun getSavedSMProfile(smProfile: SocialMediaProfile, profList: ConnectedSocialProfile) {
        viewModelScope.launch {
            val list = profList.smProfileList
            val foundProfile = list.find { it.smInfo == smProfile.smInfo }
            if (foundProfile != null) {
                _stateIsSMConnected.update { foundProfile }
            }
        }
    }
}