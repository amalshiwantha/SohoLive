package com.soho.sohoapp.live.ui.view.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soho.sohoapp.live.datastore.AppDataStoreManager
import com.soho.sohoapp.live.enums.SocialMediaInfo
import com.soho.sohoapp.live.model.ConnectedSocialProfile
import com.soho.sohoapp.live.model.SocialMediaProfile
import com.soho.sohoapp.live.utility.AppEvent
import com.soho.sohoapp.live.utility.AppEventBus
import kotlinx.coroutines.GlobalScope
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

    fun updateSMConnectedState(smInfo: SocialMediaInfo) {
        getConnectedSMProfile(smInfo.name)
    }

    //save logged SM profile
    fun saveSMProfile(smProfile: SocialMediaProfile) {
        viewModelScope.launch {
            saveConnectedSMProfileList(smProfile)
        }
    }

    //remove logout SM profile
    fun removeSMProfile(smProfile: SocialMediaProfile) {
        viewModelScope.launch {
            val currentList =
                dataStore.getSMProfileList() ?: ConnectedSocialProfile(mutableListOf())

            currentList.smProfileList.removeIf { it.smInfo.name == smProfile.smInfo.name }

            dataStore.saveSMProfileList(currentList)
        }
    }

    fun resetSMConnectState() {
        _stateIsSMConnected.update { SocialMediaProfile() }
    }

    //add or replace a connected social media profile
    private fun saveConnectedSMProfileList(newProfile: SocialMediaProfile) {
        viewModelScope.launch {
            val currentList =
                dataStore.getSMProfileList() ?: ConnectedSocialProfile(mutableListOf())

            currentList.smProfileList.removeAll { it.smInfo == newProfile.smInfo }
            currentList.smProfileList.add(newProfile.apply {
                smInfo.isItemChecked = true
                smInfo.isConnect = true
            })

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

    private fun getConnectedSMProfile(name: String) {
        viewModelScope.launch {
            val currentList =
                dataStore.getSMProfileList() ?: ConnectedSocialProfile(mutableListOf())
            val foundSMprofile = currentList.smProfileList.find { it.smInfo.name == name }

            if (foundSMprofile != null) {
                _stateIsSMConnected.update { foundSMprofile }
            }
        }
    }

    fun resetSendEvent(){
        viewModelScope.launch {
            AppEventBus.sendEvent(AppEvent.SMProfile(SocialMediaProfile()))
        }
    }
}