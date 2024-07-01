package com.soho.sohoapp.live.ui.view.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.soho.sohoapp.live.SohoLiveApp.Companion.context
import com.soho.sohoapp.live.enums.SocialMediaInfo
import com.soho.sohoapp.live.model.SMProfile
import com.soho.sohoapp.live.model.SocialMediaProfile
import com.soho.sohoapp.live.ui.view.screens.signin.gauth.GoogleUserModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _isCallSMConnect = MutableStateFlow(SocialMediaInfo.NONE)
    val isCallSMConnect: StateFlow<SocialMediaInfo> = _isCallSMConnect.asStateFlow()

    private val _stateIsSMConnected =
        MutableStateFlow(SocialMediaProfile(SocialMediaInfo.NONE, SMProfile()))
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
    fun fetchGoogleUser(gUser: GoogleUserModel) {
        viewModelScope.launch {
            _stateGoogleAuth.update {
                it.copy(
                    name = gUser.name,
                    email = gUser.email,
                    image = gUser.image,
                    isLoggedIn = gUser.isLoggedIn
                )
            }
        }
    }

    fun resetState() {
        _stateGoogleAuth.update { GoogleUserModel() }
    }

    fun resetViewModelState() {
        _stateIsSMConnected.update { SocialMediaProfile(SocialMediaInfo.NONE, SMProfile()) }
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

}